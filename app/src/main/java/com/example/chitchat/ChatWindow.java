package com.example.chitchat;

import static com.google.android.material.internal.ViewUtils.hideKeyboard;
import static com.google.android.material.internal.ViewUtils.showKeyboard;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ComponentCaller;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ChatWindow extends AppCompatActivity {
    private static final int PICK_MEDIA_REQUEST = 1;

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
    DatabaseReference sendANDreciveAudio=FirebaseDatabase.getInstance().getReference("chat_audio");
    DatabaseReference sendANDreciveVideo=FirebaseDatabase.getInstance().getReference("chat_video");
//    msgAdpetr msgAdpetr;

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
        reciver_profile=findViewById(R.id.reciver_img);
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
            }
        });



        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg=textmsg.getText().toString();
                textmsg.setText("");
                msgModel msgmodel=new msgModel(msg,null,senderUID,null,null);


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

    private void uploadaudioeToFirebase(Uri audioUri) {
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
                msgModel msgmodel=new msgModel(null,null,senderUID,null,chatAudio);

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

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            if (data.getClipData() != null) {
                int count = data.getClipData().getItemCount();
                for (int i = 0; i < count; i++) {
                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                    Toast.makeText(ChatWindow.this,"uploading Stat",Toast.LENGTH_SHORT).show();
                    uploadImageToFirebase(imageUri);

                }
            } else if (data.getData() != null) {
                Uri imageUri = data.getData();
                Toast.makeText(ChatWindow.this,"uploading Stat",Toast.LENGTH_SHORT).show();
                uploadImageToFirebase(imageUri);
            }
        } //Image intent...
    }


    private void uploadImageToFirebase(Uri imageUri) {

        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("chat_images")
                .child(System.currentTimeMillis() + "_" + imageUri.getLastPathSegment());

        storageRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
            storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                // real tym data base
                String chatImages=uri.toString();
                String reciverRoom,senderRoom;
                String senderUID=auth.getUid();
                String reciverUID=getIntent().getStringExtra("id");
                senderRoom=senderUID+reciverUID;
                reciverRoom=reciverUID+senderUID;
                msgModel msgmodel=new msgModel(null,chatImages,senderUID,null,null);

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


            });
        });
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data, @NonNull ComponentCaller caller) {
        super.onActivityResult(requestCode, resultCode, data, caller);
        if (requestCode==3 && resultCode==RESULT_OK){
            if (data.getClipData() != null){
                int count=data.getClipData().getItemCount();
                for (int i=0;i<count;i++){
                    Uri VideoUri=data.getClipData().getItemAt(i).getUri();
                    uploadVideoInFirebase(VideoUri);
                }
            }else if (data.getData() != null){
                Uri VideoUri=data.getData();
                uploadVideoInFirebase(VideoUri);

            }
        }
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
                        msgModel msgmodel=new msgModel(null,null,senderUID,chatVedio,null);

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