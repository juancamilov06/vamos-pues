package co.vamospues.vamospues.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.text.DecimalFormat;
import java.util.List;

import co.vamospues.vamospues.R;
import co.vamospues.vamospues.helpers.GillSansBoldTextView;
import co.vamospues.vamospues.helpers.GillSansLightTextView;
import co.vamospues.vamospues.helpers.GillSansSemiBoldTextView;
import co.vamospues.vamospues.models.SelectablePlaceItem;

/**
 * Created by Manuela Duque M on 22/04/2017.
 */

public class CreatePackageAdapter extends RecyclerView.Adapter<CreatePackageAdapter.ViewHolder>{

    private Context context;
    private List<SelectablePlaceItem> placeItems;
    private int resource;
    private DecimalFormat format = new DecimalFormat("###,###.##");

    public CreatePackageAdapter(Context context, int resource, List<SelectablePlaceItem> placeItems){
        this.resource = resource;
        this.placeItems = placeItems;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                resource, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SelectablePlaceItem placeItem = placeItems.get(position);
        if (placeItem != null){
            holder.nameLabel.setText(placeItem.getItem().getName());
            holder.priceLabel.setText("$" + format.format(placeItem.getPrice()));
            if (placeItem.getDiscount() == 0){
                holder.discountLabel.setVisibility(View.GONE);
            } else {
                holder.discountLabel.setText("- " + placeItem.getDiscount() + "%");
            }
            if (placeItem.isSelected()){
                holder.isSelectedLayout.setVisibility(View.VISIBLE);
            } else {
                holder.isSelectedLayout.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return placeItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        GillSansLightTextView priceLabel;
        GillSansBoldTextView discountLabel;
        GillSansSemiBoldTextView nameLabel;
        LinearLayout isSelectedLayout;

        ViewHolder(View itemView) {
            super(itemView);
            discountLabel = (GillSansBoldTextView) itemView.findViewById(R.id.discount_label);
            nameLabel = (GillSansSemiBoldTextView) itemView.findViewById(R.id.name_label);
            priceLabel = (GillSansLightTextView) itemView.findViewById(R.id.price_label);
            isSelectedLayout = (LinearLayout) itemView.findViewById(R.id.selected_background);
        }
    }

}
