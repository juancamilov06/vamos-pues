package co.vamospues.vamospues.main;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import co.vamospues.vamospues.R;
import co.vamospues.vamospues.adapters.CreatePackageAdapter;
import co.vamospues.vamospues.adapters.ShoppingCartAdapter;
import co.vamospues.vamospues.database.DatabaseHandler;
import co.vamospues.vamospues.enums.Codes;
import co.vamospues.vamospues.enums.ItemTypes;
import co.vamospues.vamospues.enums.Services;
import co.vamospues.vamospues.helpers.GillSansLightTextView;
import co.vamospues.vamospues.helpers.GillSansSemiBoldTextView;
import co.vamospues.vamospues.helpers.ItemClickSupport;
import co.vamospues.vamospues.helpers.Queue;
import co.vamospues.vamospues.helpers.Utils;
import co.vamospues.vamospues.models.Item;
import co.vamospues.vamospues.models.ItemOrder;
import co.vamospues.vamospues.models.ItemOrderItem;
import co.vamospues.vamospues.models.Place;
import co.vamospues.vamospues.models.SelectablePlaceItem;

public class CreatePackageActivity extends AppCompatActivity {

    private Place place;
    private ItemOrder order;
    private List<ItemOrderItem> shoppingCart = new ArrayList<>();

    private Context context;
    private RecyclerView internationalListView, cocktailsListView, nationalListView;
    private Dialog dialog;
    private boolean loaded = false;
    private DatabaseHandler database;
    private NestedScrollView menuContentScrollView;
    private CreatePackageAdapter cocktailsAdapter, nationalAdapter, internationalAdapter;
    private List<SelectablePlaceItem> cocktails, internationalDrinks, nationalDrinks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_package);

        place = (Place) getIntent().getExtras().get("place");

        context = CreatePackageActivity.this;
        dialog = Utils.getAlertDialog(context);
        database = new DatabaseHandler(context);

        menuContentScrollView = (NestedScrollView) findViewById(R.id.menu_content_scroll);
        shoppingCart = new ArrayList<>();

        cocktailsListView = (RecyclerView) findViewById(R.id.cocktails_list_view);
        ItemClickSupport.addTo(cocktailsListView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                ItemOrderItem orderItem = database.getItemOrderItem(order, cocktails.get(position));
                if (orderItem == null) {
                    showAddItemDialog(cocktails.get(position), position);
                } else {
                    showPromptDialog(cocktails.get(position), position);
                }
            }
        });
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
        ItemClickSupport.addTo(nationalListView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                nationalAdapter.notifyDataSetChanged();
            }
        });
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
        ItemClickSupport.addTo(internationalListView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                internationalAdapter.notifyDataSetChanged();
            }
        });
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
            createNewOrder();

        } else {

            finish();

        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (shoppingCart.size() > 0){

                    Dialog dialog = new Dialog(context, R.style.StyledDialog);
                    View dialogView = View.inflate(context, R.layout.dialog_shopping_cart, null);
                    dialog.setContentView(dialogView);

                    GillSansLightTextView totalLabel = (GillSansLightTextView) dialogView.findViewById(R.id.total_label);
                    totalLabel.setText("$" + Utils.getFormat(getOrderTotal()));

                    RecyclerView shoppingCartListView = (RecyclerView) dialog.findViewById(R.id.shopping_cart_list_view);
                    shoppingCartListView.setLayoutManager(new LinearLayoutManager(context));
                    shoppingCartListView.setAdapter(new ShoppingCartAdapter(R.layout.item_cart_item, shoppingCart));

                    dialog.show();

                } else {
                    Utils.showSnackbar("Debes agregar al menos un producto al carrito", CreatePackageActivity.this, R.id.activity_create_package);
                }
            }
        });
    }

    private double getOrderTotal() {
        double total = 0;
        for (ItemOrderItem orderItem : shoppingCart){
            total = total + (orderItem.getQuantity() * orderItem.getSelectablePlaceItem().getPrice());
        }
        return total;
    }

    private void showPromptDialog(final SelectablePlaceItem placeItem, final int position){

        final Dialog prompt = new Dialog(context, R.style.StyledDialog);
        View promptView = View.inflate(context, R.layout.dialog_prompt_create_package, null);
        prompt.setContentView(promptView);

        Button editButton = (Button) promptView.findViewById(R.id.edit_button);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prompt.dismiss();
                showAddItemDialog(placeItem, position);
            }
        });

        Button deleteButton = (Button) promptView.findViewById(R.id.delete_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prompt.dismiss();
                ItemOrderItem orderItem = database.getItemOrderItem(order, placeItem);
                removeItem(orderItem);
                if (database.deleteItemFromOrder(order, placeItem)){
                    if (placeItem.getItem().getItemType().getId() == ItemTypes.COCKTAIL) {
                        cocktails.get(position).setSelected(false);
                        cocktailsAdapter.notifyDataSetChanged();
                    } else if (placeItem.getItem().getItemType().getId() == ItemTypes.NATIONAL) {
                        nationalDrinks.get(position).setSelected(false);
                        nationalAdapter.notifyDataSetChanged();
                    } else {
                        internationalDrinks.get(position).setSelected(false);
                        internationalAdapter.notifyDataSetChanged();
                    }
                    Utils.showSnackbar("Producto borrado del pedido", CreatePackageActivity.this, R.id.activity_create_package);
                } else {
                    Utils.showSnackbar("No se pudo borrar el producto del pedido", CreatePackageActivity.this, R.id.activity_create_package);
                }
            }
        });
        prompt.show();
    }

    private void showAddItemDialog(final SelectablePlaceItem placeItem, final int position){

        final Dialog dialog = new Dialog(context, R.style.StyledDialog);
        final View view = View.inflate(context, R.layout.dialog_add_to_chart, null);
        dialog.setContentView(view);

        final GillSansSemiBoldTextView currentUnitsLabel = (GillSansSemiBoldTextView) view.findViewById(R.id.units_label);
        currentUnitsLabel.setText(String.valueOf(0));

        final ItemOrderItem existingOrderItem = database.getItemOrderItem(order, placeItem);
        if (existingOrderItem != null){
            currentUnitsLabel.setText(String.valueOf(existingOrderItem.getQuantity()));
        }

        LinearLayout addButton = (LinearLayout) view.findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int value = Integer.valueOf(currentUnitsLabel.getText().toString());
                currentUnitsLabel.setText(String.valueOf(value + 1));
            }
        });

        LinearLayout decreaseButton = (LinearLayout) view.findViewById(R.id.decrease_button);
        decreaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int value = Integer.valueOf(currentUnitsLabel.getText().toString());
                if (value > 0){
                    currentUnitsLabel.setText(String.valueOf(value - 1));
                }
            }
        });

        Button finishButton = (Button) view.findViewById(R.id.finish_button);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.valueOf(currentUnitsLabel.getText().toString()) == 0){
                    Snackbar.make(view, "Debes añadir minimo una unidad", Snackbar.LENGTH_SHORT).show();
                } else {
                    dialog.dismiss();
                    if (existingOrderItem == null) {
                        ItemOrderItem orderItem = new ItemOrderItem();
                        orderItem.setQuantity(Integer.valueOf(currentUnitsLabel.getText().toString()));
                        orderItem.setItemOrder(order);
                        orderItem.setSelectablePlaceItem(placeItem);
                        if (database.createItemOrderItem(orderItem)) {
                            shoppingCart.add(orderItem);
                            if (placeItem.getItem().getItemType().getId() == ItemTypes.COCKTAIL) {
                                cocktails.get(position).setSelected(true);
                                cocktailsAdapter.notifyDataSetChanged();
                            } else if (placeItem.getItem().getItemType().getId() == ItemTypes.NATIONAL) {
                                nationalDrinks.get(position).setSelected(true);
                                nationalAdapter.notifyDataSetChanged();
                            } else {
                                internationalDrinks.get(position).setSelected(true);
                                internationalAdapter.notifyDataSetChanged();
                            }
                            Utils.showSnackbar("Añadido con exito", CreatePackageActivity.this, R.id.activity_create_package);
                        } else {
                            Utils.showSnackbar("Error añadiendo producto", CreatePackageActivity.this, R.id.activity_create_package);
                        }
                    } else {
                        existingOrderItem.setQuantity(Integer.valueOf(currentUnitsLabel.getText().toString()));
                        if (database.createItemOrderItem(existingOrderItem)){
                            updateShoppingCartItem(Integer.valueOf(currentUnitsLabel.getText().toString()), existingOrderItem);
                        } else {
                            Utils.showSnackbar("Error actualizando producto", CreatePackageActivity.this, R.id.activity_create_package);
                        }
                    }
                }
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
    }

    private void updateShoppingCartItem(int quantity, ItemOrderItem existingOrderItem) {
        for (int i = 0; i < shoppingCart.size(); i++){
            ItemOrderItem orderItem = shoppingCart.get(i);
            if (orderItem.getSelectablePlaceItem().getId() == existingOrderItem.getSelectablePlaceItem().getId()){
                existingOrderItem.setQuantity(quantity);
                shoppingCart.set(i, existingOrderItem);
            }
        }
    }

    private void createNewOrder() {

        DateTime time = new DateTime();
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd hh:mm:ss");
        String currentDate = formatter.print(time);

        order = new ItemOrder();
        order.setId(Utils.generateOrderId(place, database.getCurrentId()));
        order.setUser(database.getCurrentId());
        order.setTotal(0);
        order.setDate(currentDate);
        order.setRedeemed(false);

        if (!database.createNewItemOrder(order)){
            Utils.showSnackbar("Error creando pedido", CreatePackageActivity.this, R.id.activity_create_package);
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

    private void removeItem(ItemOrderItem orderItem){
        for (ItemOrderItem shoppingCartOrderItem : shoppingCart){
            if (orderItem.getSelectablePlaceItem().getId() == shoppingCartOrderItem.getSelectablePlaceItem().getId()){
                shoppingCart.remove(shoppingCartOrderItem);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        database.deleteOrder(order);
        finish();
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
                            List<SelectablePlaceItem> placeItems = new ArrayList<>();
                            for (int i = 0; i < menuResponse.length(); i++) {
                                JSONObject placeItemObject = menuResponse.getJSONObject(i);

                                Item item = new Item();
                                item.setName(placeItemObject.getString("name"));
                                item.setItemType(database.getItemType(placeItemObject.getInt("type_id")));

                                SelectablePlaceItem placeItem = new SelectablePlaceItem();
                                placeItem.setId(placeItemObject.getInt("id"));
                                placeItem.setPlace(place);
                                placeItem.setItem(item);
                                placeItem.setDiscount(placeItemObject.getDouble("discount"));
                                placeItem.setPrice(placeItemObject.getDouble("price"));
                                placeItem.setSelected(false);

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

    private void setUpListViews(List<SelectablePlaceItem> placeItems){

        cocktails = new ArrayList<>();
        nationalDrinks = new ArrayList<>();
        internationalDrinks = new ArrayList<>();

        for (SelectablePlaceItem placeItem : placeItems){
            if (placeItem.getItem().getItemType().getId() == ItemTypes.COCKTAIL){
                cocktails.add(placeItem);
            } else if (placeItem.getItem().getItemType().getId() == ItemTypes.NATIONAL){
                nationalDrinks.add(placeItem);
            } else {
                internationalDrinks.add(placeItem);
            }
        }

        if (cocktails.size() > 0){
            cocktailsAdapter = new CreatePackageAdapter(context, R.layout.item_place_selectable_item, cocktails);
            cocktailsListView.setAdapter(cocktailsAdapter);
        } else {
            showCocktailsNotFoundView();
        }

        if (nationalDrinks.size() > 0){
            nationalAdapter = new CreatePackageAdapter(context, R.layout.item_place_selectable_item, nationalDrinks);
            nationalListView.setAdapter(nationalAdapter);
        } else {
            showNationalNotFoundView();
        }

        if (internationalDrinks.size() > 0){
            internationalAdapter = new CreatePackageAdapter(context, R.layout.item_place_selectable_item, internationalDrinks);
            internationalListView.setAdapter(internationalAdapter);
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
}
