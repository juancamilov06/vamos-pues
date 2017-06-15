package co.vamospues.vamospues.main;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Toast;

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
import co.vamospues.vamospues.adapters.ReviewAdapter;
import co.vamospues.vamospues.database.DatabaseHandler;
import co.vamospues.vamospues.enums.Codes;
import co.vamospues.vamospues.enums.Services;
import co.vamospues.vamospues.helpers.GillSansLightTextView;
import co.vamospues.vamospues.helpers.Queue;
import co.vamospues.vamospues.helpers.Utils;
import co.vamospues.vamospues.models.Place;
import co.vamospues.vamospues.models.Review;

public class ReviewActivity extends AppCompatActivity {

    private Context context;
    private RecyclerView reviewsListView;
    private Place place;
    private DatabaseHandler database;
    private List<Review> reviews;
    private boolean loaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_back);

        place = (Place) getIntent().getExtras().get("place");

        if (place != null) {

            context = ReviewActivity.this;
            database = new DatabaseHandler(context);
            reviewsListView = (RecyclerView) findViewById(R.id.reviews_list);
            reviewsListView.setLayoutManager(new LinearLayoutManager(this));

            setUpToolbar(toolbar);

            GillSansLightTextView placeLabel = (GillSansLightTextView) findViewById(R.id.place_label);
            placeLabel.setText(place.getName());

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    final Dialog dialog = new Dialog(context, R.style.StyledDialog);
                    View dialogView = View.inflate(context, R.layout.dialog_create_review, null);

                    dialog.setContentView(dialogView);

                    final RatingBar ratingBar = (RatingBar) dialog.findViewById(R.id.rating_bar);
                    final TextInputEditText reviewInput = (TextInputEditText) dialog.findViewById(R.id.review_input);

                    Button sendReviewButton = (Button) dialog.findViewById(R.id.send_button);
                    sendReviewButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            double rating = ratingBar.getRating();
                            String content = reviewInput.getText().toString();

                            if (TextUtils.isEmpty(content)){
                                reviewInput.setError("Debes llenar este campo");
                                return;
                            }

                            dialog.dismiss();

                            final Review review = new Review();
                            review.setPlace(place);
                            review.setContent(content);
                            review.setUsername(database.getCurrentMail());
                            review.setRating(rating);

                            StringRequest request = new StringRequest(Request.Method.POST, getPostReviewUrl(), new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    System.out.println(response);
                                    try {
                                        JSONObject responseObject = new JSONObject(response);
                                        int code = responseObject.getInt("code");
                                        if (code == Codes.CODE_SUCCESSFUL){
                                            Utils.showSnackbar("Reseña enviada con exito", ReviewActivity.this, R.id.activity_review);
                                            setUpList();
                                        } else {
                                            Utils.showSnackbar("Error enviando reseña, intenta luego", ReviewActivity.this, R.id.activity_review);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Utils.showSnackbar("Error enviando reseña, intenta luego", ReviewActivity.this, R.id.activity_review);
                                    }

                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    error.printStackTrace();
                                    Utils.showSnackbar("Error enviando reseña, intenta luego", ReviewActivity.this, R.id.activity_review);
                                }
                            }){
                                @Override
                                public Priority getPriority() {
                                    return Priority.IMMEDIATE;
                                }

                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String, String> params = new HashMap<>();
                                    JSONObject converted = reviewToJSON(review);
                                    if (converted != null) {
                                        params.put("review", converted.toString());
                                        return params;
                                    }
                                    return null;
                                }
                            };
                            Queue.getInstance(context).addToRequestQueue(request);
                        }
                    });
                    dialog.show();
                }
            });
            setUpList();
        } else {
            finish();
        }

    }

    private JSONObject reviewToJSON(Review review){
        try {
            JSONObject object = new JSONObject();
            object.put("content", review.getContent());
            object.put("username", review.getUsername());
            object.put("place_id", review.getPlace().getId());
            object.put("rating", review.getRating());
            return object;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private void setUpToolbar(Toolbar toolbar){
        if (toolbar != null){
            GillSansLightTextView placeLabel = (GillSansLightTextView) findViewById(R.id.place_label);
            ImageView placeImage = (ImageView) findViewById(R.id.place_image);
            Picasso.with(context).load(place.getImageUrl()).into(placeImage);
            placeLabel.setText(place.getName());
        }
    }

    private void setUpListViews() {
        reviewsListView.setAdapter(new ReviewAdapter(R.layout.item_review, reviews));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private String getPostReviewUrl(){
        return Services.BASE_URL + Services.REVIEW_ADD_SERVICE + "?token=" + database.getToken();
    }

    private String getReviewsUrl(){
        return Services.BASE_URL + "place/" + place.getId() + "/reviews?token=" + database.getToken();
    }

    private void setUpList(){

        final Dialog dialog = Utils.getAlertDialog(context);
        dialog.show();
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                finish();
            }
        });

        StringRequest request = new StringRequest(Request.Method.GET, getReviewsUrl(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                loaded = true;
                System.out.println("Response: " + response);
                try {
                    JSONObject reviewObject = new JSONObject(response);
                    int code = reviewObject.getInt("code");
                    if (code == Codes.CODE_SUCCESSFUL){
                        JSONArray reviewResponse = reviewObject.getJSONObject("data").getJSONArray("reviews");
                        if (reviewResponse.length() > 0){
                            reviews = new ArrayList<>();
                            for (int i = 0; i < reviewResponse.length(); i++) {

                                JSONObject placeItemObject = reviewResponse.getJSONObject(i);

                                Review review = new Review();
                                review.setId(placeItemObject.getInt("id"));
                                review.setContent(placeItemObject.getString("content"));
                                review.setCreated(placeItemObject.getString("created"));
                                review.setRating(placeItemObject.getDouble("rating"));
                                review.setUsername(placeItemObject.getString("username"));
                                review.setPlace(place);

                                reviews.add(review);

                            }
                            if (reviews.size() > 0) {
                                setUpListViews();
                            } else {
                                showMessageView();
                            }
                        } else {
                            showMessageView();
                        }
                    }
                } catch (JSONException e){
                    Utils.showSnackbar("Error obteniendo los datos", ReviewActivity.this, R.id.activity_review);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Utils.showSnackbar("Error obteniendo los datos", ReviewActivity.this, R.id.activity_review);
            }
        }){
            @Override
            public Priority getPriority() {
                return Priority.IMMEDIATE;
            }
        };
        Queue.getInstance(context).addToRequestQueue(request);
    }

    public void showMessageView(){
        findViewById(R.id.not_found_view).setVisibility(View.VISIBLE);
        reviewsListView.setVisibility(View.GONE);
    }
}
