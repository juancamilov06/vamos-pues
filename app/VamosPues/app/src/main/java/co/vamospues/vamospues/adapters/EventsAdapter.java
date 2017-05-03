package co.vamospues.vamospues.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import co.vamospues.vamospues.R;
import co.vamospues.vamospues.helpers.GillSansBoldTextView;
import co.vamospues.vamospues.helpers.GillSansSemiBoldTextView;
import co.vamospues.vamospues.helpers.GrayscaleTransformation;
import co.vamospues.vamospues.models.Event;

/**
 * Created by Manuela Duque M on 05/04/2017.
 */

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder> {

    private int resource;
    private List<Event> events;
    private Context context;

    public EventsAdapter(Context context, List<Event> events, int resource) {
        this.events = events;
        this.context = context;
        this.resource = resource;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, resource, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Event event = events.get(position);
        holder.nameLabel.setText(event.getName());
        Picasso picasso = Picasso.with(context);
        picasso.with(context).load(event.getImageUrl()).transform(new GrayscaleTransformation(picasso)).into(holder.eventImage);
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView eventImage;
        GillSansSemiBoldTextView nameLabel;

        private ViewHolder(View itemView) {
            super(itemView);
            eventImage = (ImageView) itemView.findViewById(R.id.event_image);
            nameLabel = (GillSansSemiBoldTextView) itemView.findViewById(R.id.name_label);
        }
    }
}
