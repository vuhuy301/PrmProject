package com.example.onlineshop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.onlineshop.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserProfileActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextView tvUserId, tvUserEmail, tvUserName, tvEmailVerified;
    private Button btnSignOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        // Initialize UI elements
        tvUserId = findViewById(R.id.tvUserId);
        tvUserEmail = findViewById(R.id.tvUserEmail);
        tvUserName = findViewById(R.id.tvUserName);
        tvEmailVerified = findViewById(R.id.tvEmailVerified);
        btnSignOut = findViewById(R.id.btnSignOut);

        // Display user details
        if (currentUser != null) {
            tvUserId.setText("User ID: " + currentUser.getUid());
            tvUserEmail.setText("Email: " + currentUser.getEmail());
            tvUserName.setText("Name: " + (currentUser.getDisplayName() != null ? currentUser.getDisplayName() : "N/A"));
            tvEmailVerified.setText("Email Verified: " + (currentUser.isEmailVerified() ? "Yes" : "No"));
        } else {
            Toast.makeText(this, "User not found!", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Sign out button functionality
        btnSignOut.setOnClickListener(v -> {
            mAuth.signOut();
            Toast.makeText(UserProfileActivity.this, "Signed out successfully", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(UserProfileActivity.this, SignInActivity.class));
            finish();
        });
    }
}
