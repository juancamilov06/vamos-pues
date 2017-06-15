package co.vamospues.vamospues.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import co.vamospues.vamospues.R;
import co.vamospues.vamospues.helpers.GillSansSemiBoldTextView;
import co.vamospues.vamospues.models.Place;

/**
 * Created by Manuela Duque M on 29/04/2017.
 */

public class PlacesMapAdapter extends BaseAdapter {

    private final List<Place> places;
    private final Context context;
    private final int resource;

    public PlacesMapAdapter(Context context, int resource, List<Place> places){
        this.context = context;
        this.resource = resource;
        this.places = places;
    }

    @Override
    public int getCount() {
        return places.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return places.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(resource, null);
            holder.nameLabel = (GillSansSemiBoldTextView) convertView.findViewById(R.id.name_label);
            holder.placeImage = (ImageView) convertView.findViewById(R.id.place_background);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Place place = places.get(position);
        if (place != null){
            Picasso.with(context).load(place.getImageUrl()).fit().centerCrop().into(holder.placeImage);
            holder.nameLabel.setText(place.getName());
        }
        return convertView;
    }

    private class ViewHolder{
        GillSansSemiBoldTextView nameLabel;
        ImageView placeImage;
    }
}
