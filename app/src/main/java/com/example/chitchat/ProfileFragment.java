package com.example.chitchat;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {
    ShapeableImageView profileImage;
    ProgressBar progressBar;
    ImageView pro;
    TextView username,Email;
    Button save;
    Home_items1 homeItems1;
    int pos;
    Button logout;
    FirebaseAuth auth=FirebaseAuth.getInstance();
    final private DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("images").child("img");
    final private DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("user").child(auth.getUid());
    final private StorageReference storageReference=FirebaseStorage.getInstance().getReference();

    Uri profiluri;
  public ProfileFragment() {
        // Required empty public constructor
    }



    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        progressBar=view.findViewById(R.id.prograssBar);
        pro=view.findViewById(R.id.profile22);
        pro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),send_images.class);
                startActivity(intent);
            }
        });
        save=view.findViewById(R.id.editsave);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (profiluri != null){
                    uploadTofirestore(profiluri);

                }else {
                    Toast.makeText(getActivity(),"please select image",Toast.LENGTH_SHORT).show();
                }
            }
        });


        logout=view.findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent=new Intent(getActivity(),login69.class);
                startActivity(intent);
                getActivity().finish();

            }
        });
        profileImage=view.findViewById(R.id.Profile_Image);
        username=view.findViewById(R.id.profile_username);
        Email=view.findViewById(R.id.profile_email);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String profile=snapshot.child("imageuri").getValue().toString();
                Picasso.get().load(profile).into(profileImage);
                String userName1=snapshot.child("username").getValue().toString();
                username.setText(userName1);
                String email1=snapshot.child("email").getValue().toString();
                Email.setText(email1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select"),100);

            }

        });



        // Inflate the layout for this fragment
        return view;
    }

    private void uploadTofirestore(Uri uri) {
      final StorageReference imagereffarence=storageReference.child(auth.getUid());
      imagereffarence.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
          @Override
          public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
              imagereffarence.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                  @Override
                  public void onSuccess(Uri uri) {
                      String imagage=uri.toString();
                      reference.child("imageuri").setValue(imagage);
                      progressBar.setVisibility(View.INVISIBLE);
                      Toast.makeText(getActivity(),"profile Updated",Toast.LENGTH_SHORT).show();
                  }
              });
          }
      }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
          @Override
          public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
              progressBar.setVisibility(View.VISIBLE);
          }
      }).addOnFailureListener(new OnFailureListener() {
          @Override
          public void onFailure(@NonNull Exception e) {
              progressBar.setVisibility(View.INVISIBLE);
              Toast.makeText(getActivity(),"error"+e.getMessage(),Toast.LENGTH_SHORT).show();
          }
      });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100){
            if (data!=null){
                profiluri=data.getData();
                profileImage.setImageURI(profiluri);
                uploadTofirestore(profiluri);

            }

        }
    }



    private String getFileExtension(Uri fileuri)
    {
        ContentResolver contentResolver=getContext().getContentResolver();
        MimeTypeMap map=MimeTypeMap.getSingleton();
        return map.getExtensionFromMimeType(contentResolver.getType(fileuri));

    }
}