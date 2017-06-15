package co.vamospues.vamospues.main;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import co.vamospues.vamospues.R;
import co.vamospues.vamospues.adapters.PlacePackagesAdapter;
import co.vamospues.vamospues.database.DatabaseHandler;
import co.vamospues.vamospues.enums.Services;
import co.vamospues.vamospues.helpers.Queue;
import co.vamospues.vamospues.helpers.Utils;
import co.vamospues.vamospues.models.Package;
import co.vamospues.vamospues.models.Place;
import co.vamospues.vamospues.models.PlacePackage;

public class PackagesActivity extends AppCompatActivity {

    private Place place;
    private List<PlacePackage> placePackages;
    private Context context;
    private GridView packagesGrid;
    private DatabaseHandler database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_packages);

        context = PackagesActivity.this;
        database = new DatabaseHandler(context);
        place = (Place) getIntent().getExtras().get("place");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        setSupportActionBar(toolbar);

        FrameLayout buildView = (FrameLayout) findViewById(R.id.build_view);
        buildView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PackagesActivity.this, CreatePackageActivity.class).putExtra("place", place));
            }
        });

        packagesGrid = (GridView) findViewById(R.id.packages_grid);
        packagesGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PlacePackage placePackage = placePackages.get(position);
                startActivity(new Intent(PackagesActivity.this, PackageDetailActivity.class).putExtra("place_package", placePackage));
            }
        });

        setPackagesGrid();
    }

    private String getPackagesUrl(){
        return Services.BASE_URL + "place/" + place.getId() + "/packages?token=" + database.getToken();
    }

    private void setPackagesGrid(){

        final Dialog dialog = Utils.getAlertDialog(context);
        dialog.show();
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                finish();
            }
        });


        StringRequest request = new StringRequest(Request.Method.GET, getPackagesUrl(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try{
                    JSONObject responseObject = new JSONObject(response);
                    boolean success = responseObject.getBoolean("success");
                    if (success){
                        JSONArray responsePackages = responseObject.getJSONArray("packages");
                        if (responsePackages.length() > 0){
                            placePackages = new ArrayList<>();
                            for (int i = 0; i < responsePackages.length(); i++) {
                                JSONObject packageObject = responsePackages.getJSONObject(i);

                                Package aPackage = new Package();
                                aPackage.setId(packageObject.getInt("package_id"));
                                aPackage.setImgUrl(packageObject.getString("img_url"));
                                aPackage.setName(packageObject.getString("name"));

                                PlacePackage placePackage = new PlacePackage();
                                placePackage.setId(packageObject.getInt("id"));
                                placePackage.setPlace(place);
                                placePackage.setaPackage(aPackage);
                                placePackage.setDiscount(packageObject.getDouble("discount"));
                                placePackage.setPrice(packageObject.getDouble("price"));
                                
                                placePackages.add(placePackage);
                            }
                            setGridView();
                        }
                    } else {
                        Snackbar.make(findViewById(R.id.activity_packages), "Error obteniendo los paquetes", Snackbar.LENGTH_LONG)
                                .setAction("Reintentar", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        setPackagesGrid();
                                    }
                                }).show();
                    }
                } catch(Exception e){
                    Snackbar.make(findViewById(R.id.activity_packages), "Error obteniendo los paquetes", Snackbar.LENGTH_LONG)
                            .setAction("Reintentar", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    setPackagesGrid();
                                }
                            }).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Snackbar.make(findViewById(R.id.activity_packages), "Error en el servidor, intenta luego", Snackbar.LENGTH_LONG);
            }
        });
        Queue.getInstance(context).addToRequestQueue(request);
    }

    private void setGridView() {
        packagesGrid.setAdapter(new PlacePackagesAdapter(context, R.layout.item_place_package, placePackages));
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
