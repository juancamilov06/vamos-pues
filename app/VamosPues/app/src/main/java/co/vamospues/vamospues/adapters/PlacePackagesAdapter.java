package co.vamospues.vamospues.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import co.vamospues.vamospues.R;
import co.vamospues.vamospues.helpers.GillSansSemiBoldTextView;
import co.vamospues.vamospues.models.PlacePackage;

/**
 * Created by Manuela Duque M on 21/04/2017.
 */

public class PlacePackagesAdapter extends ArrayAdapter<PlacePackage> {

    private final Context context;
    private final int resource;
    private final List<PlacePackage> placePackages;

    public PlacePackagesAdapter(Context context, int resource, List<PlacePackage> placePackages) {
        super(context, resource, placePackages);
        this.context = context;
        this.resource = resource;
        this.placePackages = placePackages;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            holder = new ViewHolder();
            convertView = View.inflate(context, resource, null);

            holder.packageImage = (ImageView) convertView.findViewById(R.id.package_image);
            holder.nameLabel = (GillSansSemiBoldTextView) convertView.findViewById(R.id.name_label);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        PlacePackage placePackage = placePackages.get(position);
        if (placePackage != null){
            Picasso.with(context).load(placePackage.getaPackage().getImgUrl()).into(holder.packageImage);
            holder.nameLabel.setText(placePackage.getaPackage().getName());
        }

        return convertView;
    }

    private class ViewHolder {
        ImageView packageImage;
        GillSansSemiBoldTextView nameLabel;
    }
}
