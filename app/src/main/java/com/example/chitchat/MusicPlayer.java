package com.example.chitchat;

import static java.security.AccessController.getContext;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.RenderEffect;
import android.graphics.Shader;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.MediaStore;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Base64;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MusicPlayer extends AppCompatActivity {
    ImageView playPuseButton;
    ImageButton playNextButton, playPreviousButton;
    SeekBar seekBar;
    TextView position,duration,musicPleyar;
    RecyclerView MusicRecycler;

    MediaPlayer mediaPlayer;
    Runnable runnable;
    Handler handler;
    int currentPossiton;
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("music");
    private boolean isBound = false;
    ImageView imageView;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

//        View decorView = getWindow().getDecorView();
//        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
//        decorView.setSystemUiVisibility(uiOptions);

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_music_player);
        playNextButton=findViewById(R.id.playfv);
        playPreviousButton=findViewById(R.id.play_bv);
        playPuseButton=findViewById(R.id.play_bt);
        seekBar=findViewById(R.id.seekbar);
        position=findViewById(R.id.strt);
        duration=findViewById(R.id.end);
        musicPleyar=findViewById(R.id.musicPleyar);
        imageView=findViewById(R.id.image000);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            imageView.setRenderEffect(RenderEffect.createBlurEffect(5,5, Shader.TileMode.REPEAT));
        }
        musicPleyar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("audio/*");
                startActivityForResult(Intent.createChooser(intent,"Select Audio"),1);

            }
        });

        mediaPlayer=MediaPlayer.create(this,R.raw.akt);
        runnable=new Runnable() {
            @Override
            public void run() {
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                handler.postDelayed(this,500);
            }
        };
        duration.setText(convertDuration(mediaPlayer.getDuration()));


        playPuseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation scaleAnim = AnimationUtils.loadAnimation(v.getContext(), R.anim.but_anim);
                playPuseButton.startAnimation(scaleAnim);


                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    playPuseButton.setImageResource(R.drawable.play22);
                    handler.removeCallbacks(runnable);


                } else {
                    mediaPlayer.start();
                    playPuseButton.setImageResource(R.drawable.pause);
                    seekBar.setMax(mediaPlayer.getDuration());
                    handler=new Handler();
                    handler.postDelayed(runnable,0);



                }
            }
        });
        playNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation=AnimationUtils.loadAnimation(v.getContext(),R.anim.forverd);
                playNextButton.startAnimation(animation);
                currentPossiton=mediaPlayer.getCurrentPosition();
                int duration=mediaPlayer.getDuration();
                if (mediaPlayer.isPlaying() && duration!=currentPossiton){
                    currentPossiton=currentPossiton+10000;
                    mediaPlayer.seekTo(currentPossiton);
                    position.setText(convertDuration(currentPossiton));
                    seekBar.setProgress(currentPossiton);


                }
            }
        });
        playPreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation=AnimationUtils.loadAnimation(v.getContext(),R.anim.backward);
                playPreviousButton.startAnimation(animation);
                currentPossiton=mediaPlayer.getCurrentPosition();
                int duration=mediaPlayer.getDuration();
                if (mediaPlayer.isPlaying() && duration!=currentPossiton){
                    currentPossiton=currentPossiton-10000;
                    mediaPlayer.seekTo(currentPossiton);
                    position.setText(convertDuration(currentPossiton));
                    seekBar.setProgress(currentPossiton);
                }
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser){
                    mediaPlayer.seekTo(progress);
                }
                position.setText(convertDuration(mediaPlayer.getCurrentPosition()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });




        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                playPuseButton.setImageResource(R.drawable.play22);
                seekBar.setProgress(0);
                position.setText("00:00");
                handler.removeCallbacks(runnable);

            }
        });



    }

    private String convertDuration(int duration) {
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(duration),
                TimeUnit.MILLISECONDS.toSeconds(duration) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1 && resultCode==RESULT_OK && data!=null){
            Uri uri=data.getData();
            uploadFirebase(uri);

            Toast.makeText(this,"Audio Selected",Toast.LENGTH_SHORT).show();

        }
    }

    private void uploadFirebase(Uri uri) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(this, uri);
        String title = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
        String artist = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
        byte[] albumArt=retriever.getEmbeddedPicture();
        Toast.makeText(this,title+"is process",Toast.LENGTH_SHORT).show();

        StorageReference storageReference= FirebaseStorage.getInstance().getReference().child("Audio_files").child(uri.getLastPathSegment());
        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onSuccess(Uri uri) {
                        String url=uri.toString();
                        String base69=null;
                        if (albumArt!=null){
                            base69= Base64.getEncoder().encodeToString(albumArt);
                        }
                        musicModel musicPleyar=new musicModel(title,artist,base69,url);
                        databaseReference.push().setValue(musicPleyar).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {


                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MusicPlayer.this,"Error Realtym database"+e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });


                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MusicPlayer.this,"Error"+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });


    }

}