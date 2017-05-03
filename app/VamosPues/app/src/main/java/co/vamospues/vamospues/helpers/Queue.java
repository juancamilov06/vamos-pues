package co.vamospues.vamospues.helpers;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Manuela Duque M on 08/02/2017.
 */

public class Queue {

    private static Queue instance;
    private static Context globContext;
    private static RequestQueue requestQueue;

    public Queue(Context context){
        globContext = context;
        requestQueue = getRequestQueue();
    }

    public static synchronized RequestQueue getRequestQueue(){
        if (requestQueue == null){
            requestQueue = Volley.newRequestQueue(globContext.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public static synchronized Queue getInstance(Context context){
        if (instance == null){
            instance = new Queue(context);
        }
        return instance;
    }

}
