package co.vamospues.vamospues.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import java.util.List;

import co.vamospues.vamospues.R;
import co.vamospues.vamospues.main.EventDetailActivity;
import co.vamospues.vamospues.models.Event;

/**
 * Created by Manuela Duque M on 05/04/2017.
 */

public class EventsPagerAdapter extends PagerAdapter {

    private final Context context;
    private final List<Event> events;
    private final int resource;

    public EventsPagerAdapter(Context context, int resource, List<Event> events){
        this.context = context;
        this.resource = resource;
        this.events = events;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        final Event event = events.get(position);

        View view = View.inflate(context, resource, null);
        ImageView eventImage = (ImageView) view.findViewById(R.id.event_image);
        eventImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, EventDetailActivity.class).putExtra("event", event));
            }
        });

        Picasso.with(context).load(event.getImageUrl()).into(eventImage);
        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return events.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
