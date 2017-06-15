package co.vamospues.vamospues.main;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import co.vamospues.vamospues.R;
import co.vamospues.vamospues.adapters.EventsAdapter;
import co.vamospues.vamospues.adapters.EventsPagerAdapter;
import co.vamospues.vamospues.database.DatabaseHandler;
import co.vamospues.vamospues.enums.Codes;
import co.vamospues.vamospues.enums.Services;
import co.vamospues.vamospues.helpers.ItemClickSupport;
import co.vamospues.vamospues.helpers.Queue;
import co.vamospues.vamospues.helpers.Utils;
import co.vamospues.vamospues.models.Event;
import me.relex.circleindicator.CircleIndicator;

public class EventActivity extends AppCompatActivity {

    private RecyclerView eventsRecyclerView;
    private EventActivity context;
    private List<Event> events, starredEvents;
    private DatabaseHandler database;
    private ViewPager starredPager;
    private CircleIndicator starredIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        context = EventActivity.this;
        database = new DatabaseHandler(context);

        eventsRecyclerView = (RecyclerView) findViewById(R.id.events_recycler_view);
        ItemClickSupport.addTo(eventsRecyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Event event = events.get(position);
                startActivity(new Intent(EventActivity.this, EventDetailActivity.class).putExtra("event", event));
            }
        });
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        eventsRecyclerView.setLayoutManager(manager);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        setSupportActionBar(toolbar);

        starredPager = (ViewPager) findViewById(R.id.starred_viewpager);
        starredIndicator = (CircleIndicator) findViewById(R.id.starred_indicator);

        getData();
    }

    private String getEventsUrl(){
        return Services.BASE_URL + Services.EVENT_SERVICE + "?token=" + database.getToken();
    }

    private void getData(){
        final Dialog dialog = Utils.getAlertDialog(context);
        dialog.show();
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                finish();
            }
        });

        StringRequest request = new StringRequest(Request.Method.GET, getEventsUrl(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                System.out.println("Response; " + response);
                try {
                    JSONObject eventResponse = new JSONObject(response);
                    int code = eventResponse.getInt("code");
                    if (code == Codes.CODE_SUCCESSFUL) {
                        JSONArray eventsResponse = eventResponse.getJSONObject("data").getJSONArray("events");
                        if (eventsResponse.length() > 0) {
                            events = new ArrayList<>();
                            starredEvents = new ArrayList<>();
                            for (int i = 0; i < eventsResponse.length(); i++) {
                                JSONObject currentEvent = eventsResponse.getJSONObject(i);
                                Event event = new Event();
                                event.setId(currentEvent.getInt("id"));
                                event.setContact(currentEvent.getString("contact"));
                                event.setDate(currentEvent.getString("date"));
                                event.setName(currentEvent.getString("name"));
                                event.setImageUrl(currentEvent.getString("img_url"));
                                event.setStarred(currentEvent.getInt("is_starred") > 0);
                                event.setActive(currentEvent.getInt("is_active") > 0);
                                event.setPlace(database.getPlace(currentEvent.getInt("place_id")));
                                event.setStart(currentEvent.getString("start"));
                                event.setDescription(currentEvent.getString("description"));

                                if (event.isStarred()) {
                                    starredEvents.add(event);
                                } else {
                                    events.add(event);
                                }
                            }
                            setUpList();
                        } else {
                            findViewById(R.id.pager_view).setVisibility(View.GONE);
                            findViewById(R.id.starred_not_found_view).findViewById(View.VISIBLE);
                            eventsRecyclerView.setVisibility(View.GONE);
                            findViewById(R.id.events_not_found_view).setVisibility(View.VISIBLE);
                        }
                    } else if (code == Codes.CODE_UNAUTHORIZED){
                        Dialog dialog = Utils.getExpiredDialog(EventActivity.this);
                        dialog.show();
                    } else {
                        Snackbar.make(findViewById(R.id.activity_event), "Error obteniendo los eventos", Snackbar.LENGTH_LONG)
                                .setAction("Reintentar", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        getData();
                                    }
                                }).show();
                    }
                } catch (JSONException e) {
                    Snackbar.make(findViewById(R.id.activity_event), "Error obteniendo los eventos", Snackbar.LENGTH_LONG)
                            .setAction("Reintentar", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    getData();
                                }
                            }).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Snackbar.make(findViewById(R.id.activity_event), "Error obteniendo los eventos", Snackbar.LENGTH_LONG)
                        .setAction("Reintentar", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getData();
                            }
                        }).show();
                error.printStackTrace();
                dialog.dismiss();
            }
        });
        Queue.getInstance(context).addToRequestQueue(request);

    }

    private void setUpList() {
        if (events.size() > 0){
            EventsAdapter adapter = new EventsAdapter(context, events, R.layout.item_event);
            eventsRecyclerView.setAdapter(adapter);
        } else {
            eventsRecyclerView.setVisibility(View.GONE);
            findViewById(R.id.events_not_found_view).setVisibility(View.VISIBLE);
        }
        if (starredEvents.size() > 0) {
            EventsPagerAdapter pagerAdapter = new EventsPagerAdapter(context, R.layout.item_starred_event, starredEvents);
            starredPager.setAdapter(pagerAdapter);
            starredIndicator.setViewPager(starredPager);
        } else {
            findViewById(R.id.pager_view).setVisibility(View.GONE);
            findViewById(R.id.starred_not_found_view).setVisibility(View.VISIBLE);
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
