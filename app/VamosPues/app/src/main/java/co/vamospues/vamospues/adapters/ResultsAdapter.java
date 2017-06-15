package co.vamospues.vamospues.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import co.vamospues.vamospues.R;
import co.vamospues.vamospues.helpers.GillSansSemiBoldTextView;
import co.vamospues.vamospues.models.Place;

/**
 * Created by Manuela Duque M on 15/03/2017.
 */

public class ResultsAdapter extends RecyclerView.Adapter<ResultsAdapter.ViewHolder> {

    private final Context context;
    private final int resource;
    private final List<Place> places;

    public ResultsAdapter(Context context, int resource, List<Place> places) {
        this.context = context;
        this.resource = resource;
        this.places = places;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                resource, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Place place = places.get(position);
        if (place != null){
            holder.nameLabel.setText(place.getName());
            String url = place.getImageUrl();
            if (!TextUtils.isEmpty(url)){
                Picasso.with(context).load(url).fit().centerCrop().into(holder.background);
            }
        }
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        GillSansSemiBoldTextView nameLabel;
        ImageView background;

        ViewHolder(View itemView) {
            super(itemView);
            nameLabel = (GillSansSemiBoldTextView) itemView.findViewById(R.id.name_label);
            background = (ImageView) itemView.findViewById(R.id.place_background);
        }
    }
}
