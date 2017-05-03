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
import co.vamospues.vamospues.adapters.MusicAdapter;
import co.vamospues.vamospues.database.DatabaseHandler;
import co.vamospues.vamospues.helpers.ItemClickSupport;
import co.vamospues.vamospues.models.Music;

public class MusicActivity extends AppCompatActivity {

    private List<Music> musics;
    private Context context;
    private DatabaseHandler database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        context = MusicActivity.this;
        database = new DatabaseHandler(context);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_back);

        RecyclerView musicListView = (RecyclerView) findViewById(R.id.music_list_view);
        musicListView.setLayoutManager(new LinearLayoutManager(this));
        ItemClickSupport.addTo(musicListView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                int musicId = musics.get(position).getId();
                startActivity(new Intent(MusicActivity.this, ResultsActivity.class).putExtra("music_id", musicId));
            }
        });

        musics = database.getMusicTypes();
        musicListView.setAdapter(new MusicAdapter(R.layout.item_general_list, musics));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
