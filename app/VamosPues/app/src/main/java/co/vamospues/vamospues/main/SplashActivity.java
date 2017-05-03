package co.vamospues.vamospues.main;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import co.vamospues.vamospues.R;
import co.vamospues.vamospues.database.DatabaseHandler;
import co.vamospues.vamospues.enums.Codes;
import co.vamospues.vamospues.enums.Services;
import co.vamospues.vamospues.helpers.Queue;
import co.vamospues.vamospues.helpers.Utils;
import co.vamospues.vamospues.models.ItemType;
import co.vamospues.vamospues.models.Music;
import co.vamospues.vamospues.models.Place;
import co.vamospues.vamospues.models.Zone;

public class SplashActivity extends AppCompatActivity {

    private AVLoadingIndicatorView indicator;
    private Context context;
    private DatabaseHandler database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        context = SplashActivity.this;
        database = new DatabaseHandler(context);
        indicator = (AVLoadingIndicatorView) findViewById(R.id.indicator);
        if (isFirstRun()) {
            getDataFromServer();
        } else {
            isSessionActive();
        }
    }

    private void isSessionActive() {

        String appToken = database.getToken();
        AccessToken facebookToken = AccessToken.getCurrentAccessToken();

        if (TextUtils.isEmpty(appToken) && facebookToken == null){
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            finish();
            return;
        }

        if (TextUtils.isEmpty(appToken) && facebookToken != null){
            LoginManager.getInstance().logOut();
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            finish();
            return;
        }

        if (!TextUtils.isEmpty(appToken) && facebookToken == null){
            database.deleteToken();
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            finish();
            return;
        }

        if (!TextUtils.isEmpty(appToken) && facebookToken != null){
            StringRequest request = new StringRequest(Request.Method.GET, Services.BASE_URL + Services.AUTH_CHECK_TOKEN_SERVICE + "?token=" + appToken, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try{
                        JSONObject responseObject = new JSONObject(response);
                        boolean success = responseObject.getBoolean("success");
                        if (success){
                            startActivity(new Intent(SplashActivity.this, MainActivity.class));
                            finish();
                        } else {
                            database.deleteToken();
                            LoginManager.getInstance().logOut();
                            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                            finish();
                        }
                    } catch(Exception e){
                        LoginManager.getInstance().logOut();
                        database.deleteToken();
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                        finish();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    finish();
                }
            });
            Queue.getInstance(context).addToRequestQueue(request);
        }
    }

    private void setFirstTime(boolean firstTime) {
        SharedPreferences prefs = getSharedPreferences(this.getPackageName(), MODE_PRIVATE);
        prefs.edit().putBoolean("firstrun", firstTime).apply();
    }

    private boolean isFirstRun() {
        SharedPreferences prefs = getSharedPreferences(this.getPackageName(), MODE_PRIVATE);
        return prefs.getBoolean("firstrun", true);
    }

    private void getDataFromServer() {
        indicator.smoothToShow();
        StringRequest request = new StringRequest(Request.Method.GET, Services.BASE_URL + Services.BASE_DATA_SERVICE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.v("Data", response);
                    JSONObject object = new JSONObject(response);
                    int code = object.getInt("code");
                    if (code == Codes.CODE_SUCCESSFUL){
                        new InsertDataAsync().execute(object.getJSONObject("data"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            @Override
            public Priority getPriority() {
                return Priority.IMMEDIATE;
            }

        };
        Queue.getInstance(context).addToRequestQueue(request);
    }

    private class InsertDataAsync extends AsyncTask<JSONObject, Void, Void>{

        @Override
        protected Void doInBackground(JSONObject... params) {
            JSONObject object = params[0];
            try {
                JSONArray zonesResponse = object.getJSONArray("zones");
                List<Zone> zones = new ArrayList<>();
                if (zonesResponse.length() > 0) {
                    for (int i = 0; i < zonesResponse.length(); i++) {
                        JSONObject currentZone = zonesResponse.getJSONObject(i);
                        Zone zone = new Zone();
                        zone.setId(currentZone.getInt("id"));
                        zone.setName(currentZone.getString("name"));
                        zones.add(zone);
                    }
                    database.insertZones(zones);
                }

                JSONArray musicResponse = object.getJSONArray("music");
                List<Music> musicStyles = new ArrayList<>();
                if (musicResponse.length() > 0) {
                    for (int i = 0; i < musicResponse.length(); i++) {
                        JSONObject currentMusic = musicResponse.getJSONObject(i);
                        Music music = new Music();
                        music.setId(currentMusic.getInt("id"));
                        music.setName(currentMusic.getString("name"));
                        musicStyles.add(music);
                    }
                    database.insertMusicTypes(musicStyles);
                }

                JSONArray itemTypesResponse = object.getJSONArray("item_types");
                List<ItemType> itemTypes = new ArrayList<>();
                if (itemTypesResponse.length() > 0){
                    for (int i = 0; i < itemTypesResponse.length(); i++) {
                        JSONObject currentType = itemTypesResponse.getJSONObject(i);
                        ItemType itemType = new ItemType();
                        itemType.setId(currentType.getInt("id"));
                        itemType.setName(currentType.getString("name"));
                        itemTypes.add(itemType);
                    }
                    database.insertItemTypes(itemTypes);
                }

                JSONArray placesResponse = object.getJSONArray("places");
                List<Place> places = new ArrayList<>();
                if (placesResponse.length() > 0) {
                    for (int i = 0; i < placesResponse.length(); i++) {
                        JSONObject currentPlace = placesResponse.getJSONObject(i);
                        Place place = new Place();
                        place.setId(currentPlace.getInt("id"));
                        place.setAddress(currentPlace.getString("address"));
                        place.setName(currentPlace.getString("name"));
                        place.setBudget(currentPlace.getDouble("budget"));
                        place.setDescription(currentPlace.getString("description"));
                        place.setLimitHour(currentPlace.getString("limit_hour"));
                        place.setDisco(currentPlace.getInt("is_disco") > 0);
                        place.setActive(currentPlace.getInt("is_active") > 0);
                        place.setLatitude(currentPlace.getDouble("latitude"));
                        place.setMusic(database.getMusic(currentPlace.getInt("music_id")));
                        place.setZone(database.getZone(currentPlace.getInt("zone_id")));
                        place.setLongitude(currentPlace.getDouble("longitude"));
                        place.setImageUrl(currentPlace.getString("image_url"));
                        place.setOpen(currentPlace.getString("open"));
                        place.setClose(currentPlace.getString("close"));
                        places.add(place);
                    }
                    database.insertPlaces(places);
                }
            } catch (JSONException e){

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            setFirstTime(false);
            indicator.smoothToHide();
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            finish();
        }
    }
}
