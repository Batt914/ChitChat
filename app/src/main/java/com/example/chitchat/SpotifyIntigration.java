package com.example.chitchat;

import static com.spotify.sdk.android.auth.LoginActivity.REQUEST_CODE;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SpotifyIntigration extends AppCompatActivity {
    TextView spotify;
    String Clint_Id="4a6602bf05784a179a0005bdc88a52fb";
    String Redicrect_uri="chitchat://callback";
    private SpotifyAppRemote mspotifyAppRemote;
    private Handler handler=new Handler();
    private Runnable songChecker;
    private String lastTrackUri = null;
    Set<String> uploadKeys=new HashSet<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_spotify_intigration);

        AuthorizationRequest.Builder builder = new AuthorizationRequest.Builder(
                Clint_Id,
                AuthorizationResponse.Type.TOKEN,
                Redicrect_uri
        );
        builder.setScopes(new String[]{"app-remote-control", "user-read-playback-state", "user-modify-playback-state", "user-read-currently-playing", "user-read-recently-played"
        ,"playlist-read-private","playlist-modify-public","playlist-modify-private","user-library-read","user-library-modify","user-read-email","user-read-private","user-top-read"});
        AuthorizationRequest request = builder.build();
        AuthorizationClient.openLoginActivity(this, REQUEST_CODE, request);






    }

    @Override
    protected void onStart() {
        super.onStart();
        ConnectionParams connectionParams=new ConnectionParams.Builder(Clint_Id)
                .setRedirectUri(Redicrect_uri)
                .showAuthView(true)
                .build();


        SpotifyAppRemote.connect(this, connectionParams,
                new Connector.ConnectionListener() {
                    @Override
                    public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                        mspotifyAppRemote=spotifyAppRemote;
                        ffetchCurrentSong();
                        Toast.makeText(SpotifyIntigration.this,"Connected",Toast.LENGTH_SHORT).show();

                    }

                    private void ffetchCurrentSong() {
                        songChecker=new Runnable() {
                            @Override
                            public void run() {
                                mspotifyAppRemote.getPlayerApi().getPlayerState()
                                        .setResultCallback(playerState -> {
                                            if (playerState==null){
                                                Toast.makeText(SpotifyIntigration.this,"Error",Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                            String trackName=playerState.track.name;
                                            String artistName=playerState.track.artist.name;
                                            String albumName=playerState.track.album.name;
                                            String imageUri = playerState.track.imageUri.raw;
                                            String song=playerState.track.uri;
                                            long trackDueration=playerState.track.duration;
                                            SpotifyMetadata_Model spotifyMetadata_model=new SpotifyMetadata_Model(song,trackName,artistName,albumName,imageUri,trackDueration);
                                            DatabaseReference databaseRef= FirebaseDatabase.getInstance().getReference("Spotify_playlist");
                                            String key=databaseRef.push().getKey();
                                            String safKey=song.replace(":","_");
                                            databaseRef.child(safKey).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    if (snapshot.exists()){
//                                                        Toast.makeText(SpotifyIntigration.this,"Already Exist",Toast.LENGTH_SHORT).show();

                                                    }
                                                    else {
                                                        databaseRef.push().child(safKey).setValue(spotifyMetadata_model).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                Toast.makeText(SpotifyIntigration.this,"Success",Toast.LENGTH_SHORT).show();

                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Toast.makeText(SpotifyIntigration.this,"Error"+e.getMessage(),Toast.LENGTH_SHORT).show();





                                                            }
                                                        });
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });




                                        });

                            }
                        };
                        handler.post(songChecker);

                    }


                    @Override
                    public void onFailure(Throwable throwable) {
                        Toast.makeText(SpotifyIntigration.this,"Error"+throwable.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                });





    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQUEST_CODE){
            AuthorizationResponse response=AuthorizationClient.getResponse(resultCode,data);
            if (response.getType()==AuthorizationResponse.Type.TOKEN){
                String accessToken=response.getAccessToken();
                fechSong(accessToken);
            }
        }


    }

    private void fechSong(String accessToken) {
        songChecker=new Runnable() {
            @Override
            public void run() {

                OkHttpClient client=new OkHttpClient();
                Request request=new Request.Builder()
                        .url("https://api.spotify.com/v1/me/player/recently-played?limit=5")
                        .addHeader("Authorization","Bearer "+accessToken)
                        .build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Toast.makeText(SpotifyIntigration.this,"Error"+e.getMessage(),Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        final String responseData=response.body().string();

                        runOnUiThread(() -> {
                            if (response.isSuccessful()) {
                                try {
                                    JSONObject jsonObject=new JSONObject(responseData);
                                    JSONArray SongItems=jsonObject.getJSONArray("items");

                                    DatabaseReference databaseRef= FirebaseDatabase.getInstance().getReference("Spotify");
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
                                        databaseRef.child(safKey).orderByChild("timestamp").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if (!snapshot.exists()) {
                                                    databaseRef.push().child(safKey).setValue(spotifyMetadata_model);


                                                }else {
                                                    Toast.makeText(SpotifyIntigration.this,"Already Exist",Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });

                                    }






                                    Toast.makeText(SpotifyIntigration.this,"Success adding songs ",Toast.LENGTH_SHORT).show();

                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }



                            } else {
                                Toast.makeText(SpotifyIntigration.this, "Failed ❌ Code: " + response.code(), Toast.LENGTH_LONG).show();
                                Log.e("SpotifyAPI", "Error Response:\n" + responseData);
                            }
                        });


                    }
                });
            }
        };
        handler.post(songChecker);
    }
}