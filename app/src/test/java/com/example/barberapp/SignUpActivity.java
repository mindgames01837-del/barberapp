package com.example.barberapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        // TODO: wire up sign_up.xml views here once IDs are shared,
        // e.g. a "Create account" button, back arrow, or "Already have
        // an account? Log in" link back to MainActivity.
    }

    @Override
    public void finish() {
        super.finish();
        // Reverse animation when leaving this screen (e.g. back button)
        overridePendingTransition(R.anim.stay, R.anim.slide_up);
    }
}