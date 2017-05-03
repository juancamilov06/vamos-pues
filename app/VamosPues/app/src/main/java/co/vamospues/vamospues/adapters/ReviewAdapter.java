package co.vamospues.vamospues.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import co.vamospues.vamospues.R;
import co.vamospues.vamospues.helpers.GillSansLightTextView;
import co.vamospues.vamospues.helpers.GillSansSemiBoldTextView;
import co.vamospues.vamospues.models.Review;

/**
 * Created by Manuela Duque M on 03/04/2017.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private final List<Review> reviews;
    private final int resource;

    public ReviewAdapter(int resource, List<Review> reviews) {
        this.resource = resource;
        this.reviews = reviews;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                resource, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Review review = reviews.get(position);
        if (review != null){
            holder.contentLabel.setText(review.getContent());
            holder.nameLabel.setText(review.getUsername().toUpperCase());
            holder.ratingLabel.setText(String.valueOf(review.getRating()));
            holder.dateLabel.setText(review.getCreated());
        }
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        
        GillSansSemiBoldTextView nameLabel, ratingLabel, dateLabel;
        GillSansLightTextView contentLabel;

        ViewHolder(View itemView) {
            super(itemView);
            contentLabel = (GillSansLightTextView) itemView.findViewById(R.id.content_label);
            nameLabel = (GillSansSemiBoldTextView) itemView.findViewById(R.id.name_label);
            ratingLabel = (GillSansSemiBoldTextView) itemView.findViewById(R.id.rating_label);
            dateLabel = (GillSansSemiBoldTextView) itemView.findViewById(R.id.date_label);

        }
    }

}
