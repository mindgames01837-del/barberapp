package com.example.barberapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        userRepository = new UserRepository(this);

        // ── Load logged-in user and greet them ────────────────────
        User user = userRepository.getLoggedInUser();
        if (user != null) {
            // Wire this to whatever greeting TextView you have in activity_home.xml
            // Example: TextView tvGreeting = findViewById(R.id.tvGreeting);
            //          tvGreeting.setText("Hey, " + user.getFullName() + "!");
        }

        // ── Logout example — wire to your logout button/menu item ─
        // btnLogout.setOnClickListener(v -> logout());
    }

    private void logout() {
        userRepository.logout();
        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.stay, R.anim.slide_down);
        finish();
    }
}