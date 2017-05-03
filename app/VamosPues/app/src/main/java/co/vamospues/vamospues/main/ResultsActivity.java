package co.vamospues.vamospues.main;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import co.vamospues.vamospues.R;
import co.vamospues.vamospues.adapters.ResultsAdapter;
import co.vamospues.vamospues.database.DatabaseHandler;
import co.vamospues.vamospues.helpers.ItemClickSupport;
import co.vamospues.vamospues.models.Place;

public class ResultsActivity extends AppCompatActivity {

    private List<Place> places;
    private DatabaseHandler database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        int musicId = getIntent().getIntExtra("music_id", 0);
        int zoneId = getIntent().getIntExtra("zone_id", 0);

        Context context = ResultsActivity.this;
        database = new DatabaseHandler(context);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        setSupportActionBar(toolbar);

        places = getPlaces(musicId, zoneId);
        RecyclerView resultsListView = (RecyclerView) findViewById(R.id.results_list_view);
        resultsListView.setLayoutManager(new LinearLayoutManager(this));
        if (places.size() > 0) {
            resultsListView.setAdapter(new ResultsAdapter(context, R.layout.item_place, places));
            ItemClickSupport.addTo(resultsListView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                @Override
                public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                    Place place = places.get(position);
                    if (place != null) {
                        startActivity(new Intent(ResultsActivity.this, PortalActivity.class).putExtra("place", place));
                    }
                }
            });
        } else {
            findViewById(R.id.not_found_view).setVisibility(View.VISIBLE);
            resultsListView.setVisibility(View.GONE);
        }
    }

    public List<Place> getPlaces(int musicId, int zoneId) {
        if (musicId == 0 && zoneId != 0){
            places = database.getPlacesByZone(zoneId);
            return places;
        } else if (musicId != 0 && zoneId == 0){
            places = database.getPlacesByMusic(musicId);
            return places;
        } else {
            places = new ArrayList<>();
            return places;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
