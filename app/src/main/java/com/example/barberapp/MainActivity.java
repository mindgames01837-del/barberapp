package com.example.barberapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ── Session check: skip get-started if already logged in ──
        UserRepository repo = new UserRepository(this);
        if (repo.isLoggedIn()) {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.get_started);

        RelativeLayout btnGetStarted = findViewById(R.id.btn_get_started);
        btnGetStarted.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, SignUpActivity.class));
            overridePendingTransition(R.anim.slide_up, R.anim.stay);
        });

        TextView btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            overridePendingTransition(R.anim.slide_up, R.anim.stay);
        });
    }
}