package co.vamospues.vamospues.main;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import co.vamospues.vamospues.R;
import co.vamospues.vamospues.adapters.TicketsAdapter;
import co.vamospues.vamospues.enums.Services;
import co.vamospues.vamospues.helpers.BlurTransformation;
import co.vamospues.vamospues.helpers.GillSansLightTextView;
import co.vamospues.vamospues.helpers.GillSansSemiBoldTextView;
import co.vamospues.vamospues.helpers.ItemClickSupport;
import co.vamospues.vamospues.helpers.Queue;
import co.vamospues.vamospues.helpers.Utils;
import co.vamospues.vamospues.models.Event;
import co.vamospues.vamospues.models.Ticket;
import jp.wasabeef.picasso.transformations.GrayscaleTransformation;
import jp.wasabeef.picasso.transformations.gpu.VignetteFilterTransformation;

public class EventDetailActivity extends AppCompatActivity {

    private Context context;
    private Event event;
    private List<Ticket> tickets = new ArrayList<>();
    private RecyclerView ticketsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        context = EventDetailActivity.this;
        event = (Event) getIntent().getExtras().get("event");

        ticketsListView = (RecyclerView) findViewById(R.id.tickets_list_view);
        ItemClickSupport.addTo(ticketsListView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {

            }
        });
        ticketsListView.setLayoutManager(new LinearLayoutManager(this));

        GillSansSemiBoldTextView dateLabel = (GillSansSemiBoldTextView) findViewById(R.id.date_label);
        dateLabel.setText(event.getDate().substring(0,10));
        GillSansLightTextView startLabel = (GillSansLightTextView) findViewById(R.id.start_label);
        startLabel.setText(event.getStart());
        GillSansSemiBoldTextView contactLabel = (GillSansSemiBoldTextView) findViewById(R.id.contact_label);
        contactLabel.setText(event.getContact());
        GillSansLightTextView infoLabel = (GillSansLightTextView) findViewById(R.id.info_label);
        infoLabel.setText(event.getDescription());
        GillSansSemiBoldTextView nameLabel = (GillSansSemiBoldTextView) findViewById(R.id.name_label);
        nameLabel.setText(event.getPlace().getName());
        GillSansLightTextView addressLabel = (GillSansLightTextView) findViewById(R.id.address_label);
        addressLabel.setText(event.getPlace().getAddress());

        LinearLayout locationButton = (LinearLayout) findViewById(R.id.location_button);
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(event.getName());
        toolbar.setNavigationIcon(R.drawable.ic_back);
        setSupportActionBar(toolbar);

        setToolbarHeader();
        setUpTicketsList();
    }

    private void setUpTicketsList(){
        final String GET_EVENTS_TICKET_SERVICE = "events/" + event.getId() + "/tickets";
        StringRequest request = new StringRequest(Request.Method.GET, Services.BASE_URL + GET_EVENTS_TICKET_SERVICE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject responseObject = new JSONObject(response);
                    boolean success = responseObject.getBoolean("success");
                    if (success){
                        JSONArray ticketsResponse = responseObject.getJSONObject("data").getJSONArray("tickets");
                        if (ticketsResponse.length() > 0){
                            for (int i = 0; i < ticketsResponse.length(); i++) {
                                JSONObject currentTicket = ticketsResponse.getJSONObject(i);
                                Ticket ticket = new Ticket();
                                ticket.setCapacity(currentTicket.getInt("capacity"));
                                ticket.setEvent(event);
                                ticket.setId(currentTicket.getInt("id"));
                                ticket.setLocation(currentTicket.getString("location"));
                                ticket.setPrice(currentTicket.getDouble("price"));
                                tickets.add(ticket);
                            }
                            setUpListView();
                        }
                    } else {
                        Utils.showSnackbar("No hay tiquetes ", EventDetailActivity.this, R.id.activity_event_detail);
                    }
                } catch (Exception e){
                    e.printStackTrace();
                    Utils.showSnackbar("Error ", EventDetailActivity.this, R.id.activity_event_detail);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Utils.showSnackbar("Error ", EventDetailActivity.this, R.id.activity_event_detail);
            }
        });
        Queue.getInstance(context).addToRequestQueue(request);
    }

    private void setUpListView() {
        ticketsListView.setAdapter(new TicketsAdapter(R.layout.item_ticket, tickets));
    }

    private void setToolbarHeader(){
        Picasso.with(context).load(event.getImageUrl())
                .transform(new BlurTransformation(context))
                .transform(new VignetteFilterTransformation(context))
                .into((ImageView) findViewById(R.id.event_image));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
