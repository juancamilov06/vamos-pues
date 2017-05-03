package co.vamospues.vamospues.main;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.Profile;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import co.vamospues.vamospues.R;
import co.vamospues.vamospues.adapters.FavoritePlacesAdapter;
import co.vamospues.vamospues.database.DatabaseHandler;
import co.vamospues.vamospues.enums.Services;
import co.vamospues.vamospues.helpers.GillSansLightTextView;
import co.vamospues.vamospues.helpers.GillSansSemiBoldTextView;
import co.vamospues.vamospues.helpers.ItemClickSupport;
import co.vamospues.vamospues.helpers.Queue;
import co.vamospues.vamospues.helpers.Utils;
import co.vamospues.vamospues.models.Place;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener{

    private Context context;
    private int packageOrders = 0;
    private int itemOrders = 0;
    private DatabaseHandler database;
    private List<Place> favorites;
    private RecyclerView favoritesListView;
    private String phone = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        context = ProfileActivity.this;
        database = new DatabaseHandler(context);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        Profile profile = Profile.getCurrentProfile();

        favoritesListView = (RecyclerView) findViewById(R.id.favorites_list_view);
        favoritesListView.setLayoutManager(new LinearLayoutManager(this));
        ItemClickSupport.addTo(favoritesListView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Place place = favorites.get(position);
                startActivity(new Intent(ProfileActivity.this, PortalActivity.class).putExtra("place", place));
            }
        });

        GillSansLightTextView mailLabel = (GillSansLightTextView) findViewById(R.id.mail_label);
        mailLabel.setText(database.getCurrentMail());

        CircularImageView profileImage = (CircularImageView) findViewById(R.id.profile_image);
        GillSansSemiBoldTextView lastNameLabel = (GillSansSemiBoldTextView) findViewById(R.id.last_name_label);
        lastNameLabel.setText(profile.getLastName().toUpperCase());

        GillSansLightTextView firstNameLabel = (GillSansLightTextView) findViewById(R.id.first_name_label);
        firstNameLabel.setText(profile.getFirstName().toUpperCase());
        Picasso.with(context).load(profile.getProfilePictureUri(200, 200)).into(profileImage);

        FloatingActionButton phoneFab = (FloatingActionButton) findViewById(R.id.phone_fab);
        phoneFab.setOnClickListener(this);

        getUserInfo();
    }

    private void getUserInfo() {
        final Dialog dialog = Utils.getAlertDialog(context);
        dialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, Services.BASE_URL + Services.PROFILE_BASIC_INFO_SERVICE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try{
                    JSONObject responseObject = new JSONObject(response);
                    boolean success = responseObject.getBoolean("success");
                    if (success){
                        JSONObject dataObject = responseObject.getJSONObject("data");
                        itemOrders = dataObject.getInt("item_order_count");
                        packageOrders = dataObject.getInt("package_order_count");
                        phone = dataObject.getJSONObject("user").getString("phone");

                        getFavorites();

                    } else {
                        Utils.showSnackbar("Error obteniendo el perfil", ProfileActivity.this, R.id.activity_profile);
                    }
                } catch (Exception e){
                    dialog.dismiss();
                    Utils.showSnackbar("Error obteniendo el perfil", ProfileActivity.this, R.id.activity_profile);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Utils.showSnackbar("Error obteniendo el perfil", ProfileActivity.this, R.id.activity_profile);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("mail", database.getCurrentMail());
                return params;
            }
        };
        Queue.getInstance(context).addToRequestQueue(request);
    }

    private void getFavorites() {
        favorites = database.getFavorites();
        favoritesListView.setAdapter(new FavoritePlacesAdapter(context, R.layout.item_favorite_place, favorites));
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.phone_fab:

                final Dialog dialog = new Dialog(context, R.style.StyledDialog);
                View dialogView = View.inflate(context, R.layout.dialog_phone_change, null);
                dialog.setContentView(dialogView);

                final TextInputEditText phoneInput = (TextInputEditText) dialogView.findViewById(R.id.phone_input);

                Button finishButton = (Button) dialogView.findViewById(R.id.finish_button);
                finishButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();

                        final String phone = phoneInput.getText().toString();
                        if (TextUtils.isEmpty(phone)){
                            Utils.showSnackbar("El telefono es invalido", ProfileActivity.this, R.id.activity_profile);
                            return;
                        }

                        if (phone.equals(ProfileActivity.this.phone)){
                            Utils.showSnackbar("Si quieres actualizar tu numero, ingresa uno diferente", ProfileActivity.this, R.id.activity_profile);
                            return;
                        }

                        final Dialog loadingDialog = Utils.getAlertDialog(context);
                        loadingDialog.show();
                        StringRequest request = new StringRequest(Request.Method.POST, Services.BASE_URL + Services.CHANGE_PHONE_SERVICE, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                loadingDialog.dismiss();
                                try{
                                    JSONObject responseObject = new JSONObject(response);
                                    boolean success = responseObject.getBoolean("success");
                                    if (success){
                                        Utils.showSnackbar("Telefono cambiado con exito", ProfileActivity.this, R.id.activity_profile);
                                        getUserInfo();
                                    } else {
                                        Utils.showSnackbar("Error cambiando el telefono", ProfileActivity.this, R.id.activity_profile);
                                    }
                                } catch (Exception e){
                                    Utils.showSnackbar("Error cambiando el telefono, intenta luego", ProfileActivity.this, R.id.activity_profile);
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                loadingDialog.dismiss();
                                Utils.showSnackbar("Error interno del servidor", ProfileActivity.this, R.id.activity_profile);
                            }
                        }){
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<>();
                                params.put("mail", database.getCurrentMail());
                                params.put("phone", phone);
                                return params;
                            }
                        };
                        Queue.getInstance(context).addToRequestQueue(request);
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
                break;
            case R.id.favorites_fab:



                break;
            default:
                break;
        }
    }
}
