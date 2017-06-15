package co.vamospues.vamospues.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import co.vamospues.vamospues.R;
import co.vamospues.vamospues.helpers.GillSansLightTextView;
import co.vamospues.vamospues.helpers.GillSansSemiBoldTextView;
import co.vamospues.vamospues.models.Promo;

/**
 * Created by Manuela Duque M on 31/05/2017.
 */

public class PromoAdapter extends RecyclerView.Adapter<PromoAdapter.ViewHolder>{

    private final int resource;
    private final List<Promo> promos;
    private final Context context;

    public PromoAdapter(Context context, List<Promo> promos, int resource){
        this.promos = promos;
        this.context = context;
        this.resource = resource;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                resource, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Promo promo = promos.get(position);
        if (promo != null){
            holder.nameLabel.setText(promo.getName());
            holder.placeLabel.setText(promo.getPlace().getName());
            Picasso.with(context).load(promo.getImgUrl()).into(holder.promoImage);
        }
    }

    @Override
    public int getItemCount() {
        return promos.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView promoImage;
        GillSansSemiBoldTextView nameLabel;
        GillSansLightTextView placeLabel;

        public ViewHolder(View itemView) {
            super(itemView);
            promoImage = (ImageView) itemView.findViewById(R.id.promo_image);
            nameLabel = (GillSansSemiBoldTextView) itemView.findViewById(R.id.promo_name_label);
            placeLabel = (GillSansLightTextView) itemView.findViewById(R.id.place_label);
        }
    }

}
