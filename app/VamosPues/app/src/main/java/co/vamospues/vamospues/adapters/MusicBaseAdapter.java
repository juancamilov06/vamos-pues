package co.vamospues.vamospues.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import co.vamospues.vamospues.R;
import co.vamospues.vamospues.helpers.GillSansSemiBoldTextView;
import co.vamospues.vamospues.models.Music;
import co.vamospues.vamospues.models.Zone;

/**
 * Created by Manuela Duque M on 29/04/2017.
 */

public class MusicBaseAdapter extends BaseAdapter {

    private final List<Music> music;
    private final Context context;
    private final int resource;

    public MusicBaseAdapter(Context context, int resource, List<Music> music){
        this.context = context;
        this.resource = resource;
        this.music = music;
    }

    @Override
    public int getCount() {
        return music.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return music.get(position).getId();
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

        Music music = this.music.get(position);
        if (music != null){
            holder.nameLabel.setText(music.getName());
        }
        return convertView;
    }

    private class ViewHolder{
        GillSansSemiBoldTextView nameLabel;
    }
}
