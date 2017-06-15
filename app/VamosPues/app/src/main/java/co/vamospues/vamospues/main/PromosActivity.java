package co.vamospues.vamospues.main;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import co.vamospues.vamospues.R;
import co.vamospues.vamospues.adapters.PromoAdapter;
import co.vamospues.vamospues.database.DatabaseHandler;
import co.vamospues.vamospues.enums.Services;
import co.vamospues.vamospues.helpers.Queue;
import co.vamospues.vamospues.helpers.Utils;
import co.vamospues.vamospues.models.Promo;

public class PromosActivity extends AppCompatActivity {

    private RecyclerView promosListView;
    private List<Promo> promos;
    private DatabaseHandler database;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promos);

        context = PromosActivity.this;
        database = new DatabaseHandler(context);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        setSupportActionBar(toolbar);

        promosListView = (RecyclerView) findViewById(R.id.promos_list_view);
        promosListView.setLayoutManager(new LinearLayoutManager(this));

        setUpList();
    }

    private void setAdapter(){
        promosListView.setAdapter(new PromoAdapter(context, promos, R.layout.item_promo));
    }

    private String getPromosUrl(){
        return Services.BASE_URL + Services.GET_PROMOS_SERVICE + "?token=" + database.getToken();
    }

    private void setUpList() {

        promos = new ArrayList<>();

        final Dialog dialog = Utils.getAlertDialog(context);
        dialog.show();
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                finish();
            }
        });

        JsonObjectRequest request = new JsonObjectRequest(getPromosUrl(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dialog.dismiss();
                try {
                    JSONArray promosResponse = response.getJSONArray("promos");
                    if (promosResponse.length() > 0 ){
                        for (int i = 0; i < promosResponse.length(); i++) {
                            JSONObject promoResponse = promosResponse.getJSONObject(i);
                            Promo promo = new Promo();
                            promo.setId(promoResponse.getInt("id"));
                            promo.setImgUrl(promoResponse.getString("img_url"));
                            promo.setName(promoResponse.getString("name"));
                            promo.setPlace(database.getPlace(promoResponse.getInt("place_id")));
                            promos.add(promo);
                        }
                        setAdapter();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
            }
        });
        Queue.getInstance(context).addToRequestQueue(request);
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
