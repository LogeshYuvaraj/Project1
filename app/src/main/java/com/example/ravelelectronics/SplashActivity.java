package com.example.ravelelectronics;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ravelelectronics.util.SessionManagement;

public class SplashActivity extends AppCompatActivity {
    private final int SPLASH_DISPLAY_LENGTH = 2000;
    SessionManagement session;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        session = new SessionManagement(getApplicationContext());
        Thread splashThread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(SPLASH_DISPLAY_LENGTH);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    finish();
                    if (session.isLoggedIn()) {
                        Intent i = new Intent(SplashActivity.this, DashboardActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                        finish();
                    }
                }
            }
        };
        splashThread.start();
    }
}


