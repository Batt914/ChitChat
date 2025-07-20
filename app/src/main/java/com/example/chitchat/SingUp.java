package com.example.chitchat;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.checkerframework.checker.units.qual.C;

import java.util.HashMap;

public class SingUp extends AppCompatActivity {

    EditText username1,email1,pass1,Cpass1;
    TextView login;
    ShapeableImageView profile;
    Button sing;
    FirebaseDatabase database;
    FirebaseAuth auth;
    DatabaseReference reference,databaseReference;
    FirebaseStorage storage;
    StorageReference storageReference;
    Uri imageURI,uri;
    String imageuri1;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sing_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        username1=findViewById(R.id.re_username);
        email1=findViewById(R.id.re_email);
        pass1=findViewById(R.id.re_pass);
        Cpass1=findViewById(R.id.re_repass);

        sing=findViewById(R.id.re_signBtn);
        login=findViewById(R.id.RedirectLogin);






        sing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database=FirebaseDatabase.getInstance();
                reference=database.getReference("user");
                auth=FirebaseAuth.getInstance();
                storageReference=FirebaseStorage.getInstance().getReference();


                String username = username1.getText().toString();
                String email=email1.getText().toString();
                String pass=pass1.getText().toString();
                String repass= Cpass1.getText().toString();



                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(email) || TextUtils.isEmpty(pass) || TextUtils.isEmpty(repass)){
                    Toast.makeText(SingUp.this,"enter the valid informetion ",Toast.LENGTH_SHORT).show();



                } if (!pass.trim().equals(repass.trim())) {
                    Toast.makeText(SingUp.this,"pass not equal repass",Toast.LENGTH_SHORT).show();
                }if(imageURI !=null) {
                    auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                final StorageReference imagereffarence=storageReference.child(System.currentTimeMillis()+getFileExtension(imageURI));
                                imagereffarence.putFile(imageURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        imagereffarence.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                imageuri1=uri.toString();
                                                String id=task.getResult().getUser().getUid();
                                                HelperClass helperClass=new HelperClass(username,email,pass,repass,id,imageuri1);
                                                reference.child(id).setValue(helperClass);

                                                Intent intent=new Intent(SingUp.this,MainActivity.class);
                                                startActivity(intent);
                                                finish();


                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                               Toast.makeText(SingUp.this,"error1"+e.getMessage(),Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(SingUp.this,"error"+e.getMessage(),Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }


                        }
                    });

                }else {
                    Toast.makeText(SingUp.this,"set profile",Toast.LENGTH_SHORT).show();
                }



            }
        });




        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SingUp.this,login69.class);
                startActivity(intent);
                finish();
            }
        });
        profile=findViewById(R.id.profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"),100);
            }
        });

    }
    private String getFileExtension(Uri uri)
    {
        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap map=MimeTypeMap.getSingleton();
        return map.getExtensionFromMimeType(contentResolver.getType(uri));

    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100){
            if (data!=null){
                imageURI=data.getData();
                profile.setImageURI(imageURI);

            }

        }
    }
}