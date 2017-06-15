package co.vamospues.vamospues.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.zip.Inflater;

import co.vamospues.vamospues.R;
import co.vamospues.vamospues.database.DatabaseHandler;
import co.vamospues.vamospues.helpers.GillSansSemiBoldTextView;
import co.vamospues.vamospues.models.Place;

/**
 * Created by Manuela Duque M on 24/04/2017.
 */

public class FavoritePlacesAdapter extends RecyclerView.Adapter<FavoritePlacesAdapter.ViewHolder>{

    private final Context context;
    private final int resource;
    private final List<Place> favorites;
    private final ViewBinderHelper binderHelper = new ViewBinderHelper();
    private final DatabaseHandler database;

    public FavoritePlacesAdapter(Context context, int resource, List<Place> favorites){
        this.context = context;
        this.resource = resource;
        this.favorites = favorites;
        this.database = new DatabaseHandler(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(resource, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (favorites != null && 0 <= position && position < favorites.size()) {
            Place place = favorites.get(position);
            binderHelper.bind(holder.swipeLayout, String.valueOf(place.getId()));
            holder.bind(place);
        }
    }

    @Override
    public int getItemCount() {
        if (favorites == null)
            return 0;
        return favorites.size();
    }

    /**
     * Only if you need to restore open/close state when the orientation is changed.
     * Call this method in {@link android.app.Activity#onSaveInstanceState(Bundle)}
     */
    public void saveStates(Bundle outState) {
        binderHelper.saveStates(outState);
    }

    /**
     * Only if you need to restore open/close state when the orientation is changed.
     * Call this method in {@link android.app.Activity#onRestoreInstanceState(Bundle)}
     */
    public void restoreStates(Bundle inState) {
        binderHelper.restoreStates(inState);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private SwipeRevealLayout swipeLayout;
        private View deleteLayout;
        GillSansSemiBoldTextView nameLabel;
        ImageView background;

        public ViewHolder(View itemView) {
            super(itemView);
            swipeLayout = (SwipeRevealLayout) itemView.findViewById(R.id.swipe_layout);
            deleteLayout = itemView.findViewById(R.id.delete_layout);
            nameLabel = (GillSansSemiBoldTextView) itemView.findViewById(R.id.name_label);
            background = (ImageView) itemView.findViewById(R.id.place_background);
        }

        void bind(final Place place) {
            deleteLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    database.deleteFavorite(place.getId());
                    favorites.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                }
            });

            nameLabel.setText(place.getName());
            Picasso.with(context).load(place.getImageUrl()).fit().centerCrop().into(background);
        }
    }

}
