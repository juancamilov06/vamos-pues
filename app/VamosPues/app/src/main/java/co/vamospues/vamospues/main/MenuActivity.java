package co.vamospues.vamospues.main;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import co.vamospues.vamospues.R;
import co.vamospues.vamospues.adapters.MenuAdapter;
import co.vamospues.vamospues.database.DatabaseHandler;
import co.vamospues.vamospues.enums.Codes;
import co.vamospues.vamospues.enums.ItemTypes;
import co.vamospues.vamospues.enums.Services;
import co.vamospues.vamospues.helpers.GillSansLightTextView;
import co.vamospues.vamospues.helpers.Queue;
import co.vamospues.vamospues.helpers.Utils;
import co.vamospues.vamospues.models.Item;
import co.vamospues.vamospues.models.Place;
import co.vamospues.vamospues.models.PlaceItem;

public class MenuActivity extends AppCompatActivity {

    private Place place;
    private Context context;
    private RecyclerView internationalListView, cocktailsListView, nationalListView;
    private Dialog dialog;
    private boolean loaded = false;
    private DatabaseHandler database;
    private NestedScrollView menuContentScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        place = (Place) getIntent().getExtras().get("place");
        context = MenuActivity.this;
        dialog = Utils.getAlertDialog(context);
        database = new DatabaseHandler(context);

        menuContentScrollView = (NestedScrollView) findViewById(R.id.menu_content_scroll);

        cocktailsListView = (RecyclerView) findViewById(R.id.cocktails_list_view);
        cocktailsListView.setLayoutManager(new LinearLayoutManager(this));

        FrameLayout cocktailsHeader = (FrameLayout) findViewById(R.id.cocktails_header);
        cocktailsHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int visible = cocktailsListView.getVisibility();
                if (visible == View.GONE){
                    cocktailsListView.setVisibility(View.VISIBLE);
                    menuContentScrollView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            menuContentScrollView.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    },200);
                } else {
                    cocktailsListView.setVisibility(View.GONE);
                }
            }
        });

        nationalListView = (RecyclerView) findViewById(R.id.national_list_view);
        nationalListView.setLayoutManager(new LinearLayoutManager(this));

        FrameLayout nationalHeader = (FrameLayout) findViewById(R.id.national_header);
        nationalHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int visible = nationalListView.getVisibility();
                if (visible == View.GONE){
                    nationalListView.setVisibility(View.VISIBLE);
                    menuContentScrollView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            menuContentScrollView.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    },200);
                } else {
                    nationalListView.setVisibility(View.GONE);
                }
            }
        });

        internationalListView = (RecyclerView) findViewById(R.id.international_list_view);
        internationalListView.setLayoutManager(new LinearLayoutManager(this));

        FrameLayout internationalHeader = (FrameLayout) findViewById(R.id.international_header);
        internationalHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int visible = internationalListView.getVisibility();
                if (visible == View.GONE){
                    internationalListView.setVisibility(View.VISIBLE);
                    menuContentScrollView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            menuContentScrollView.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    },200);
                } else {
                    internationalListView.setVisibility(View.GONE);
                }
            }
        });

        if (place != null) {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationIcon(R.drawable.ic_back);
            setUpToolbar(toolbar);

            setListsUp();
        } else {
            finish();
        }
    }

    private void setListsUp(){

        dialog.show();
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (!loaded){
                    finish();
                }
            }
        });

        final String MENU_SERVICE = "place/" + place.getId() + "/menu";

        StringRequest request = new StringRequest(Request.Method.GET, Services.BASE_URL + MENU_SERVICE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                System.out.println("Response: " + response);
                try {
                    JSONObject menuObject = new JSONObject(response);
                    int code = menuObject.getInt("code");
                    if (code == Codes.CODE_SUCCESSFUL){
                        JSONArray menuResponse = menuObject.getJSONObject("data").getJSONArray("menu");
                        if (menuResponse.length() > 0){
                            List<PlaceItem> placeItems = new ArrayList<>();
                            for (int i = 0; i < menuResponse.length(); i++) {
                                JSONObject placeItemObject = menuResponse.getJSONObject(i);

                                Item item = new Item();
                                item.setName(placeItemObject.getString("name"));
                                item.setItemType(database.getItemType(placeItemObject.getInt("type_id")));

                                PlaceItem placeItem = new PlaceItem();
                                placeItem.setPlace(place);
                                placeItem.setItem(item);
                                placeItem.setDiscount(placeItemObject.getDouble("discount"));
                                placeItem.setPrice(placeItemObject.getDouble("price"));

                                placeItems.add(placeItem);
                            }
                            if (placeItems.size() > 0) {
                                setUpListViews(placeItems);
                            }
                        }
                    }
                } catch (JSONException e){
                    dialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("place_id", String.valueOf(place.getId()));
                return params;
            }
        };
        Queue.getInstance(context).addToRequestQueue(request);
    }

    private void setUpListViews(List<PlaceItem> placeItems){

        List<PlaceItem> cocktails = new ArrayList<>();
        List<PlaceItem> nationalDrinks = new ArrayList<>();
        List<PlaceItem> internationalDrinks = new ArrayList<>();

        for (PlaceItem placeItem : placeItems){
            if (placeItem.getItem().getItemType().getId() == ItemTypes.COCKTAIL){
                cocktails.add(placeItem);
            } else if (placeItem.getItem().getItemType().getId() == ItemTypes.NATIONAL){
                nationalDrinks.add(placeItem);
            } else {
                internationalDrinks.add(placeItem);
            }
        }

        if (cocktails.size() > 0){
            cocktailsListView.setAdapter(new MenuAdapter(R.layout.item_menu, cocktails));
        } else {
            showCocktailsNotFoundView();
        }

        if (nationalDrinks.size() > 0){
            nationalListView.setAdapter(new MenuAdapter(R.layout.item_menu, nationalDrinks));
        } else {
            showNationalNotFoundView();
        }

        if (internationalDrinks.size() > 0){
            internationalListView.setAdapter(new MenuAdapter(R.layout.item_menu, internationalDrinks));
        } else {
            showInternationalNotFoundView();
        }
    }

    private void showCocktailsNotFoundView(){
        findViewById(R.id.cocktails_not_found_view).setVisibility(View.VISIBLE);
        cocktailsListView.setVisibility(View.GONE);
    }

    private void showNationalNotFoundView(){
        findViewById(R.id.national_not_found_view).setVisibility(View.VISIBLE);
        nationalListView.setVisibility(View.GONE);
    }

    private void showInternationalNotFoundView(){
        findViewById(R.id.international_not_found_view).setVisibility(View.VISIBLE);
        internationalListView.setVisibility(View.GONE);
    }

    private void setUpToolbar(Toolbar toolbar){
        if (toolbar != null){
            GillSansLightTextView placeLabel = (GillSansLightTextView) findViewById(R.id.place_label);
            ImageView placeImage = (ImageView) findViewById(R.id.place_image);
            Picasso.with(context).load(place.getImageUrl()).into(placeImage);
            placeLabel.setText(place.getName());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
