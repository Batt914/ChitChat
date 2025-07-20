package com.example.chitchat;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GroupFragment extends Fragment {
    private RecyclerView recyclerView;
    private final DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
    private final List<Group_items> group_items1= new ArrayList<>();




    public GroupFragment() {
        // Required empty public constructor
    }


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_group, container, false);
        recyclerView = view.findViewById(R.id.Group_Recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                group_items1.clear();
                for (DataSnapshot users : snapshot.child("user").getChildren()){
                    final String getUser_Name = users.child("username").getValue(String.class);
                    Group_items group_items = new Group_items(getUser_Name,"Active","");
                    group_items1.add(group_items);
                    GroupAdpter groupAdpter = new GroupAdpter(getContext(),group_items1);
                    recyclerView.setAdapter(groupAdpter);




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