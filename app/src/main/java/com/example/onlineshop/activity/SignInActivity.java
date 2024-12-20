package com.example.onlineshop.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.onlineshop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignInActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText etEmailSignIn, etPasswordSignIn;
    private DatabaseReference db;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference();
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        mAuth.getFirebaseAuthSettings().setAppVerificationDisabledForTesting(true);
        mAuth.getFirebaseAuthSettings().forceRecaptchaFlowForTesting(false);
        etEmailSignIn = findViewById(R.id.etEmailSignIn);
        etPasswordSignIn = findViewById(R.id.etPasswordSignIn);
        Button btnSignIn = findViewById(R.id.btnSignIn);
        TextView tvSwitchToSignUp = findViewById(R.id.tvSwitchToSignUp);
        TextView tvForgotPassword = findViewById(R.id.tvForgotPassword);



        btnSignIn.setOnClickListener(view -> signInUser());
        tvForgotPassword.setOnClickListener(view -> showForgotPasswordDialog());
        tvSwitchToSignUp.setOnClickListener(view -> {
            startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
            finish();
        });
    }

    private void signInUser() {
        String email = etEmailSignIn.getText().toString().trim();
        String password = etPasswordSignIn.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            etEmailSignIn.setError("Email is required.");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            etPasswordSignIn.setError("Password is required.");
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(SignInActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                        DatabaseReference roleRef = db.child("users").child(mAuth.getCurrentUser().getUid());
                        roleRef.get().addOnCompleteListener(task2 -> {
                            if (task2.isSuccessful()) {
                                DataSnapshot dataSnapshot = task2.getResult();
                                if (dataSnapshot.exists()) {
                                    // Retrieve the role value
                                    String role = dataSnapshot.getValue(String.class); // Assuming role is a String
                                    Log.d("FirebaseRole", "User role is: " + role);

                                    // Save the role in Shared Preferences
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("userRole", role); // Store the role with the key "userRole"
                                    editor.apply(); // Commit the changes
                                } else {
                                    Log.d("FirebaseRole", "Role does not exist in the database.");
                                }
                            } else {
                                Log.e("FirebaseRole", "Error getting data: ", task2.getException());
                            }
                        });
                        try {
                            Thread.sleep(1000); //Role take time to fetch
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        startActivity(new Intent(SignInActivity.this, UserProfileActivity.class));
                        finish();
                    } else {
                        Toast.makeText(SignInActivity.this, "Login Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showForgotPasswordDialog() {
        // Create an AlertDialog to prompt the user for their email
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Reset Password");

        // Set up the input
        final EditText input = new EditText(this);
        input.setHint("Enter your email");
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("Send", (dialog, which) -> {
            String email = input.getText().toString().trim();
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(SignInActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();
            } else {
                sendPasswordResetEmail(email);
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void sendPasswordResetEmail(String email) {
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(SignInActivity.this, "Reset link sent to your email", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(SignInActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

