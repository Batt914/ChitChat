package com.example.chitchat;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Home_adpter extends RecyclerView.Adapter<Home_adpter.ViewHolder>{
    private ArrayList<Home_items1> home_items;
    private  Context context;
    public Home_adpter(Context context, ArrayList<Home_items1> home_items) {
        
        this.home_items = home_items;
        this.context = context;
    }

    @NonNull
    @Override
    public Home_adpter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new Home_adpter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.home_items,parent,false) );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        holder.username.setText(home_items.get(position).getUser_Name());
        Home_items1 homeItems1=home_items.get(position);
        holder.username.setText(homeItems1.getUser_Name());
        Picasso.get().load(homeItems1.getUser_Image()).into(holder.profile);

        holder.useritems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,ChatWindow.class);
                intent.putExtra("username", home_items.get(holder.getAdapterPosition()).getUser_Name());
                intent.putExtra("id", home_items.get(holder.getAdapterPosition()).getId());
                intent.putExtra("imageuri",home_items.get(holder.getAdapterPosition()).getUser_Image());


                context.startActivity(intent);
            }
        });




    }

    @Override
    public int getItemCount() {
        return home_items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView username;
        private final RelativeLayout useritems;
        private final ShapeableImageView profile;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.h_Name);
            useritems=itemView.findViewById(R.id.User_items);
            profile = itemView.findViewById(R.id.h_Image112);
        }
    }
}
