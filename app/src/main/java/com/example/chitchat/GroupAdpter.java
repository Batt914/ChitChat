package com.example.chitchat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class GroupAdpter extends RecyclerView.Adapter<GroupAdpter.ViewHolder>{
    private final List<Group_items> group_items;
    private final Context context;
    public GroupAdpter(Context context, List<Group_items> group_items) {
        this.group_items = group_items;
        this.context = context;
    }

    @NonNull
    @Override
    public GroupAdpter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GroupAdpter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.group_items,parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull GroupAdpter.ViewHolder holder, int position) {
        holder.User_Name.setText(group_items.get(position).getUser_Name());


    }

    @Override
    public int getItemCount() {
        return group_items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView User_Name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            User_Name = itemView.findViewById(R.id.User_Name);
        }
    }
}