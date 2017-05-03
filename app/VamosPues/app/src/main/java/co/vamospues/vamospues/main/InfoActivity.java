package co.vamospues.vamospues.main;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import co.vamospues.vamospues.R;
import co.vamospues.vamospues.database.DatabaseHandler;
import co.vamospues.vamospues.helpers.GillSansLightTextView;
import co.vamospues.vamospues.helpers.GillSansSemiBoldTextView;
import co.vamospues.vamospues.models.Place;

public class InfoActivity extends AppCompatActivity {

    private Context context;
    private Place place;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        context = InfoActivity.this;
        place = (Place) getIntent().getExtras().get("place");

        if (place != null) {

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationIcon(R.drawable.ic_back);
            setUpToolbar(toolbar);

            GillSansSemiBoldTextView descriptionLabel = (GillSansSemiBoldTextView) findViewById(R.id.description_label);
            descriptionLabel.setText(place.getDescription());

            LinearLayout hoursButton = (LinearLayout) findViewById(R.id.hours_button);
            hoursButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Dialog dialog = new Dialog(context, R.style.StyledDialog);
                    View view = View.inflate(context, R.layout.dialog_hours, null);
                    dialog.setContentView(view);

                    GillSansSemiBoldTextView openCloseLabel = (GillSansSemiBoldTextView) dialog.findViewById(R.id.open_close_label);
                    openCloseLabel.setText(place.getOpen() + " - " + place.getClose());

                    dialog.show();

                }
            });

            LinearLayout servicesButton = (LinearLayout) findViewById(R.id.services_button);
            LinearLayout locationButton = (LinearLayout) findViewById(R.id.location_button);
            locationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Dialog dialog = new Dialog(context, R.style.StyledDialog);
                    View view = View.inflate(context, R.layout.dialog_map, null);
                    dialog.setContentView(view);

                    GillSansSemiBoldTextView addressLabel = (GillSansSemiBoldTextView) dialog.findViewById(R.id.address_label);
                    addressLabel.setText(place.getAddress());

                    LinearLayout mapButton = (LinearLayout) dialog.findViewById(R.id.map_button);
                    mapButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(InfoActivity.this, DirectionsActivity.class).putExtra("place", place));
                        }
                    });

                    dialog.show();
                    
                }
            });

            LinearLayout musicButton = (LinearLayout) findViewById(R.id.music_button);
            musicButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Dialog dialog = new Dialog(context, R.style.StyledDialog);
                    View view = View.inflate(context, R.layout.dialog_music, null);
                    dialog.setContentView(view);

                    GillSansSemiBoldTextView musicLabel = (GillSansSemiBoldTextView) dialog.findViewById(R.id.music_label);
                    musicLabel.setText(place.getMusic().getName());

                    dialog.show();

                }
            });

        } else {
            finish();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpToolbar(Toolbar toolbar){
        if (toolbar != null){
            GillSansLightTextView placeLabel = (GillSansLightTextView) findViewById(R.id.place_label);
            ImageView placeImage = (ImageView) findViewById(R.id.place_image);
            Picasso.with(context).load(place.getImageUrl()).into(placeImage);
            placeLabel.setText(place.getName());
        }
    }
}
