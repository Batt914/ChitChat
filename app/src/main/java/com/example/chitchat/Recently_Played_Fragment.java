package com.example.chitchat;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.spotify.android.appremote.api.SpotifyAppRemote;

import java.util.ArrayList;
import java.util.Collections;

public class Recently_Played_Fragment extends Fragment {
    private SpotifyAppRemote mSpotifyAppRemote;
    private RecyclerView recyclerView;
    private final DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Spotify_recently_played");
    private RelativeLayout useritems;

    public Recently_Played_Fragment() {
        // Required empty public constructor
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_recently__played_, container, false);
        useritems=view.findViewById(R.id.User_items);
        recyclerView=view.findViewById(R.id.Recently_played_recyclerView);

        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);



        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<SpotifyMetadata_Model> newList=new ArrayList<>();
                newList.clear();
                for (DataSnapshot users : snapshot.getChildren()){
                    for (DataSnapshot user : users.getChildren()){
                        {
                            final String getSongName = user.child("songName").getValue(String.class);
                            final String getMovieName=user.child("albumName").getValue(String.class);
                            final String getArtistName=user.child("artistName").getValue(String.class);
                            final String getImageUri=user.child("imageUri").getValue(String.class);
                            final String getsong=user.child("song").getValue(String.class);
                            final long getDueration=user.child("dueration").getValue(long.class);

                            SpotifyMetadata_Model spotifyMetadataModel=new SpotifyMetadata_Model(getsong,getSongName,getArtistName,getMovieName,getImageUri,getDueration);
                            newList.add(spotifyMetadataModel);

                        }
                    }
                }
                Collections.reverse(newList);
                RecentlyPlyedAdpter recentlyPlyed=new RecentlyPlyedAdpter(getContext(),newList);
                recentlyPlyed.update(newList);
                recyclerView.setAdapter(recentlyPlyed);
                recentlyPlyed.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("error");
                System.out.println(error.getMessage());
                System.out.println(error.getCode());
                System.out.println(error.getDetails());

            }
        });
        return view;

    }

}