package co.vamospues.vamospues.main;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import co.vamospues.vamospues.R;
import co.vamospues.vamospues.adapters.ZoneAdapter;
import co.vamospues.vamospues.database.DatabaseHandler;
import co.vamospues.vamospues.helpers.ItemClickSupport;
import co.vamospues.vamospues.models.Zone;

public class ZoneActivity extends AppCompatActivity {

    private Context context;
    private List<Zone> zones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zone);

        context = ZoneActivity.this;
        DatabaseHandler database = new DatabaseHandler(context);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_back);

        RecyclerView zoneListView = (RecyclerView) findViewById(R.id.zone_list_view);
        zoneListView.setLayoutManager(new LinearLayoutManager(this));
        ItemClickSupport.addTo(zoneListView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                int zoneId = zones.get(position).getId();
                startActivity(new Intent(ZoneActivity.this, ResultsActivity.class).putExtra("zone_id", zoneId));
            }
        });

        zones = database.getZones();
        zoneListView.setAdapter(new ZoneAdapter(R.layout.item_general_list, zones));
        
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
