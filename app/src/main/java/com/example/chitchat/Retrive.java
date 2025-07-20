package com.example.chitchat;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Retrive extends AppCompatActivity {

    ImageView imageView;
    DatabaseReference databaseReference;
    FirebaseAuth auth=FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_retrive);

        imageView=findViewById(R.id.rtv);

        databaseReference=FirebaseDatabase.getInstance().getReference().child("user").child(auth.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String imageModel = snapshot.child("imageuri").getValue().toString();
                if (imageModel != null) {
                    String url = imageModel.toString();
                    Glide.with(getApplicationContext()).load(url).into(imageView);
                } else {
                    Toast.makeText(Retrive.this, "ImageModel is null", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(Retrive.this, "error"+error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


    }
}