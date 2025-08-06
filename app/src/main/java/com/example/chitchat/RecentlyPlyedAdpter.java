package com.example.chitchat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;

import java.util.ArrayList;

public class RecentlyPlyedAdpter extends RecyclerView.Adapter<RecentlyPlyedAdpter.ViewHolder> {
    private String currentPlayingUri = null;
    private RecyclerView.ViewHolder lastPlayingHolder = null;

    String Clint_Id="4a6602bf05784a179a0005bdc88a52fb";
    String Redicrect_uri="chitchat://callback";
    SpotifyAppRemote mspotifyAppRemote;
    private Context context;
    private ArrayList<SpotifyMetadata_Model> spotifyMetadataModels;
    private SpotifyAppRemote mSpotifyAppRemote;
    public RecentlyPlyedAdpter(Context context, ArrayList<SpotifyMetadata_Model> spotifyMetadataModels) {
        this.context = context;
        this.spotifyMetadataModels = spotifyMetadataModels;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.spotify_items,parent,false));
    }

    /**
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SpotifyMetadata_Model spotifyMetadataModel=spotifyMetadataModels.get(position);
        holder.songName.setText(spotifyMetadataModel.getSongName());
        holder.artistName.setText(spotifyMetadataModel.getArtistName());
        holder.movieName.setText(spotifyMetadataModel.getAlbumName());

        Glide.with(context).clear(holder.MovieImage);
        holder.MovieImage.setImageDrawable(null);
        if (spotifyMetadataModel.getImageUri() != null && spotifyMetadataModel.getImageUri().startsWith("https://")) {
            Glide.with(context).load(spotifyMetadataModel.getImageUri()).into(holder.MovieImage);
        }
        else if (spotifyMetadataModel.getImageUri() != null && spotifyMetadataModel.getImageUri().startsWith("spotify:image:")) {
            String validurl = spotifyMetadataModel.getImageUri().replace("spotify:image:", "https://i.scdn.co/image/");
            Glide.with(context).load(validurl).into(holder.MovieImage);
        }
        if (spotifyMetadataModel.getSong()!=null){

            String song=spotifyMetadataModel.getSong();

            ConnectionParams connectionParams=new ConnectionParams.Builder(Clint_Id)
                    .setRedirectUri(Redicrect_uri)
                    .showAuthView(true)
                    .build();

            SpotifyAppRemote.connect(context, connectionParams,
                    new Connector.ConnectionListener() {
                        @Override
                        public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                            mSpotifyAppRemote=spotifyAppRemote;
                            Toast.makeText(context,"Connected_adpter",Toast.LENGTH_SHORT).show();



                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            Toast.makeText(context,"Error"+throwable.getMessage(),Toast.LENGTH_SHORT).show();

                        }
                    });

            boolean[] isPlaying = {false};
            holder.playPuseButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Animation animation= AnimationUtils.loadAnimation(context,R.anim.but_anim);
                    v.startAnimation(animation);


                    if (mSpotifyAppRemote == null) {
                        Toast.makeText(context, "Spotify app not connected", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!isPlaying[0]) {
                        // Play the song
                        mSpotifyAppRemote.getPlayerApi().play(song);
                        holder.playPuseButton.setImageResource(R.drawable.pause_dark);
                        isPlaying[0] = true;
                    }
                    else {
                        // Pause the song
                        mSpotifyAppRemote.getPlayerApi().pause();
                        holder.playPuseButton.setImageResource(R.drawable.play22);
                        isPlaying[0] = false;
                    }
                }
            });

        }




    }

    /**
     * @return
     */
    @Override
    public int getItemCount() {
        return spotifyMetadataModels.size();
    }

    public void update(ArrayList<SpotifyMetadata_Model> newList) {
        spotifyMetadataModels=newList;
        notifyDataSetChanged();
        notifyItemRangeChanged(0,spotifyMetadataModels.size());
        notifyItemRangeInserted(0,spotifyMetadataModels.size());
        notifyItemRangeRemoved(0,spotifyMetadataModels.size());
        notifyItemRangeChanged(0,spotifyMetadataModels.size());
        notifyItemRangeRemoved(0,spotifyMetadataModels.size());
        notifyItemRangeInserted(0,spotifyMetadataModels.size());
        notifyItemRangeChanged(0,spotifyMetadataModels.size());
        notifyItemRangeRemoved(0,spotifyMetadataModels.size());
        notifyItemRangeInserted(0,spotifyMetadataModels.size());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView songName,artistName,movieName;
        static ImageView MovieImage;
        ImageView playPuseButton;

        @SuppressLint("WrongViewCast")
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            songName=itemView.findViewById(R.id.Song_name);
            artistName=itemView.findViewById(R.id.Artist_name);
            movieName=itemView.findViewById(R.id.Movie_name);
            MovieImage=itemView.findViewById(R.id.Move_image);
            playPuseButton=itemView.findViewById(R.id.playPuseButton);
        }
    }
}
