package com.example.onlineshop.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.onlineshop.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class UserProfileActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText etUserId, etUserEmail, etUserName, etUserPhone, etUserPhotoUrl; // Changed to EditText
    private TextView tvEmailVerified;
    private Button btnEdit, btnSignOut;
    private SharedPreferences sharedPreferences;
    private boolean isEditing = false; // Track editing state

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile); // Ensure you set the correct layout
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Log.i("User Role",this.getRoleFromSharedPrefs());
        // Initialize UI elements
        etUserId = findViewById(R.id.etUserId);
        etUserEmail = findViewById(R.id.etUserEmail);
        etUserName = findViewById(R.id.etUserName);
        tvEmailVerified = findViewById(R.id.tvEmailVerified);
        etUserPhotoUrl = findViewById(R.id.etUserPhotoUrl);
        btnEdit = findViewById(R.id.btnEdit); // Add edit button
        btnSignOut = findViewById(R.id.btnSignOut);

        // Display user details
        if (currentUser != null) {
            Log.i("Users",mAuth.getCurrentUser().getEmail());
            etUserId.setText(currentUser.getUid());
            etUserEmail.setText(currentUser.getEmail());
            etUserName.setText(currentUser.getDisplayName() != null ? currentUser.getDisplayName() : "N/A");
            etUserPhotoUrl.setText(currentUser.getPhotoUrl() != null ? currentUser.getPhotoUrl().toString() : "N/A");
            tvEmailVerified.setText("User Role: " + getRoleFromSharedPrefs());
        } else {
            Toast.makeText(this, "User not found!", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Edit button functionality
        btnEdit.setOnClickListener(v -> {
            if (!isEditing) {
                // Enable editing
                etUserName.setFocusableInTouchMode(true);
                etUserPhone.setFocusableInTouchMode(true);
                etUserPhotoUrl.setFocusableInTouchMode(true);
                btnEdit.setText("Save");
            } else {
                // Save changes
                String name = etUserName.getText().toString();
                String photo = etUserPhotoUrl.getText().toString();

                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .setPhotoUri(Uri.parse(photo))
                        .build();

                if(currentUser!=null){
                    currentUser.updateProfile(profileUpdates)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Log.d("FirebaseUserUpdate", "User profile updated.");
                                } else {
                                    Log.e("FirebaseUserUpdate", "Profile update failed.", task.getException());
                                }
                            });
                }

                // Disable editing
                etUserName.setFocusable(false);
                btnEdit.setText("Edit");

                Toast.makeText(this, "Changes saved!", Toast.LENGTH_SHORT).show();
            }
            isEditing = !isEditing; // Toggle editing state
        });

        // Sign out button functionality
        btnSignOut.setOnClickListener(v -> {
            mAuth.signOut();
            Toast.makeText(UserProfileActivity.this, "Signed out successfully", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(UserProfileActivity.this, SignInActivity.class));
            finish();
        });
    }
    public String getRoleFromSharedPrefs() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String role = sharedPreferences.getString("userRole", "user"); // "user" is the default value
        Log.d("SharedPrefsRole", "Retrieved user role: " + role);
        return role;
    }
}
