package co.vamospues.vamospues.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import co.vamospues.vamospues.R;
import co.vamospues.vamospues.adapters.ResultsAdapter;
import co.vamospues.vamospues.database.DatabaseHandler;
import co.vamospues.vamospues.helpers.ItemClickSupport;
import co.vamospues.vamospues.models.Place;

public class BudgetResultsActivity extends AppCompatActivity {

    private List<Place> places;
    private DatabaseHandler database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        double budget = getIntent().getDoubleExtra("budget", 0);

        Context context = BudgetResultsActivity.this;
        database = new DatabaseHandler(context);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        setSupportActionBar(toolbar);

        places = getPlaces(budget);
        RecyclerView resultsListView = (RecyclerView) findViewById(R.id.results_list_view);
        resultsListView.setLayoutManager(new LinearLayoutManager(this));

        if (places.size() > 0) {
            resultsListView.setAdapter(new ResultsAdapter(context, R.layout.item_place, places));
            ItemClickSupport.addTo(resultsListView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                @Override
                public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                    Place place = places.get(position);
                    if (place != null) {
                        startActivity(new Intent(BudgetResultsActivity.this, PortalActivity.class).putExtra("place", place));
                    }
                }
            });
        } else {
            findViewById(R.id.not_found_view).setVisibility(View.VISIBLE);
            resultsListView.setVisibility(View.GONE);
        }
    }

    public List<Place> getPlaces(double budget) {
        places = database.getPlacesByBudget(budget);
        return places;
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
