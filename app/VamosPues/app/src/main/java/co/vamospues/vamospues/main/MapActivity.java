package co.vamospues.vamospues.main;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Spinner;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapView;

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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        setSpinner();
    }

    private void setSpinner() {
        places = database.getPlaces();
        placesSpinner.setAdapter(new PlacesMapAdapter(context, R.layout.item_place, places));
    }

}
