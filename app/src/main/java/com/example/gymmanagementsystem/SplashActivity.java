package com.example.gymmanagementsystem;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {
    boolean isLoggedIn;
    private static final int SPLASH_DELAY = 3000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);


        isLoggedIn = false;



        new Handler().postDelayed(()-> {
            Intent intent;
            if(isLoggedIn){
               intent = new Intent(SplashActivity.this, MainActivity.class);

            }else{
                intent = new Intent(SplashActivity.this, AddMemberActivity.class);

            }
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();

        }, SPLASH_DELAY);



    }
}