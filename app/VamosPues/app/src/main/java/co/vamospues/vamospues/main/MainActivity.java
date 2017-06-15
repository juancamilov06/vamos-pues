package co.vamospues.vamospues.main;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

import co.vamospues.vamospues.R;
import co.vamospues.vamospues.adapters.MusicBaseAdapter;
import co.vamospues.vamospues.adapters.ZoneBaseAdapter;
import co.vamospues.vamospues.database.DatabaseHandler;
import co.vamospues.vamospues.helpers.GillSansSemiBoldTextView;
import co.vamospues.vamospues.models.Music;
import co.vamospues.vamospues.models.Prefs;
import co.vamospues.vamospues.models.Zone;
import co.vamospues.vamospues.services.PromosService;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private Context context;
    private DatabaseHandler database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Profile profile = Profile.getCurrentProfile();
        context = MainActivity.this;
        database = new DatabaseHandler(context);

        if (profile != null){
            drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
            View headerView = navigationView.getHeaderView(0);

            ImageView closeButton = (ImageView) headerView.findViewById(R.id.close_button);
            closeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawerLayout.closeDrawers();
                }
            });

            GillSansSemiBoldTextView nameLabel = (GillSansSemiBoldTextView) headerView.findViewById(R.id.name_label);
            nameLabel.setText(profile.getName().toUpperCase());

            CircularImageView profilePic = (CircularImageView) headerView.findViewById(R.id.profile_image);
            profilePic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            Picasso.with(context).load(profile.getProfilePictureUri(100,100)).into(profilePic);

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setNavigationIcon(R.drawable.ic_menu);
            setSupportActionBar(toolbar);

            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    if(item.isChecked())
                        item.setChecked(false);
                    else
                        item.setChecked(true);

                    drawerLayout.closeDrawers();

                    switch (item.getItemId()){
                        case R.id.action_logout:

                            final Dialog dialog = new Dialog(context, R.style.StyledDialog);
                            View view = View.inflate(context, R.layout.dialog_logout, null);
                            dialog.setContentView(view);

                            Button exitButton = (Button) view.findViewById(R.id.exit_button);
                            exitButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    LoginManager.getInstance().logOut();
                                    database.deleteToken();
                                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                                    finish();
                                }
                            });

                            Button cancelButton = (Button) view.findViewById(R.id.cancel_button);
                            cancelButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });

                            dialog.show();
                            break;
                        case R.id.action_settings:
                            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                            break;
                        case R.id.action_profile:
                            startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                            break;
                        case R.id.action_promos:
                            startActivity(new Intent(MainActivity.this, PromosActivity.class));
                            break;
                        case R.id.action_map:
                            startActivity(new Intent(MainActivity.this, MapActivity.class));
                            break;
                        default:
                            break;
                    }

                    return true;
                }
            });

            LinearLayout musicButton = (LinearLayout) findViewById(R.id.music_button);
            musicButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, MusicActivity.class);
                    String name = "toolbar_transition";
                    View startView = findViewById(R.id.music_filter_image);
                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this,
                            startView,
                            name
                    );
                    ActivityCompat.startActivity(MainActivity.this, intent, options.toBundle());
                }
            });

            LinearLayout zoneButton = (LinearLayout) findViewById(R.id.zone_button);
            zoneButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, ZoneActivity.class);
                    String name = "toolbar_transition";
                    View startView = findViewById(R.id.zone_filter_image);
                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this,
                            startView,
                            name
                    );
                    ActivityCompat.startActivity(MainActivity.this, intent, options.toBundle());
                }
            });

            LinearLayout eventsButton = (LinearLayout) findViewById(R.id.events_button);
            eventsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, EventActivity.class));
                }
            });

            LinearLayout budgetButton = (LinearLayout) findViewById(R.id.budget_button);
            budgetButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, BudgetActivity.class);
                    String name = "toolbar_transition";
                    View startView = findViewById(R.id.budget_filter_image);
                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this,
                            startView,
                            name
                    );
                    ActivityCompat.startActivity(MainActivity.this, intent, options.toBundle());
                }
            });

            if (database.getPrefs() == null){
                showsPrefsDialog();
            } else {
                startService(new Intent(this, PromosService.class));
            }
        } else {
            LoginManager.getInstance().logOut();
            database.deleteToken();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }
    }

    private void showsPrefsDialog(){
        final Dialog dialog = new Dialog(context, R.style.StyledDialog);
        View dialogView = View.inflate(context, R.layout.dialog_prefs, null);
        dialog.setContentView(dialogView);

        final Spinner zoneSpinner = (Spinner) dialogView.findViewById(R.id.zone_spinner);
        final List<Zone> zones = database.getZones();
        zoneSpinner.setAdapter(new ZoneBaseAdapter(context, R.layout.item_basic, zones));

        final Spinner musicSpinner = (Spinner) dialogView.findViewById(R.id.music_spinner);
        final List<Music> musicTypes = database.getMusicTypes();
        musicSpinner.setAdapter(new MusicBaseAdapter(context, R.layout.item_basic, musicTypes));

        Button confirmButton = (Button) dialogView.findViewById(R.id.finish_button);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Prefs prefs = new Prefs();
                prefs.setZone(zones.get(zoneSpinner.getSelectedItemPosition()));
                prefs.setMusic(musicTypes.get(musicSpinner.getSelectedItemPosition()));
                prefs.setNotify(true);

                database.insertPrefs(prefs);
                startService(new Intent(MainActivity.this, PromosService.class));
                dialog.dismiss();
            }
        });

        Button cancelButton = (Button) dialogView.findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
