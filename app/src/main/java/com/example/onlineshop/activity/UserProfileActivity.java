package com.example.onlineshop.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.onlineshop.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserProfileActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText etUserId, etUserEmail, etUserName, etUserPhotoUrl, etUserAddress, etUserPhone; // Changed to EditText
    private TextView tvEmailVerified, tvStatus;
    private Button btnEdit, btnSignOut;
    private ImageView imageViewProfile;
    private DatabaseReference db;
    private SharedPreferences sharedPreferences;
    private boolean isEditing = false; // Track editing state

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile); // Ensure you set the correct layout
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        db = FirebaseDatabase.getInstance().getReference();
        // Initialize UI elements
        etUserId = findViewById(R.id.etUserId);
        etUserEmail = findViewById(R.id.etUserEmail);
        etUserName = findViewById(R.id.etUserName);
        etUserAddress = findViewById(R.id.etUserAddress);
        etUserPhone = findViewById(R.id.etUserPhone);
        tvEmailVerified = findViewById(R.id.tvEmailVerified);
        tvStatus = findViewById(R.id.tvStatus);
        etUserPhotoUrl = findViewById(R.id.etUserPhotoUrl);
        imageViewProfile = findViewById(R.id.imageViewProfile);
        btnEdit = findViewById(R.id.btnEdit); // Add edit button
        btnSignOut = findViewById(R.id.btnSignOut);

        // Display user details
        if (currentUser != null) {
            etUserId.setText(currentUser.getUid());
            etUserEmail.setText(currentUser.getEmail());
            etUserName.setText(currentUser.getDisplayName() != null ? currentUser.getDisplayName() : "N/A");
            etUserPhotoUrl.setText(currentUser.getPhotoUrl() != null ? currentUser.getPhotoUrl().toString() : "N/A");
            etUserPhone.setText(getPhoneFromSharedPrefs());
            etUserAddress.setText(getAddressFromSharedPrefs());
            tvEmailVerified.setText("User Role: " + getRoleFromSharedPrefs());
            tvStatus.setText("User Status: " + (getStatusFromSharedPrefs().equals("true")? "Active" : "Inactive"));
            if (currentUser.getPhotoUrl()!=null) {
                imageViewProfile.setVisibility(View.VISIBLE);
                Glide.with(this).load(currentUser.getPhotoUrl().toString()).into(imageViewProfile);
            } else {
                imageViewProfile.setVisibility(View.GONE); // Hide the ImageView if no URL
            }
        } else {
            Toast.makeText(this, "User not found!", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Edit button functionality
        btnEdit.setOnClickListener(v -> {
            if (!isEditing) {
                // Enable editing
                etUserName.setEnabled(true);
                etUserPhone.setEnabled(true);
                etUserAddress.setEnabled(true);
                etUserPhotoUrl.setEnabled(true);
                btnEdit.setText("Save");
            } else {
                // Save changes
                String name = etUserName.getText().toString();
                String photo = etUserPhotoUrl.getText().toString();
                String phone = etUserPhone.getText().toString();
                String address = etUserAddress.getText().toString();

                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .setPhotoUri(Uri.parse(photo))
                        .build();

                if(currentUser!=null){
                    assignPhoneNumberToUser(currentUser.getUid(),phone);
                    assignAddressToUser(currentUser.getUid(),address);
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
                etUserName.setEnabled(false);
                etUserPhone.setEnabled(false);
                etUserAddress.setEnabled(false);
                etUserPhotoUrl.setEnabled(false);

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
    public String getStatusFromSharedPrefs() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String status = sharedPreferences.getString("userStatus", "true"); // "user" is the default value
        Log.d("SharedPrefsStatus", "Retrieved user status: " + status);
        return status;
    }
    public String getAddressFromSharedPrefs() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String address = sharedPreferences.getString("userAddress", "Hanoi"); // "user" is the default value
        Log.d("SharedPrefsStatus", "Retrieved user address: " + address);
        return address;
    }

    public String getPhoneFromSharedPrefs() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String address = sharedPreferences.getString("userPhone", "+84"); // "user" is the default value
        Log.d("SharedPrefsStatus", "Retrieved user phone: " + address);
        return address;
    }

    private void assignAddressToUser(String uid, String address) {
        // Store the role in Realtime Database
        db.child("users").child(uid).child("address").setValue(address)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("UpdateProfile", "User address assigned successfully");
                    } else {
                        Log.e("UpdateProfile", "Error assigning address: " + task.getException().getMessage());
                    }
                });
    }
    private void assignPhoneNumberToUser(String uid, String phone) {
        // Store the role in Realtime Database
        db.child("users").child(uid).child("phone").setValue(phone)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("UpdateProfile", "User phone assigned successfully");
                    } else {
                        Log.e("UpdateProfile", "Error assigning phone: " + task.getException().getMessage());
                    }
                });
    }
}
