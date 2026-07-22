package com.example.barberapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail;
    private EditText etPassword;

    private ImageView ivEyeToggle;

    private Button btnLogin;
    private Button btnFacebook;
    private Button btnGoogle;
    private Button btnGithub;

    private TextView tvForgotPassword;
    private TextView tvSignUp;

    private boolean passwordVisible = false;

    private UserRepository userRepository;

    @Override
    protected void onCreate(
            Bundle savedInstanceState
    ) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.login
        );

        // Initialize repository
        userRepository =
                new UserRepository(this);

        // Find views
        etEmail =
                findViewById(R.id.etEmail);

        etPassword =
                findViewById(R.id.etPassword);

        ivEyeToggle =
                findViewById(R.id.ivEyeToggle);

        btnLogin =
                findViewById(R.id.btnLogin);

        btnFacebook =
                findViewById(R.id.btnFacebook);

        btnGoogle =
                findViewById(R.id.btnGoogle);

        btnGithub =
                findViewById(R.id.btnGithub);

        tvForgotPassword =
                findViewById(R.id.tvForgotPassword);

        tvSignUp =
                findViewById(R.id.tvSignUp);

        // Password eye toggle
        setupPasswordToggle();

        // Login button
        btnLogin.setOnClickListener(
                v -> attemptLogin()
        );

        // Forgot password
        tvForgotPassword.setOnClickListener(v ->
                Toast.makeText(
                        this,
                        "Forgot password — coming soon!",
                        Toast.LENGTH_SHORT
                ).show()
        );

        // Facebook
        btnFacebook.setOnClickListener(v ->
                Toast.makeText(
                        this,
                        "Facebook login — coming soon!",
                        Toast.LENGTH_SHORT
                ).show()
        );

        // Google
        btnGoogle.setOnClickListener(v ->
                Toast.makeText(
                        this,
                        "Google login — coming soon!",
                        Toast.LENGTH_SHORT
                ).show()
        );

        // GitHub
        btnGithub.setOnClickListener(v ->
                Toast.makeText(
                        this,
                        "GitHub login — coming soon!",
                        Toast.LENGTH_SHORT
                ).show()
        );

        // Sign Up
        tvSignUp.setOnClickListener(v -> {

            Intent intent = new Intent(
                    LoginActivity.this,
                    SignUpActivity.class
            );

            startActivity(intent);

            overridePendingTransition(
                    R.anim.slide_up,
                    R.anim.stay
            );
        });
    }

    // ============================================================
    // PASSWORD TOGGLE
    // ============================================================

    private void setupPasswordToggle() {

        ivEyeToggle.setOnClickListener(v -> {

            passwordVisible =
                    !passwordVisible;

            if (passwordVisible) {

                etPassword.setTransformationMethod(
                        HideReturnsTransformationMethod
                                .getInstance()
                );

                ivEyeToggle.setImageResource(
                        R.drawable.ic_eye_off
                );

            } else {

                etPassword.setTransformationMethod(
                        PasswordTransformationMethod
                                .getInstance()
                );

                ivEyeToggle.setImageResource(
                        R.drawable.ic_eye
                );
            }

            etPassword.setSelection(
                    etPassword.getText().length()
            );
        });
    }

    // ============================================================
    // LOGIN
    // ============================================================

    private void attemptLogin() {

        String email =
                etEmail.getText()
                        .toString()
                        .trim();

        String password =
                etPassword.getText()
                        .toString();

        // Validate email
        if (email.isEmpty()) {

            etEmail.setError(
                    "Email is required"
            );

            etEmail.requestFocus();

            return;
        }

        // Validate email format
        if (!android.util.Patterns.EMAIL_ADDRESS
                .matcher(email)
                .matches()) {

            etEmail.setError(
                    "Enter a valid email"
            );

            etEmail.requestFocus();

            return;
        }

        // Validate password
        if (password.isEmpty()) {

            etPassword.setError(
                    "Password is required"
            );

            etPassword.requestFocus();

            return;
        }

        // Login
        UserRepository.LoginResult result =
                userRepository.login(
                        email,
                        password
                );

        switch (result) {

            case SUCCESS:

                Toast.makeText(
                        this,
                        "Login successful!",
                        Toast.LENGTH_SHORT
                ).show();

                goToHome();

                break;

            case WRONG_PASSWORD:

                etPassword.setError(
                        "Incorrect password"
                );

                etPassword.requestFocus();

                break;

            case USER_NOT_FOUND:

                etEmail.setError(
                        "No account found with this email"
                );

                etEmail.requestFocus();

                break;

            default:

                Toast.makeText(
                        this,
                        "Login failed. Please try again.",
                        Toast.LENGTH_SHORT
                ).show();

                break;
        }
    }

    // ============================================================
    // LOGIN SUCCESS → HOME
    // ============================================================

    private void goToHome() {

        Intent intent = new Intent(
                LoginActivity.this,
                HomeActivity.class
        );

        // Remove LoginActivity from back stack
        intent.setFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK
                        |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK
        );

        startActivity(intent);

        overridePendingTransition(
                R.anim.slide_up,
                R.anim.stay
        );

        finish();
    }
}