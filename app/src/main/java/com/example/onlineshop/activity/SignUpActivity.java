package com.example.onlineshop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.onlineshop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference db;
    private EditText etEmailSignUp, etPasswordSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference();
        mAuth.getFirebaseAuthSettings().setAppVerificationDisabledForTesting(true);
        mAuth.getFirebaseAuthSettings().forceRecaptchaFlowForTesting(false);
        etEmailSignUp = findViewById(R.id.etEmailSignUp);
        etPasswordSignUp = findViewById(R.id.etPasswordSignUp);
        Button btnSignUp = findViewById(R.id.btnSignUp);
        TextView tvSwitchToSignIn = findViewById(R.id.tvSwitchToSignIn);

        btnSignUp.setOnClickListener(view -> signUpUser());

        tvSwitchToSignIn.setOnClickListener(view -> {
            startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
            finish();
        });
    }

    private void signUpUser() {
        String email = etEmailSignUp.getText().toString().trim();
        String password = etPasswordSignUp.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            etEmailSignUp.setError("Email is required.");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            etPasswordSignUp.setError("Password is required.");
            return;
        }
        if (password.length() < 6) {
            etPasswordSignUp.setError("Password must be at least 6 characters.");
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        assignRoleToUser(mAuth.getCurrentUser().getUid(),"user");
                        assignStatusToUser(mAuth.getCurrentUser().getUid(), "true");
                        assignAddressToUser(mAuth.getCurrentUser().getUid(),"Hanoi");
                        assignPhoneNumberToUser(mAuth.getCurrentUser().getUid(),"+84");
                        assignEmailToUser(mAuth.getCurrentUser().getUid(), mAuth.getCurrentUser().getEmail());
                        assignNameToUser(mAuth.getCurrentUser().getUid(),"New User!");
                        Toast.makeText(SignUpActivity.this, "Sign-Up Successful!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
                        finish();
                    } else {
                        Toast.makeText(SignUpActivity.this, "Sign-Up Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void assignRoleToUser(String uid, String role) {
        // Store the role in Realtime Database
        db.child("users").child(uid).child("roles").setValue(role)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("SignUpActivity", "User role assigned successfully");
                    } else {
                        Log.e("SignUpActivity", "Error assigning role: " + task.getException().getMessage());
                    }
                });
    }
    private void assignEmailToUser(String uid, String email) {
        // Store the role in Realtime Database
        db.child("users").child(uid).child("email").setValue(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("SignUpActivity", "User email assigned successfully");
                    } else {
                        Log.e("SignUpActivity", "Error assigning email: " + task.getException().getMessage());
                    }
                });
    }
    private void assignNameToUser(String uid, String name) {
        // Store the role in Realtime Database
        db.child("users").child(uid).child("name").setValue(name)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("SignUpActivity", "User name assigned successfully");
                    } else {
                        Log.e("SignUpActivity", "Error assigning name: " + task.getException().getMessage());
                    }
                });
    }
    private void assignStatusToUser(String uid, String status){
        db.child("users").child(uid).child("status").setValue(status)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("SignUpActivity", "User status assigned successfully");
                    } else {
                        Log.e("SignUpActivity", "Error assigning status: " + task.getException().getMessage());
                    }
                });
    }
    private void assignAddressToUser(String uid, String address) {
        // Store the role in Realtime Database
        db.child("users").child(uid).child("address").setValue(address)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("SignUpActivity", "User address assigned successfully");
                    } else {
                        Log.e("SignUpActivity", "Error assigning address: " + task.getException().getMessage());
                    }
                });
    }
    private void assignPhoneNumberToUser(String uid, String phone) {
        // Store the role in Realtime Database
        db.child("users").child(uid).child("phone").setValue(phone)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("SignUpActivity", "User phone assigned successfully");
                    } else {
                        Log.e("SignUpActivity", "Error assigning phone: " + task.getException().getMessage());
                    }
                });
    }
}

