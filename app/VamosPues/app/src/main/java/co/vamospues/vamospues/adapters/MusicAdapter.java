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
import co.vamospues.vamospues.models.Music;

/**
 * Created by Manuela Duque M on 12/03/2017.
 */

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.ViewHolder> {

    private int resource;
    private List<Music> musics;

    public MusicAdapter(int resource, List<Music> musics) {
        this.musics = musics;
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
        Music music = musics.get(position);
        if (music != null){
            holder.musicLabel.setText(music.getName());
        }
    }

    @Override
    public int getItemCount() {
        return musics.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        GillSansSemiBoldTextView musicLabel;
        public ViewHolder(View itemView) {
            super(itemView);
            musicLabel = (GillSansSemiBoldTextView) itemView.findViewById(R.id.title_label);
        }
    }

}
