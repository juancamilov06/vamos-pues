package co.vamospues.vamospues.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.text.DecimalFormat;
import java.util.List;

import co.vamospues.vamospues.R;
import co.vamospues.vamospues.helpers.GillSansLightTextView;
import co.vamospues.vamospues.helpers.GillSansSemiBoldTextView;
import co.vamospues.vamospues.models.PackageItem;

/**
 * Created by Manuela Duque M on 21/04/2017.
 */

public class PackageItemsAdapter extends RecyclerView.Adapter<PackageItemsAdapter.ViewHolder> {

    private int resource;
    private List<PackageItem> packageItems;
    private DecimalFormat format = new DecimalFormat("###,###.##");

    public PackageItemsAdapter(int resource, List<PackageItem> packageItems) {
        this.resource = resource;
        this.packageItems = packageItems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                resource, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PackageItem item = packageItems.get(position);
        if (item != null){
            holder.nameLabel.setText(item.getName());
            holder.quantityLabel.setText("x " + String.valueOf(item.getQuantity()));
            holder.priceLabel.setText("$" + String.valueOf(format.format(item.getPrice())));
            holder.totalLabel.setText("$" + String.valueOf(format.format(item.getTotal())));
        }
    }

    @Override
    public int getItemCount() {
        return packageItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        GillSansSemiBoldTextView nameLabel;
        GillSansLightTextView quantityLabel, priceLabel, totalLabel;

        private ViewHolder(View itemView) {
            super(itemView);
            nameLabel = (GillSansSemiBoldTextView) itemView.findViewById(R.id.name_label);
            priceLabel = (GillSansLightTextView) itemView.findViewById(R.id.price_label);
            totalLabel = (GillSansLightTextView) itemView.findViewById(R.id.total_label);
            quantityLabel = (GillSansLightTextView) itemView.findViewById(R.id.quantity_label);
        }
    }
}
