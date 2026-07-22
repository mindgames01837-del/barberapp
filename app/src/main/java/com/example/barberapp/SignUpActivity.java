package com.example.barberapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {

    private EditText etFullName;
    private EditText etEmail;
    private EditText etPassword;

    private Button btnCreateAccount;

    private TextView txtLogin;

    private LinearLayout successBanner;

    private ImageView ivTogglePassword;

    private View strengthBar1;
    private View strengthBar2;
    private View strengthBar3;
    private View strengthBar4;

    private boolean isPasswordVisible = false;

    private UserRepository userRepository;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.sign_up);

        // Initialize repository
        userRepository = new UserRepository(this);

        // Find views
        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        btnCreateAccount = findViewById(R.id.btnCreateAccount);

        txtLogin = findViewById(R.id.txtLogin);

        successBanner = findViewById(R.id.successBanner);

        ivTogglePassword = findViewById(
                R.id.ivTogglePassword
        );

        strengthBar1 = findViewById(R.id.strengthBar1);
        strengthBar2 = findViewById(R.id.strengthBar2);
        strengthBar3 = findViewById(R.id.strengthBar3);
        strengthBar4 = findViewById(R.id.strengthBar4);

        // Create Account button
        btnCreateAccount.setOnClickListener(
                v -> createAccount()
        );

        // Login link
        txtLogin.setOnClickListener(v -> {

            Intent intent = new Intent(
                    SignUpActivity.this,
                    LoginActivity.class
            );

            startActivity(intent);

            overridePendingTransition(
                    R.anim.slide_up,
                    R.anim.stay
            );
        });

        setupPasswordToggle();

        setupPasswordStrengthWatcher();
    }

    // ============================================================
    // CREATE ACCOUNT
    // ============================================================

    private void createAccount() {

        String fullName =
                etFullName.getText()
                        .toString()
                        .trim();

        String email =
                etEmail.getText()
                        .toString()
                        .trim();

        String password =
                etPassword.getText()
                        .toString()
                        .trim();

        // Validate full name
        if (fullName.isEmpty()) {

            etFullName.setError(
                    "Enter Full Name"
            );

            etFullName.requestFocus();

            return;
        }

        // Validate email
        if (email.isEmpty()) {

            etEmail.setError(
                    "Enter Email"
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
                    "Enter Password"
            );

            etPassword.requestFocus();

            return;
        }

        // Create User object
        User user = new User(
                fullName,
                email,
                password
        );

        // Register user
        boolean registered =
                userRepository.registerUser(user);

        // Registration failed
        if (!registered) {

            etEmail.setError(
                    "An account with this email already exists"
            );

            etEmail.requestFocus();

            Toast.makeText(
                    this,
                    "Email already registered. Try logging in instead.",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        // Registration successful
        Toast.makeText(
                this,
                "Account created successfully!",
                Toast.LENGTH_SHORT
        ).show();

        // Show success banner
        showSuccessBannerAndGoToLogin();
    }

    // ============================================================
    // SUCCESS → LOGIN
    // ============================================================

    private void showSuccessBannerAndGoToLogin() {

        // Show success banner
        successBanner.setVisibility(
                View.VISIBLE
        );

        // Wait 2.2 seconds
        new Handler(
                Looper.getMainLooper()
        ).postDelayed(() -> {

            Intent intent = new Intent(
                    SignUpActivity.this,
                    LoginActivity.class
            );

            startActivity(intent);

            overridePendingTransition(
                    R.anim.slide_up,
                    R.anim.stay
            );

            // Prevent returning to SignUpActivity
            finish();

        }, 2200);
    }

    // ============================================================
    // PASSWORD VISIBILITY
    // ============================================================

    private void setupPasswordToggle() {

        ivTogglePassword.setOnClickListener(v -> {

            isPasswordVisible =
                    !isPasswordVisible;

            int cursorPosition =
                    etPassword.getSelectionEnd();

            if (isPasswordVisible) {

                etPassword.setInputType(
                        EditorInfo.TYPE_CLASS_TEXT
                                | EditorInfo
                                .TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                );

                ivTogglePassword.setImageResource(
                        R.drawable.ic_eye_off
                );

            } else {

                etPassword.setInputType(
                        EditorInfo.TYPE_CLASS_TEXT
                                | EditorInfo
                                .TYPE_TEXT_VARIATION_PASSWORD
                );

                ivTogglePassword.setImageResource(
                        R.drawable.ic_eye
                );
            }

            if (cursorPosition >= 0) {

                etPassword.setSelection(
                        Math.min(
                                cursorPosition,
                                etPassword.length()
                        )
                );
            }
        });
    }

    // ============================================================
    // PASSWORD STRENGTH
    // ============================================================

    private void setupPasswordStrengthWatcher() {

        etPassword.addTextChangedListener(
                new TextWatcher() {

                    @Override
                    public void beforeTextChanged(
                            CharSequence s,
                            int start,
                            int count,
                            int after
                    ) {
                    }

                    @Override
                    public void onTextChanged(
                            CharSequence s,
                            int start,
                            int before,
                            int count
                    ) {

                        updateStrengthBar(
                                s.toString()
                        );
                    }

                    @Override
                    public void afterTextChanged(
                            Editable s
                    ) {
                    }
                }
        );
    }

    private void updateStrengthBar(
            String password
    ) {

        int score =
                calculatePasswordStrength(
                        password
                );

        View[] bars = {
                strengthBar1,
                strengthBar2,
                strengthBar3,
                strengthBar4
        };

        for (
                int i = 0;
                i < bars.length;
                i++
        ) {

            if (i < score) {

                bars[i].setBackgroundColor(
                        getStrengthColor(score)
                );

            } else {

                bars[i].setBackgroundColor(
                        0xFFE8E8E8
                );
            }
        }
    }

    private int calculatePasswordStrength(
            String password
    ) {

        if (password.isEmpty()) {
            return 0;
        }

        int score = 0;

        if (password.length() >= 8) {
            score++;
        }

        if (
                password.matches(".*[A-Z].*")
                        &&
                        password.matches(".*[a-z].*")
        ) {
            score++;
        }

        if (password.matches(".*\\d.*")) {
            score++;
        }

        if (
                password.matches(
                        ".*[!@#$%^&*(),.?\":{}|<>].*"
                )
        ) {
            score++;
        }

        return Math.max(
                score,
                1
        );
    }

    private int getStrengthColor(
            int score
    ) {

        switch (score) {

            case 1:
                return 0xFFE53935;

            case 2:
                return 0xFFF5A623;

            case 3:
                return 0xFF1E88E5;

            case 4:
                return 0xFF2E7D32;

            default:
                return 0xFFE8E8E8;
        }
    }
}