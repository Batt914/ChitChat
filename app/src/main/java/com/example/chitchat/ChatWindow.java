package com.example.chitchat;

import static com.google.android.material.internal.ViewUtils.hideKeyboard;
import static com.google.android.material.internal.ViewUtils.showKeyboard;
import static com.spotify.sdk.android.auth.LoginActivity.REQUEST_CODE;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ComponentCaller;
import android.content.Context;
import android.content.Intent;
import android.hardware.display.VirtualDisplay;
import android.media.MediaMetadataRetriever;
import android.media.MediaRecorder;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ChatWindow extends AppCompatActivity {

    CardView sendbtn;
    ShapeableImageView chatprofile,sender_profile,reciver_profile;
    LinearLayout music,gellary,audio,vedio;
    LinearLayout jiosaavan,spotify,wynkmusic;
    CardView dropdownmenu;
    EditText textmsg;
    LinearLayout expandableMenu,expandableMenu21;
    String recNmae;
    TextView recNamee;
    FirebaseAuth auth;
    FirebaseDatabase database,firebaseDatabase;

    DatabaseReference reference;
    String senderRoom,reciverRoom;
    RecyclerView chatRecycler;
    List<msgModel> msgModelArrayList;
    msgAdpetr msgAdpetr1;


    private static final int REQUEST_CODE = 1000;
    private MediaProjectionManager projectionManager;
    private MediaProjection mediaProjection;
    private VirtualDisplay virtualDisplay;
    private MediaRecorder mediaRecorder;
    String Clint_Id="4a6602bf05784a179a0005bdc88a52fb";
    String Redicrect_uri="chitchat://callback";
    DatabaseReference databaseRef= FirebaseDatabase.getInstance().getReference("Spotify_recently_played");
    private SpotifyAppRemote mspotifyAppRemote;
    private Handler handler=new Handler();
    private Runnable songChecker;
    private String lastTrackUri = null;


    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);



        setContentView(R.layout.activity_chat_window);
        database=FirebaseDatabase.getInstance();
        reference=database.getReference("chats");
        auth=FirebaseAuth.getInstance();



        recNmae=getIntent().getStringExtra("username");
        String reciverUID=getIntent().getStringExtra("id");
        chatprofile=findViewById(R.id.h_Image1111);
        String profile=getIntent().getStringExtra("imageuri");
        Picasso.get().load(profile).into(chatprofile);

        msgModelArrayList= new ArrayList<>();
        String senderUID=auth.getUid();
        senderRoom = (senderUID+reciverUID);
        reciverRoom= reciverUID+senderUID;


        recNamee=findViewById(R.id.chat_UserName);
        recNamee.setText(""+recNmae);



        sendbtn=findViewById(R.id.sendbtn);

        textmsg=findViewById(R.id.textmsg);
        textmsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(expandableMenu.getVisibility() == View.VISIBLE){

                    expandableMenu.animate()
                            .scaleX(0.8f)
                            .scaleY(0.8f)
                            .translationY(-50f)
                            .alpha(0f)
                            .setInterpolator(new AccelerateInterpolator())
                            .setDuration(300)
                            .withEndAction(new Runnable() {
                                @Override
                                public void run() {
                                    expandableMenu.setVisibility(View.GONE);
                                }
                            })
                            .start();

                }
            }
        }); // atometocally layout will close

        textmsg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    sendbtn.setVisibility(View.VISIBLE);

                } else {
                    sendbtn.setVisibility(View.GONE);
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });    // send button viewer


        expandableMenu=findViewById(R.id.expanded_menu1);
        dropdownmenu=findViewById(R.id.dropdown_menu1);
        dropdownmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (expandableMenu.getVisibility() == View.GONE) {
                    expandableMenu.setScaleX(0.8f);
                    expandableMenu.setScaleY(0.8f);
                    expandableMenu.setTranslationY(-50f);
                    expandableMenu.setAlpha(0f);
                    expandableMenu.setVisibility(View.VISIBLE);
                    expandableMenu.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .translationY(0f)
                            .alpha(1f)
                            .setInterpolator(new OvershootInterpolator(1.5f)) // Smooth pop effect
                            .setDuration(500) // Slightly slower animation
                            .start();
                    if (imm != null && getCurrentFocus() != null) {
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                } else {
                    expandableMenu.animate()
                            .scaleX(0.8f)
                            .scaleY(0.8f)
                            .translationY(-50f)
                            .alpha(0f)
                            .setInterpolator(new AccelerateInterpolator())
                            .setDuration(300)
                            .withEndAction(new Runnable() {
                                @Override
                                public void run() {
                                    expandableMenu.setVisibility(View.GONE);
                                }
                            })
                            .start();

                }
            }

        });   // dropdownmenu 1


        LinearLayout bottomLayout = findViewById(R.id.ll3);
        ObjectAnimator slideUp = ObjectAnimator.ofFloat(bottomLayout, "translationY", 100f, 0f);
        slideUp.setDuration(600);
        slideUp.start();



        chatRecycler=findViewById(R.id.chat_room_Recycler);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        chatRecycler.setLayoutManager(linearLayoutManager);
        msgAdpetr1=new msgAdpetr(ChatWindow.this,msgModelArrayList);
        chatRecycler.setAdapter(msgAdpetr1);



        DatabaseReference chatreference=database.getReference().child("chats").child(senderRoom).child("msg");
        chatreference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                msgModelArrayList.clear();
                for (DataSnapshot dataSnapshot: snapshot.getChildren() ){
                    msgModel msgmodel=dataSnapshot.getValue(msgModel.class);
                    if (msgmodel != null){
                        msgModelArrayList.add(msgmodel);

                    }else {
                        Toast.makeText(ChatWindow.this,"Error",Toast.LENGTH_SHORT).show();
                    }
                    msgAdpetr1.notifyDataSetChanged();
                    Log.d("ChatDebug", "Messages count: " + msgModelArrayList.size());

                    chatRecycler.post(new Runnable() {
                        @Override
                        public void run() {
                            chatRecycler.smoothScrollToPosition(msgModelArrayList.size() - 1);
                        }
                    });


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        }); // chat messages adpter



        music=findViewById(R.id.music);
        expandableMenu21=findViewById(R.id.expanded_menu2);
        music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (expandableMenu21.getVisibility()==View.GONE){
                    expandableMenu21.setVisibility(View.VISIBLE);
                }else {
                    expandableMenu21.setVisibility(View.GONE);
                }
            }
        }); // dropdown menu 2

        gellary=findViewById(R.id.gellary);
        gellary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"),1);
                expandableMenu.animate()
                        .scaleX(0.8f)
                        .scaleY(0.8f)
                        .translationY(-50f)
                        .alpha(0f)
                        .setInterpolator(new AccelerateInterpolator())
                        .setDuration(300)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                expandableMenu.setVisibility(View.GONE);
                            }
                        })
                        .start();
            }
        });


        audio=findViewById(R.id.Audio);
        audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                intent.setType("audio/*");
                audioPickerLauncher.launch(intent);
                expandableMenu.animate()
                        .scaleX(0.8f)
                        .scaleY(0.8f)
                        .translationY(-50f)
                        .alpha(0f)
                        .setInterpolator(new AccelerateInterpolator())
                        .setDuration(300)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                expandableMenu.setVisibility(View.GONE);
                            }
                        })
                        .start();
            }
        });

        vedio=findViewById(R.id.video);
        vedio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setType("video/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Video"),3);
                expandableMenu.animate()
                        .scaleX(0.8f)
                        .scaleY(0.8f)
                        .translationY(-50f)
                        .alpha(0f)
                        .setInterpolator(new AccelerateInterpolator())
                        .setDuration(300)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                expandableMenu.setVisibility(View.GONE);
                            }
                        })
                        .start();
            }
        });



        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg=textmsg.getText().toString();
                textmsg.setText("");
                msgModel msgmodel=new msgModel(msg,null,senderUID,null,null,null,null,null);


                database.getReference().child("chats").child(senderRoom).child("msg").push()
                        .setValue(msgmodel).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {


                                database.getReference().child("chats").child(reciverRoom).child("msg").push()
                                        .setValue(msgmodel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {



                                            }
                                        });


                            }
                        });



            }
        }); // send messages in firebase database



        jiosaavan=findViewById(R.id.JioSaavan);
        jiosaavan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.jiosaavn.com/"));
                startActivity(intent);


            }
        }); // jiosaavan

        spotify=findViewById(R.id.Spotify);
        spotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        spotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(ChatWindow.this);
                View view= LayoutInflater.from(ChatWindow.this).inflate(R.layout.bottom_sheet,null);
                expandableMenu.animate()
                        .scaleX(0.8f)
                        .scaleY(0.8f)
                        .translationY(-50f)
                        .alpha(0f)
                        .setInterpolator(new AccelerateInterpolator())
                        .setDuration(300)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                expandableMenu.setVisibility(View.GONE);
                            }
                        })
                        .start();
                bottomSheetDialog.setContentView(view);
                bottomSheetDialog.show();
                MyViewpageAdpater myViewpageAdpater=new MyViewpageAdpater(ChatWindow.this);
                TabLayout tabLayout=view.findViewById(R.id.tablayout);
                ViewPager2 viewPager2=view.findViewById(R.id.view_pager);
                viewPager2.setAdapter(myViewpageAdpater);
                tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        viewPager2.setCurrentItem(tab.getPosition());
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {

                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {

                    }
                });
                viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                    /**
                     * @param position Position index of the new selected page.
                     */
                    @Override
                    public void onPageSelected(int position) {
                        super.onPageSelected(position);
                        tabLayout.getTabAt(position).select();

                    }
                });
                AuthorizationRequest.Builder builder = new AuthorizationRequest.Builder(
                        Clint_Id,
                        AuthorizationResponse.Type.TOKEN,
                        Redicrect_uri
                );
                builder.setScopes(new String[]{"app-remote-control", "user-read-playback-state", "user-modify-playback-state", "user-read-currently-playing", "user-read-recently-played"
                        ,"playlist-read-private","playlist-modify-public","playlist-modify-private","user-library-read","user-library-modify","user-read-email","user-read-private","user-top-read"});
                AuthorizationRequest request = builder.build();
                AuthorizationClient.openLoginActivity(ChatWindow.this, REQUEST_CODE, request);




                ConnectionParams connectionParams=new ConnectionParams.Builder(Clint_Id)
                        .setRedirectUri(Redicrect_uri).showAuthView(true).build();

                SpotifyAppRemote.connect(ChatWindow.this, connectionParams, new Connector.ConnectionListener() {
                    @Override
                    public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                        mspotifyAppRemote=spotifyAppRemote;
                        Toast.makeText(ChatWindow.this,"Spotify Connected Successfully ",Toast.LENGTH_SHORT).show();
                        ffetchCurrentSong();

                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Toast.makeText(ChatWindow.this,"Spotify Connected Failed "+throwable.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                });



            }

            private void ffetchCurrentSong() {
                if (mspotifyAppRemote!=null){
                    mspotifyAppRemote.getPlayerApi().getPlayerState()
                            .setResultCallback(playerState -> {
                                if (playerState==null){
                                    Toast.makeText(ChatWindow.this,"Error",Toast.LENGTH_SHORT).show();
                                }else {
                                    String trackName=playerState.track.name;
                                    String artistName=playerState.track.artist.name;
                                    String albumName=playerState.track.album.name;
                                    String imageUri = playerState.track.imageUri.raw;
                                    String song=playerState.track.uri;
                                    long trackDueration=playerState.track.duration;
                                    SpotifyMetadata_Model spotifyMetadata_model=new SpotifyMetadata_Model(song,trackName,artistName,albumName,imageUri,trackDueration);
                                    String safKey=song.replace(":","_");
                                    databaseRef.child(safKey).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.exists()){
                                                Toast.makeText(ChatWindow.this,"alredy_Exits",Toast.LENGTH_SHORT).show();
                                            }else {
                                                databaseRef.push().child(safKey).setValue(spotifyMetadata_model).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Toast.makeText(ChatWindow.this,"new song added",Toast.LENGTH_SHORT).show();

                                                    }
                                                });
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });




                                }


                            });
                }
            }
        });
    }

    ActivityResultLauncher<Intent> audioPickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {

                    Intent data = result.getData();
                    if (data.getClipData() != null){
                        int count = data.getClipData().getItemCount();
                        for (int i = 0; i < count; i++) {
                            Uri audioUri  = data.getClipData().getItemAt(i).getUri();
                            Toast.makeText(ChatWindow.this,"uploading Stat",Toast.LENGTH_SHORT).show();
                            uploadaudioeToFirebase(audioUri );

                        }
                    } else if (data.getData() != null) {
                        Uri audioUri  = data.getData();
                        Toast.makeText(ChatWindow.this,"uploading Stat",Toast.LENGTH_SHORT).show();
                        uploadaudioeToFirebase(audioUri );
                    }
                    // Handle audioUri
                }
            }
    );

    @SuppressLint("NewApi")
    private void uploadaudioeToFirebase(Uri audioUri) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(this, audioUri);
        String title = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
        String artist = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
        byte[] albumArt=retriever.getEmbeddedPicture();


        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("chat_audio")
                .child(System.currentTimeMillis() + "_" + audioUri.getLastPathSegment());

        storageRef.putFile(audioUri).addOnSuccessListener(taskSnapshot -> {
            storageRef.getDownloadUrl().addOnSuccessListener(uri -> {

                String chatAudio=uri.toString();
                String reciverRoom,senderRoom;
                String senderUID=auth.getUid();
                String reciverUID=getIntent().getStringExtra("id");
                senderRoom=senderUID+reciverUID;
                reciverRoom=reciverUID+senderUID;
                String songImage=null;
                if (albumArt!=null){
                    songImage= Base64.getEncoder().encodeToString(albumArt);
                }


                msgModel msgmodel=new msgModel(null,null,senderUID,null,chatAudio,title,artist,songImage);

                database.getReference().child("chats").child(senderRoom).child("msg").push()
                        .setValue(msgmodel).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {


                                database.getReference().child("chats").child(reciverRoom).child("msg").push()
                                        .setValue(msgmodel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {



                                            }
                                        });


                            }
                        });

                Toast.makeText(this, "Upload sucessesfully: ", Toast.LENGTH_SHORT).show();


           });
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });

    }

//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == 1 && resultCode == RESULT_OK) {
//            if (data.getClipData() != null) {
//                int count = data.getClipData().getItemCount();
//                for (int i = 0; i < count; i++) {
//                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
//                    Toast.makeText(ChatWindow.this,"uploading Stat",Toast.LENGTH_SHORT).show();
//                    uploadImageToFirebase(imageUri);
//
//                }
//            } else if (data.getData() != null) {
//                Uri imageUri = data.getData();
//                Toast.makeText(ChatWindow.this,"uploading Stat",Toast.LENGTH_SHORT).show();
//                uploadImageToFirebase(imageUri);
//            }
//        } //Image intent...
//    }
//
//
//    private void uploadImageToFirebase(Uri imageUri) {
//
//        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("chat_images")
//                .child(System.currentTimeMillis() + "_" + imageUri.getLastPathSegment());
//
//        storageRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
//            storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
//                // real tym data base
//                String chatImages=uri.toString();
//                String reciverRoom,senderRoom;
//                String senderUID=auth.getUid();
//                String reciverUID=getIntent().getStringExtra("id");
//                senderRoom=senderUID+reciverUID;
//                reciverRoom=reciverUID+senderUID;
//                msgModel msgmodel=new msgModel(null,chatImages,senderUID,null,null,null,null,null);
//
//                database.getReference().child("chats").child(senderRoom).child("msg").push()
//                        .setValue(msgmodel).addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//
//
//                                database.getReference().child("chats").child(reciverRoom).child("msg").push()
//                                        .setValue(msgmodel).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                            @Override
//                                            public void onComplete(@NonNull Task<Void> task) {
//
//
//
//                                            }
//                                        });
//
//
//                            }
//                        });
//
//
//            });
//        });
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            AuthorizationResponse response = AuthorizationClient.getResponse(resultCode, data);
            String accessToken=response.getAccessToken();
            updateSong(accessToken);

        }
    }

    private void updateSong(String accessToken) {
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder()
                .url("https://api.spotify.com/v1/me/player/recently-played?limit=10")
                .addHeader("Authorization","Bearer "+accessToken)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> {
                    Toast.makeText(ChatWindow.this,"responce error"+e.getMessage(),Toast.LENGTH_SHORT).show();

                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseData=response.body().string();
                runOnUiThread(() -> {
                    if (response.isSuccessful()){
                        try {
                            JSONObject jsonObject=new JSONObject(responseData);
                            JSONArray SongItems=jsonObject.getJSONArray("items");


                            for (int i=0;i<SongItems.length();i++) {

                                JSONObject songObject = SongItems.getJSONObject(i);
                                String playedAt = songObject.getString("played_at"); // Unique key

                                JSONObject trackObject = songObject.getJSONObject("track");
                                String trackName = trackObject.getString("name");

                                String artistName = trackObject.getJSONArray("artists").getJSONObject(0).getString("name");
                                String albumName = trackObject.getJSONObject("album").getString("name");
                                String imageUri = trackObject.getJSONObject("album").getJSONArray("images").getJSONObject(0).getString("url");
                                String song = trackObject.getString("uri");
                                long trackDueration = trackObject.getLong("duration_ms");
                                SpotifyMetadata_Model spotifyMetadata_model = new SpotifyMetadata_Model(song, trackName, artistName, albumName, imageUri, trackDueration);
                                String safKey = song.replace(":", "_").replace(".", "_");
                                databaseRef.child(safKey).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (!snapshot.exists()) {
                                            databaseRef.push().child(safKey).setValue(spotifyMetadata_model);


                                        }else {
                                            Toast.makeText(ChatWindow.this,"Already Exist",Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }






                            Toast.makeText(ChatWindow.this,"Success adding songs ",Toast.LENGTH_SHORT).show();

                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }



                    }



                });

            }
        });

    }


    private void uploadVideoInFirebase(Uri videoUri) {
        StorageReference VideoReff=FirebaseStorage.getInstance().getReference().child("chat_Videos").child(System.currentTimeMillis() + "_" + videoUri.getLastPathSegment());
        VideoReff.putFile(videoUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                VideoReff.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String chatVedio=uri.toString();
                        String reciverRoom,senderRoom;
                        String senderUID=auth.getUid();
                        String reciverUID=getIntent().getStringExtra("id");
                        senderRoom=senderUID+reciverUID;
                        reciverRoom=reciverUID+senderUID;
                        msgModel msgmodel=new msgModel(null,null,senderUID,chatVedio,null,null,null,null);

                        database.getReference().child("chats").child(senderRoom).child("msg").push()
                                .setValue(msgmodel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {


                                        database.getReference().child("chats").child(reciverRoom).child("msg").push()
                                                .setValue(msgmodel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {



                                                    }
                                                });


                                    }
                                });




                        Toast.makeText(ChatWindow.this,"updated sucssessfully",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ChatWindow.this,"error"+e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }

}