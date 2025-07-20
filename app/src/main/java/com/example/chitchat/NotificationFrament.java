package com.example.chitchat;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;

import java.util.ArrayList;
import java.util.List;

public class NotificationFrament extends Fragment {
    private RecyclerView recyclerView;
    private  VideoAdapter videoAdapter;
    private List<Vide0_item> vedioList;

    public NotificationFrament() {

        // Required empty public constructor
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment



        View view = inflater.inflate(R.layout.fragment_notification_frament, container, false);
        vedioList = genarateVedioList();
        recyclerView = view.findViewById(R.id.notification_RecyclerView);
        videoAdapter = new VideoAdapter(vedioList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter( videoAdapter );

        return view;



    }

    private List<Vide0_item> genarateVedioList() {
        List<Vide0_item> videoItems = new ArrayList<>();
        videoItems.add(new Vide0_item(R.drawable.family, R.drawable.chanel, "Movie trailer", "sithara_Entairtener"));
        videoItems.add(new Vide0_item(R.drawable.frds, R.drawable.chanel, "Movie teser", "cenimas_Entairtener"));
        videoItems.add(new Vide0_item(R.drawable.nene, R.drawable.chanel, "createv_knowlwdge", "sithara_Entairtener"));
        videoItems.add(new Vide0_item(R.drawable.frd2, R.drawable.chanel, "Movie trailer", "sithara_Entairtener"));
        videoItems.add(new Vide0_item(R.drawable.frds3, R.drawable.chanel, "Movie teser", "cenimas_Entairtener"));
        videoItems.add(new Vide0_item(R.drawable.vedio_thum, R.drawable.chanel, "Movie teser", "cenimas_Entairtener"));
        videoItems.add(new Vide0_item(R.drawable.cat, R.drawable.chanel, "createv_knowlwdge", "sithara_Entairtener"));
        return videoItems;







    }
    

}