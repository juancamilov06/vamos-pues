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
import co.vamospues.vamospues.helpers.GillSansSemiBoldTextView;
import co.vamospues.vamospues.models.Zone;

/**
 * Created by Manuela Duque M on 12/03/2017.
 */

public class ZoneAdapter extends RecyclerView.Adapter<ZoneAdapter.ViewHolder> {

    private int resource;
    private List<Zone> zones;

    public ZoneAdapter(int resource, List<Zone> zones) {
        this.zones = zones;
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
        Zone zone = zones.get(position);
        if (zone != null){
            holder.zoneLabel.setText(zone.getName());
        }
    }

    @Override
    public int getItemCount() {
        return zones.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        GillSansSemiBoldTextView zoneLabel;

        ViewHolder(View itemView) {
            super(itemView);
            zoneLabel = (GillSansSemiBoldTextView) itemView.findViewById(R.id.title_label);
        }
    }

}
