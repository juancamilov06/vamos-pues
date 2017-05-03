package co.vamospues.vamospues.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.DecimalFormat;
import java.util.List;

import co.vamospues.vamospues.R;
import co.vamospues.vamospues.helpers.GillSansLightTextView;
import co.vamospues.vamospues.helpers.GillSansSemiBoldTextView;
import co.vamospues.vamospues.models.Ticket;

/**
 * Created by Manuela Duque M on 27/04/2017.
 */

public class TicketsAdapter extends RecyclerView.Adapter<TicketsAdapter.ViewHolder> {

    private List<Ticket> tickets;
    private int resource;
    private DecimalFormat format = new DecimalFormat("###,###.##");

    public TicketsAdapter(int resource, List<Ticket> tickets){
        this.resource = resource;
        this.tickets = tickets;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                resource, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Ticket ticket = tickets.get(position);
        if (ticket != null){
            holder.priceLabel.setText("$" + format.format(ticket.getPrice()));
            holder.capacityLabel.setText(String.valueOf(ticket.getCapacity()));
            holder.locationLabel.setText(ticket.getLocation());
        }
    }

    @Override
    public int getItemCount() {
        return tickets.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        GillSansSemiBoldTextView priceLabel, locationLabel;
        GillSansLightTextView capacityLabel;

        public ViewHolder(View itemView) {
            super(itemView);
            priceLabel = (GillSansSemiBoldTextView) itemView.findViewById(R.id.price_label);
            locationLabel = (GillSansSemiBoldTextView) itemView.findViewById(R.id.location_label);
            capacityLabel = (GillSansLightTextView) itemView.findViewById(R.id.capacity_label);
        }

    }

}
