package com.example.chitchat;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class ForgotPasword extends AppCompatActivity {
    FirebaseAuth auth;
    EditText Email_Address;
    Button Send_OTP;
    TextView Login, resd_OTP;;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        EdgeToEdge.enable( this );
        setContentView( R.layout.activity_forgot_pasword );

        Email_Address=findViewById(R.id.forgot_Email);
        Send_OTP=findViewById(R.id.Fogot_button);
        Login=findViewById(R.id.RedirectLogin);
        resd_OTP=findViewById(R.id.Send_OTP);

        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();

        Send_OTP.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=Email_Address.getText().toString();
                if(email.isEmpty()){
                    Email_Address.setError("Enter Email");
                    Email_Address.requestFocus();
                }
                else {
                    auth.sendPasswordResetEmail( email ).addOnCompleteListener( new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){

                                Toast.makeText( ForgotPasword.this, "Check your email", Toast.LENGTH_SHORT ).show();

                            }else {
                                Toast.makeText( ForgotPasword.this, "error"+task.getException().getMessage(), Toast.LENGTH_SHORT ).show();

                            }
                        }
                    } );
                }
            }
        });
    }
}