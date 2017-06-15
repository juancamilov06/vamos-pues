package co.vamospues.vamospues.main;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerViewOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import java.util.ArrayList;
import java.util.List;

import co.vamospues.vamospues.R;
import co.vamospues.vamospues.adapters.PlacesMapAdapter;
import co.vamospues.vamospues.database.DatabaseHandler;
import co.vamospues.vamospues.models.Place;

public class MapActivity extends AppCompatActivity {

    private Spinner placesSpinner;
    private List<Place> places = new ArrayList<>();
    private Context context;
    private DatabaseHandler database;
    private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, "pk.eyJ1IjoianVhbmNhbWlsb3YwNiIsImEiOiJjaXpuaGFiNmUwMjdrMndydmdpcmxoeGQ1In0.4soovvTozop8nqzq51g9lg");
        setContentView(R.layout.activity_map);

        context = MapActivity.this;
        database = new DatabaseHandler(context);

        mapView = (MapView) findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        placesSpinner = (Spinner) findViewById(R.id.places_spinner);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final MapboxMap mapboxMap) {
                placesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Place place = places.get(position);
                        MarkerViewOptions markerViewOptions = new MarkerViewOptions()
                                .position(new LatLng(place.getLatitude(), place.getLongitude()))
                                .title(place.getName())
                                .snippet(place.getAddress());

                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(new LatLng(place.getLatitude(), place.getLongitude()))
                                .zoom(17)
                                .bearing(180)
                                .tilt(30)
                                .build();

                        mapboxMap.animateCamera(CameraUpdateFactory
                                .newCameraPosition(cameraPosition), 4000);
                        mapboxMap.addMarker(markerViewOptions);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                if (places.size() > 0){
                    Place place = places.get(0);
                    MarkerViewOptions markerViewOptions = new MarkerViewOptions()
                            .position(new LatLng(place.getLatitude(), place.getLongitude()))
                            .title(place.getName())
                            .snippet(place.getAddress());

                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(new LatLng(place.getLatitude(), place.getLongitude()))
                            .zoom(17)
                            .bearing(180)
                            .tilt(30)
                            .build();

                    mapboxMap.animateCamera(CameraUpdateFactory
                            .newCameraPosition(cameraPosition), 4000);
                    mapboxMap.addMarker(markerViewOptions);
                }
            }
        });
        setSpinner();
    }

    private void setSpinner() {
        places = database.getPlaces();
        placesSpinner.setAdapter(new PlacesMapAdapter(context, R.layout.item_dropdown_place, places));
    }

}
