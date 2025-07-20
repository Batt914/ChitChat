package com.example.chitchat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

public class MainActivity extends AppCompatActivity {
    private int selectedTab = 1;
    private FirebaseAuth auth;

//    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        final LinearLayout homeLayout = findViewById(R.id.Home_Layout);
        final LinearLayout groupLayout = findViewById(R.id.Group_Layout);
        final LinearLayout notificationLayout = findViewById(R.id.Notification_Layout);
        final LinearLayout profileLayout = findViewById(R.id.Profile_Layout);

        final ImageView homeImage = findViewById(R.id.Home_Image);
        final ImageView groupImage = findViewById(R.id.Group_Image);
        final ImageView notificationImage = findViewById(R.id.Notification_Image);
        final ImageView profileImage = findViewById(R.id.Profile_Image);

        final TextView homeText = findViewById(R.id.Home_Text);
        final TextView groupText = findViewById(R.id.Group_Text);
        final TextView notificationText = findViewById(R.id.Notification_Text);
        final TextView profileText = findViewById(R.id.Profile_Text);

        // set home fragment as default
        getSupportFragmentManager().beginTransaction().setReorderingAllowed(true).replace(R.id.fragment_container, new HomeFragment()).commit();


        homeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedTab != 1) {
                    getSupportFragmentManager().beginTransaction().setReorderingAllowed(true).replace(R.id.fragment_container, new HomeFragment()).commit();

                    groupImage.setImageResource(R.drawable.group);
                    notificationImage.setImageResource(R.drawable.notification);
                    profileImage.setImageResource(R.drawable.profile);


                    groupText.setVisibility(View.GONE);
                    notificationText.setVisibility(View.GONE);
                    profileText.setVisibility(View.GONE);


                    groupLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    notificationLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    profileLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));


                    homeImage.setImageResource(R.drawable.home2);
                    homeText.setVisibility(View.VISIBLE);
                    homeLayout.setBackgroundResource(R.drawable.lavender_boder);

                    // crete animatiom
                    ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f, 1.0f, 1f, 1f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
                    scaleAnimation.setDuration(200);
                    scaleAnimation.setFillAfter(true);
                    homeLayout.startAnimation(scaleAnimation);
                    selectedTab = 1;


                }
            }
        });

        groupLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedTab != 2) {

                    getSupportFragmentManager().beginTransaction().setReorderingAllowed(true).replace(R.id.fragment_container, new GroupFragment()).commit();

                    homeImage.setImageResource(R.drawable.home2);
                    notificationImage.setImageResource(R.drawable.notification);
                    profileImage.setImageResource(R.drawable.profile);


                    homeText.setVisibility(View.GONE);
                    notificationText.setVisibility(View.GONE);
                    profileText.setVisibility(View.GONE);


                    homeLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    notificationLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    profileLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));


                    groupImage.setImageResource(R.drawable.group);
                    groupText.setVisibility(View.VISIBLE);
                    groupLayout.setBackgroundResource(R.drawable.lavender_boder);

                    // crete animatiom
                    ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f, 1.0f, 1f, 1f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
                    scaleAnimation.setDuration(200);
                    scaleAnimation.setFillAfter(true);
                    groupLayout.startAnimation(scaleAnimation);
                    selectedTab = 2;


                }

            }

        });

        notificationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (selectedTab != 3) {
                    getSupportFragmentManager().beginTransaction().setReorderingAllowed(true).replace(R.id.fragment_container, new NotificationFrament()).commit();

                    groupImage.setImageResource(R.drawable.group);
                    homeImage.setImageResource(R.drawable.home2);
                    profileImage.setImageResource(R.drawable.profile);


                    groupText.setVisibility(View.GONE);
                    homeText.setVisibility(View.GONE);
                    profileText.setVisibility(View.GONE);


                    groupLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    homeLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    profileLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));


                    notificationImage.setImageResource(R.drawable.notification);
                    notificationText.setVisibility(View.VISIBLE);
                    notificationLayout.setBackgroundResource(R.drawable.lavender_boder);

                    // crete animatiom
                    ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f, 1.0f, 1f, 1f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
                    scaleAnimation.setDuration(200);
                    scaleAnimation.setFillAfter(true);
                    notificationLayout.startAnimation(scaleAnimation);
                    selectedTab = 3;


                }

            }
        });

        profileLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (selectedTab != 4) {
                    getSupportFragmentManager().beginTransaction().setReorderingAllowed(true).replace(R.id.fragment_container, new ProfileFragment()).commit();

                    groupImage.setImageResource(R.drawable.group);
                    notificationImage.setImageResource(R.drawable.notification);
                    homeImage.setImageResource(R.drawable.home2);


                    groupText.setVisibility(View.GONE);
                    notificationText.setVisibility(View.GONE);
                    homeText.setVisibility(View.GONE);


                    groupLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    notificationLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    homeLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));


                    profileImage.setImageResource(R.drawable.profile);
                    profileText.setVisibility(View.VISIBLE);
                    profileLayout.setBackgroundResource(R.drawable.lavender_boder);

                    // crete animatiom
                    ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f, 1.0f, 1f, 1f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
                    scaleAnimation.setDuration(200);
                    scaleAnimation.setFillAfter(true);
                    profileLayout.startAnimation(scaleAnimation);
                    selectedTab = 4;


                }
            }
        });

//        auth = FirebaseAuth.getInstance();
//        if (auth.getCurrentUser() != null) {
//
//            Intent intent = new Intent(MainActivity.this, login69.class);
//            startActivity(intent);
//            finish();
//
//        }
    }
}