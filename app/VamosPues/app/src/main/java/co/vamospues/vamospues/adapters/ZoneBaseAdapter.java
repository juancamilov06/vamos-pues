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
import co.vamospues.vamospues.models.Zone;

/**
 * Created by Manuela Duque M on 29/04/2017.
 */

public class ZoneBaseAdapter extends BaseAdapter {

    private final List<Zone> zones;
    private final Context context;
    private final int resource;

    public ZoneBaseAdapter(Context context, int resource, List<Zone> places){
        this.context = context;
        this.resource = resource;
        this.zones = places;
    }

    @Override
    public int getCount() {
        return zones.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return zones.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(resource, null);
            holder.nameLabel = (GillSansSemiBoldTextView) convertView.findViewById(R.id.name_label);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Zone zone = zones.get(position);
        if (zone != null){
            holder.nameLabel.setText(zone.getName());
        }
        return convertView;
    }

    private class ViewHolder{
        GillSansSemiBoldTextView nameLabel;
    }
}
