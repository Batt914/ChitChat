package com.example.chitchat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
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
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.play.core.integrity.au;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class login69 extends AppCompatActivity {

    TextView cretaeNew,forgotPass;
    TextInputEditText email,pass;
    ProgressBar progressBar;
    Button login;
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference reference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login69);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        cretaeNew=findViewById(R.id.RedirectSin);
        email=findViewById(R.id.emailLogin);
        pass=findViewById(R.id.passlogin);
        login=findViewById(R.id.login);
        forgotPass=findViewById(R.id.forgot);
        progressBar=findViewById(R.id.prograssBar11);

        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();





        cretaeNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(login69.this,SingUp.class);
                startActivity(intent);finish();

            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String e=email.getText().toString();
                String p=pass.getText().toString();
                if (e.isEmpty() || p.isEmpty()){
                    Toast.makeText(login69.this,"enter valid informatin",Toast.LENGTH_SHORT).show();
                }else {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    progressBar.setVisibility(View.VISIBLE);
                    login.setVisibility(View.GONE);
                    auth.signInWithEmailAndPassword(e,p).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressBar.setVisibility(View.GONE);
                            login.setVisibility(View.VISIBLE);
                            if (task.isSuccessful()){
                                Intent intent=new Intent(login69.this,MainActivity.class);
                                startActivity(intent);
                                finish();

                            }else {
                                Toast.makeText(login69.this,"error"+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }


            }
        });
        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(login69.this, ForgotPasword.class);
                startActivity(intent);
            }
        });
        if (user !=null){
            Intent intent=new Intent(login69.this,MainActivity.class);
            startActivity(intent);
            finish();

        }



    }
}