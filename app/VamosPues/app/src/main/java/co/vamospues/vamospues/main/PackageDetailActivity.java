package co.vamospues.vamospues.main;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import co.vamospues.vamospues.R;
import co.vamospues.vamospues.adapters.PackageItemsAdapter;
import co.vamospues.vamospues.enums.Services;
import co.vamospues.vamospues.helpers.GillSansLightTextView;
import co.vamospues.vamospues.helpers.Queue;
import co.vamospues.vamospues.helpers.Utils;
import co.vamospues.vamospues.models.PackageItem;
import co.vamospues.vamospues.models.Place;
import co.vamospues.vamospues.models.PlacePackage;

public class PackageDetailActivity extends AppCompatActivity {

    private Context context;
    private PlacePackage placePackage;
    private RecyclerView packageContentListView;
    private List<PackageItem> packageItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_detail);

        context = PackageDetailActivity.this;
        placePackage = (PlacePackage) getIntent().getExtras().get("place_package");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_back);

        packageContentListView = (RecyclerView) findViewById(R.id.package_content_list);
        packageContentListView.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        setUpToolbar(toolbar, placePackage.getPlace());
        setUpPackageItems();
    }

    private void setUpPackageItems() {

        final Dialog dialog = Utils.getAlertDialog(context);
        dialog.show();

        final String PLACE_PACKAGE_CONTENT_SERVICE = "/package/" + placePackage.getId();
        StringRequest request = new StringRequest(Request.Method.GET, Services.BASE_URL + PLACE_PACKAGE_CONTENT_SERVICE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try{
                    JSONObject responseObject = new JSONObject(response);
                    boolean success = responseObject.getBoolean("success");
                    if (success){
                        JSONArray responseItems = responseObject.getJSONObject("data").getJSONArray("items");
                        if (responseItems.length() > 0){
                            packageItems = new ArrayList<>();
                            for (int i = 0; i < responseItems.length(); i++) {

                                JSONObject itemObject = responseItems.getJSONObject(i);

                                PackageItem item = new PackageItem();
                                item.setId(itemObject.getInt("id"));
                                item.setName(itemObject.getString("name"));
                                item.setPrice(itemObject.getDouble("price"));
                                item.setTotal(itemObject.getDouble("total"));
                                item.setQuantity(itemObject.getInt("quantity"));
                                item.setDiscount(placePackage.getDiscount());

                                packageItems.add(item);
                            }
                            setItemsListView();
                        }
                    }
                } catch (Exception e){
                    Utils.showSnackbar("Error obteniendo los productos, intenta de nuevo", PackageDetailActivity.this, R.id.activity_package_detail);
                    dialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utils.showSnackbar("Error obteniendo los productos, intenta de nuevo", PackageDetailActivity.this, R.id.activity_package_detail);
                dialog.dismiss();
            }
        });
        Queue.getInstance(context).addToRequestQueue(request);
    }

    private void setItemsListView(){
        PackageItemsAdapter adapter = new PackageItemsAdapter(R.layout.item_package_item, packageItems);
        packageContentListView.setAdapter(adapter);
    }

    private void setUpToolbar(Toolbar toolbar, Place place){
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
