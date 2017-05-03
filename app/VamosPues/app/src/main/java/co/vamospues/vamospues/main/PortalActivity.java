package co.vamospues.vamospues.main;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import co.vamospues.vamospues.R;
import co.vamospues.vamospues.database.DatabaseHandler;
import co.vamospues.vamospues.helpers.BlurTransformation;
import co.vamospues.vamospues.helpers.ConnectivityReceiver;
import co.vamospues.vamospues.helpers.Utils;
import co.vamospues.vamospues.models.Place;

public class PortalActivity extends AppCompatActivity {

    private LinearLayout favoritesView;
    private DatabaseHandler database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portal);

        final Place place = (Place) getIntent().getExtras().get("place");

        Context context = PortalActivity.this;
        database = new DatabaseHandler(context);

        favoritesView = (LinearLayout) findViewById(R.id.favorites_view);
        favoritesView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (database.insertFavorite(place)){
                    Utils.showSnackbar("Agregado a favoritos", PortalActivity.this, R.id.activity_portal);
                    favoritesView.setVisibility(View.GONE);
                } else {
                    Utils.showSnackbar("Error agregando a favoritos", PortalActivity.this, R.id.activity_portal);
                }
            }
        });

        FrameLayout menuButton = (FrameLayout) findViewById(R.id.menu_button);
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ConnectivityReceiver.isConnected()) {
                    startActivity(new Intent(PortalActivity.this, MenuActivity.class).putExtra("place", place));
                } else {
                    Utils.showSnackbar("Activa la conexion a internet", PortalActivity.this, R.id.activity_portal);
                }
            }
        });

        FrameLayout reviewsButton = (FrameLayout) findViewById(R.id.reviews_button);
        reviewsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ConnectivityReceiver.isConnected()) {
                    startActivity(new Intent(PortalActivity.this, ReviewActivity.class).putExtra("place", place));
                } else {
                    Utils.showSnackbar("Activa la conexion a internet", PortalActivity.this, R.id.activity_portal);
                }
            }
        });

        FrameLayout infoButton = (FrameLayout) findViewById(R.id.info_button);
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ConnectivityReceiver.isConnected()) {
                    startActivity(new Intent(PortalActivity.this, InfoActivity.class).putExtra("place", place));
                } else {
                    Utils.showSnackbar("Activa la conexion a internet", PortalActivity.this, R.id.activity_portal);
                }
            }
        });

        FrameLayout packagesButton = (FrameLayout) findViewById(R.id.packages_button);
        packagesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ConnectivityReceiver.isConnected()) {
                    startActivity(new Intent(PortalActivity.this, PackagesActivity.class).putExtra("place", place));
                } else {
                    Utils.showSnackbar("Activa la conexion a internet", PortalActivity.this, R.id.activity_portal);
                }
            }
        });

        if (place != null) {

            CircularImageView placeImage = (CircularImageView) findViewById(R.id.place_image);
            Picasso.with(context).load(place.getImageUrl()).into(placeImage);
            ImageView placeBackground = (ImageView) findViewById(R.id.place_background);
            Picasso.with(context).load(place.getImageUrl()).fit().transform(new BlurTransformation(context)).into(placeBackground);

            if (checkIfPlaceIsFavorite(place)){
                favoritesView.setVisibility(View.GONE);
            }
        }
    }

    private boolean checkIfPlaceIsFavorite(Place place){
        return database.isFavorite(place.getId());
    }
}
