package com.example.chitchat;

import static androidx.core.util.TimeUtils.formatDuration;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RenderEffect;
import android.graphics.Shader;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class msgAdpetr extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final List<msgModel> msgModelArrayList;
    private final Context context;
    int ITEM_SEND=1;
    Handler handler;
    int ITEM_RECIVE=2;
    int ItemSendImages=3;
    int ItemReciveImages=4;
    int ItemSendVideo=5;
    int ItemReciveVideo=6;
    int ItemSendSongs=7;
    int ItemReciveSongs=8;
    private Runnable runnable;
    private Handler handler1 = new Handler();
    private int currenplayinPossition=1;

    public msgAdpetr( Context context,List<msgModel> msgModelArrayList) {
        this.msgModelArrayList = msgModelArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==ITEM_SEND){
            View view= LayoutInflater.from(context).inflate(R.layout.sender,parent,false);
            return new senderViewHolder(view);

        } else if (viewType==ITEM_RECIVE) {
            View view= LayoutInflater.from(context).inflate(R.layout.reciver_layout,parent,false);
            return new reciverViewHolder(view);


        } else if (viewType==ItemSendImages) {
            View view= LayoutInflater.from(context).inflate(R.layout.activity_send_images,parent,false);
            return new senderViewHolder(view);


        } else if (viewType==ItemReciveImages) {
            View view= LayoutInflater.from(context).inflate(R.layout.recive_images,parent,false);
            return new reciverViewHolder(view);


        }else if (viewType==ItemSendVideo) {
            View view= LayoutInflater.from(context).inflate(R.layout.sender_video,parent,false);
            return new senderViewHolder(view);


        }else if (viewType==ItemReciveVideo) {
            View view= LayoutInflater.from(context).inflate(R.layout.reciver_video,parent,false);
            return new reciverViewHolder(view);


        }else if (viewType==ItemSendSongs) {
            View view= LayoutInflater.from(context).inflate(R.layout.sender_music,parent,false);
            return new senderViewHolder(view);


        }else if (viewType==ItemReciveSongs) {
            View view= LayoutInflater.from(context).inflate(R.layout.recive_musci,parent,false);
            return new reciverViewHolder(view);


        }else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        msgModel msgModel=msgModelArrayList.get(position);
        if (holder.getClass()==senderViewHolder.class){
            senderViewHolder viewHolder=(senderViewHolder) holder;
            if (msgModel.getMsg()!=null){
                viewHolder.msgtext.setText(msgModel.getMsg());
            }
            if (msgModel.getShareImages()!=null){
                Glide.with(context).load(msgModel.getShareImages()).into(viewHolder.chatImages);
            }
            if (msgModel.getShareVideos()!=null){
                ExoPlayer player=new ExoPlayer.Builder(context).build();
                viewHolder.playerView.setPlayer(player);
                MediaItem mediaItem=MediaItem.fromUri(msgModel.getShareVideos());
                player.setMediaItem(mediaItem);
                player.prepare();
                player.setPlayWhenReady(false);
                if (player.isPlaying()){
                    viewHolder.playerView.setPlayer(player);
                }

            }
            if (msgModel.getShareAudios()!=null){

                viewHolder.songName.setText(msgModel.getTitle());
                viewHolder.artistName.setText(msgModel.getAlubumpic());
                if (msgModel.getArtist()!=null){
                    Bitmap albumArt = decodeBase64ToBitmap(msgModel.getArtist());
                    if (albumArt != null) {
                        viewHolder.bgSongImage.setImageBitmap(albumArt);
                        if (viewHolder.bgSongImage!=null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
                            viewHolder.bgSongImage.setRenderEffect(RenderEffect.createBlurEffect(5,5, Shader.TileMode.REPEAT));

                        }
                    }else {
                        Picasso.get().load(msgModel.getArtist()).into(viewHolder.bgSongImage);
                    }
                }


                String playSong=msgModel.getShareAudios();
                MediaPlayer player=new MediaPlayer();
                Runnable runnable=new Runnable() {
                    @Override
                    public void run() {

                        viewHolder.seekBar.setProgress(player.getCurrentPosition());
                        handler.postDelayed(this,500);

                    }
                };


                try {
                    player.setDataSource(playSong);
                    player.prepareAsync();
                    player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            int currentduareton=player.getDuration();
                            viewHolder.dueration.setText(convertion(currentduareton));
                        }
                    });


                    viewHolder.playButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Animation animation= AnimationUtils.loadAnimation(context,R.anim.but_anim);
                            viewHolder.playButton.startAnimation(animation);

                            if (player.isPlaying()){
                                viewHolder.playButton.setImageResource(R.drawable.play123);
                                player.pause();
                                handler.removeCallbacks(runnable);
                            }else {
                                viewHolder.playButton.setImageResource(R.drawable.pause);
                                player.start();
                                viewHolder.seekBar.setMax(player.getDuration());
                                handler=new Handler();
                                handler.postDelayed(runnable,0);
                            }
                        }
                    });

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                viewHolder.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if (fromUser){
                            player.seekTo(progress);
                        }
                        viewHolder.dueration.setText(convertion(player.getCurrentPosition()));
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });

            }
        }
        else if (holder.getClass()==reciverViewHolder.class) {
            reciverViewHolder viewHolder=(reciverViewHolder) holder;
            if (msgModel.getMsg()!=null){
                viewHolder.msgtext.setText(msgModel.getMsg());
            }
            if (msgModel.getShareImages()!=null){
                Glide.with(context).load(msgModel.getShareImages()).into(viewHolder.chatImages);
            }
            if (msgModel.getShareVideos()!=null){
                ExoPlayer player=new ExoPlayer.Builder(context).build();
                viewHolder.playerView.setPlayer(player);
                MediaItem mediaItem=MediaItem.fromUri(msgModel.getShareVideos());
                player.setMediaItem(mediaItem);
                player.prepare();
                player.setPlayWhenReady(false);
            }
            if (msgModel.getShareAudios()!=null){

                viewHolder.songName.setText(msgModel.getTitle());
                viewHolder.artistName.setText(msgModel.getAlubumpic());
                if (msgModel.getArtist()!=null){
                    Bitmap albumArt = decodeBase64ToBitmap(msgModel.getArtist());
                    if (albumArt != null) {
                        viewHolder.bgSongImage.setImageBitmap(albumArt);
                        if (viewHolder.bgSongImage!=null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
                            viewHolder.bgSongImage.setRenderEffect(RenderEffect.createBlurEffect(5,5, Shader.TileMode.REPEAT));

                        }
                    }else {
                        Picasso.get().load(msgModel.getArtist()).into(viewHolder.bgSongImage);
                    }
                }


                String playSong=msgModel.getShareAudios();
                MediaPlayer player=new MediaPlayer();
                Runnable runnable=new Runnable() {
                    @Override
                    public void run() {

                        viewHolder.seekBar.setProgress(player.getCurrentPosition());
                        handler.postDelayed(this,500);

                    }
                };


                try {
                    player.setDataSource(playSong);
                    player.prepareAsync();
                    player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            int currentduareton=player.getDuration();
                            viewHolder.dueration.setText(convertion(currentduareton));
                        }
                    });


                    viewHolder.playButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Animation animation= AnimationUtils.loadAnimation(context,R.anim.but_anim);
                            viewHolder.playButton.startAnimation(animation);


                            if (player.isPlaying()){
                                viewHolder.playButton.setImageResource(R.drawable.play123);
                                player.pause();
                                handler.removeCallbacks(runnable);
                            }else {
                                viewHolder.playButton.setImageResource(R.drawable.pause);
                                player.start();
                                viewHolder.seekBar.setMax(player.getDuration());
                                handler=new Handler();
                                handler.postDelayed(runnable,0);
                            }
                        }
                    });

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                viewHolder.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if (fromUser){
                            player.seekTo(progress);
                        }
                        viewHolder.dueration.setText(convertion(player.getCurrentPosition()));
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });

            }
        }


    }

    @Override
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);

    }

    private String convertion(int currentduareton) {
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(currentduareton),
                TimeUnit.MILLISECONDS.toSeconds(currentduareton) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(currentduareton)));
    }


    public Bitmap decodeBase64ToBitmap(String base64String) {
        byte[] decodedBytes = Base64.decode(base64String, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    @Override
    public int getItemCount() {
        return msgModelArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        msgModel msgModel=msgModelArrayList.get(position);
        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(msgModel.getSenderUID())){
            if (msgModel.getShareImages()!=null){
                return ItemSendImages;
            }
            if (msgModel.getShareVideos()!=null){
                return ItemSendVideo;

            }
            if (msgModel.getShareAudios()!=null){
                return ItemSendSongs;

            }return ITEM_SEND;



        }else {
            if (msgModel.getShareImages()!=null){
                return ItemReciveImages;
            }
            if (msgModel.getShareVideos()!=null){
                return ItemReciveVideo;

            }
            if(msgModel.getShareAudios()!=null){
                return ItemReciveSongs;

            }
            return ITEM_RECIVE;
        }

    }

    public class senderViewHolder extends RecyclerView.ViewHolder{
        TextView msgtext;
        ImageView chatImages;
        StyledPlayerView playerView;

        // musicPart

        ImageView bgSongImage;
        TextView songName,artistName;
        SeekBar seekBar;
        ImageView playButton;
        ImageButton nextButton,previousButton;
        TextView position,dueration;

        @SuppressLint("WrongViewCast")
        public senderViewHolder(@NonNull View itemView) {
            super(itemView);
            msgtext=itemView.findViewById(R.id.sendermsg);
            chatImages=itemView.findViewById(R.id.SenderchatImages);
            playerView=itemView.findViewById(R.id.sender_video);

            // music part

            bgSongImage=itemView.findViewById(R.id.bgSongImage);
            seekBar=itemView.findViewById(R.id.seekbarchat111);
            playButton=itemView.findViewById(R.id.play_bt11);
            songName=itemView.findViewById(R.id.songname);
            artistName=itemView.findViewById(R.id.artistName);
            dueration=itemView.findViewById(R.id.sondDuaretion11);

        }
    }

    public class reciverViewHolder extends RecyclerView.ViewHolder{

        TextView msgtext;
        ImageView chatImages;
        StyledPlayerView playerView;

        // music part
        ImageView bgSongImage;
        SeekBar seekBar;
        ImageView playButton;
        ImageButton nextButton,previousButton;
        TextView songName,artistName,position,dueration;

        public reciverViewHolder(@NonNull View itemView) {
            super(itemView);
            msgtext=itemView.findViewById(R.id.recivermsg);
            chatImages=itemView.findViewById(R.id.recive_chat_images);
            playerView=itemView.findViewById(R.id.reciver_video);

            // music part

            bgSongImage=itemView.findViewById(R.id.bgSongImage22);
            seekBar=itemView.findViewById(R.id.seekbarchat22);
            playButton=itemView.findViewById(R.id.play_bt22);
            nextButton=itemView.findViewById(R.id.playfv22);
            previousButton=itemView.findViewById(R.id.play_bv22);
            songName=itemView.findViewById(R.id.songname22);
            artistName=itemView.findViewById(R.id.artistName22);
            dueration=itemView.findViewById(R.id.sondDuaretion122);
            position=itemView.findViewById(R.id.songPosition123);

        }
    }

}
