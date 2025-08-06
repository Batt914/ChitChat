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

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private final DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
    private final ArrayList<Home_items1> home_items1= new ArrayList<>();
    private  RelativeLayout useritems;



    public HomeFragment() {
        // Required empty public constructor
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView=view.findViewById(R.id.Home_RecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                home_items1.clear();
                for (DataSnapshot users : snapshot.child("user").getChildren())
                {
                    final String getUser_Name = users.child("username").getValue(String.class);
                    final String getuserImage=users.child("imageuri").getValue(String.class);
                    final String getid=users.child("id").getValue(String.class);

                    Home_items1 home_items = new Home_items1(getUser_Name,"",getuserImage,getid);
                    home_items1.add(home_items);
                    Home_adpter home_adpter = new Home_adpter(getContext(),home_items1);
                    recyclerView.setAdapter(home_adpter);



                }




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