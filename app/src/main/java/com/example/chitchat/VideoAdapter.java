package com.example.chitchat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {

    private List<Vide0_item> videoList;

    public VideoAdapter(List<Vide0_item> videoList) {
        this.videoList = videoList;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.items_vedio, parent, false );
        return new VideoViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        Vide0_item vide0_item = videoList.get( position );
        holder.videoThumbnail.setImageResource( vide0_item.getVedio_image() );
        holder.channelThumbnail.setImageResource( vide0_item.getChanel_image() );
        holder.videoTitle.setText( vide0_item.getTittle() );
        holder.channelName.setText( vide0_item.getChanel_name() );

    }

    @Override
    public int getItemCount() {

        return videoList.size();
    }

    public static class VideoViewHolder extends RecyclerView.ViewHolder {
        ImageView videoThumbnail;
        ImageView channelThumbnail;
        TextView videoTitle;
        TextView channelName;


        public VideoViewHolder(@NonNull View itemView) {
            super( itemView );
            videoThumbnail = itemView.findViewById( R.id.vedio_Thum );
            channelThumbnail = itemView.findViewById( R.id.chanel_image );
            videoTitle = itemView.findViewById( R.id.vedio_tittle );
            channelName = itemView.findViewById( R.id.channel_name );

        }
    }

}
