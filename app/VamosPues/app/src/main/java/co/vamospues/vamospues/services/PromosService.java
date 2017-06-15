package co.vamospues.vamospues.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import co.vamospues.vamospues.R;
import co.vamospues.vamospues.database.DatabaseHandler;
import co.vamospues.vamospues.enums.Services;
import co.vamospues.vamospues.helpers.Queue;
import co.vamospues.vamospues.main.MainActivity;
import co.vamospues.vamospues.models.Prefs;
import co.vamospues.vamospues.models.Promo;

/**
 * Created by Manuela Duque M on 27/05/2017.
 */

public class PromosService extends Service {

    private Context context;
    private DatabaseHandler database;

    private Looper mServiceLooper;
    private Prefs prefs;
    private ServiceHandler mServiceHandler;
    private Timer mTimer;
    private String token;

    @Override
    public void onCreate() {
        context = this;
        database = new DatabaseHandler(this);

        HandlerThread thread = new HandlerThread("PromosService",
                Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        checkPromosUpdates();
        return START_NOT_STICKY;
    }

    private void checkPromosUpdates(){
        prefs = database.getPrefs();
        mTimer = new Timer();
        if(prefs.getZone() != null && prefs.getMusic() != null) {
            mTimer.scheduleAtFixedRate(new ObservePromotions(), 0, 10000);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createNotification(final int id){

        NotificationManager nm = (NotificationManager)context.getSystemService(NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(context);
        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context,0,notificationIntent,0);

        builder.setContentIntent(contentIntent);
        builder.setContentText("Hay una nueva promocion para ti");
        builder.setSmallIcon(R.drawable.com_facebook_button_icon);
        builder.setPriority(Notification.PRIORITY_HIGH);
        builder.setContentTitle("Vamos Pues!");
        builder.setAutoCancel(true);
        builder.setDefaults(Notification.DEFAULT_ALL);

        Notification notification = builder.build();
        nm.notify(id,notification);
        System.out.println("Notified" + id);
    }

    private void handleResponse(JSONObject object) throws JSONException {
        JSONArray promos = object.getJSONArray("promos");
        if (promos.length() > 0) {
            for (int i = 0; i < promos.length(); i++) {
                JSONObject promoObject = promos.getJSONObject(i);
                int id = promoObject.getInt("id");
                if (!database.existsPromo(id)) {
                    Promo promo = new Promo();
                    promo.setId(id);
                    database.insertPromo(promo);
                    createNotification(id);
                }
            }
        }
    }

    private String getPromosUrl(Prefs prefs) {
        return Services.BASE_URL + Services.GET_FILTERED_PROMOS_SERVICE + "/?zone=" + prefs.getZone().getId()
                + "&music=" + prefs.getMusic().getId() + "&token=" + token;
    }

    private class ObservePromotions extends TimerTask {

        @Override
        public void run() {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    prefs = database.getPrefs();
                    token = database.getToken();
                    if (prefs.isNotify()) {
                        JsonObjectRequest request = new JsonObjectRequest(getPromosUrl(prefs), null, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    handleResponse(response);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                            }
                        });
                        Queue.getInstance(PromosService.this).addToRequestQueue(request);
                    }
                }
            });
        }
    }

    private final class ServiceHandler extends Handler {

        ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
