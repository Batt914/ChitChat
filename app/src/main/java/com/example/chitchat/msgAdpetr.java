package com.example.chitchat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.List;

public class msgAdpetr extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final List<msgModel> msgModelArrayList;
    private final Context context;
    int ITEM_SEND=1;
    int ITEM_RECIVE=2;
    int ItemSendImages=3;
    int ItemReciveImages=4;

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


        }else {
            return null;
        }
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        msgModel msgModel=msgModelArrayList.get(position);
        if (holder.getClass()==senderViewHolder.class){
            senderViewHolder viewHolder=(senderViewHolder) holder;
            if (msgModel.getMsg()!=null){
                viewHolder.msgtext.setText(msgModel.getMsg());
            }
            if (msgModel.getShareImages()!=null){
                Glide.with(context).load(msgModel.getShareImages()).into(viewHolder.chatImages);
            }
        } else if (holder.getClass()==reciverViewHolder.class) {
            reciverViewHolder viewHolder=(reciverViewHolder) holder;
            if (msgModel.getMsg()!=null){
                viewHolder.msgtext.setText(msgModel.getMsg());
            }
            if (msgModel.getShareImages()!=null){
                Glide.with(context).load(msgModel.getShareImages()).into(viewHolder.chatImages);
            }
        }

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
            }return ITEM_SEND;



        }else {
            if (msgModel.getShareImages()!=null){
                return ItemReciveImages;
            }
            return ITEM_RECIVE;
        }

    }

    public class senderViewHolder extends RecyclerView.ViewHolder{
        TextView msgtext;
        ImageView chatImages;

        public senderViewHolder(@NonNull View itemView) {
            super(itemView);
            msgtext=itemView.findViewById(R.id.sendermsg);
            chatImages=itemView.findViewById(R.id.SenderchatImages);
        }
    }

    public class reciverViewHolder extends RecyclerView.ViewHolder{

        TextView msgtext;
        ImageView chatImages;
        public reciverViewHolder(@NonNull View itemView) {
            super(itemView);
            msgtext=itemView.findViewById(R.id.recivermsg);
            chatImages=itemView.findViewById(R.id.recive_chat_images);
        }
    }

}
