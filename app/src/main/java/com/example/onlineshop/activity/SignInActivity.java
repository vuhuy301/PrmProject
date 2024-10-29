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

public class SignInActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText etEmailSignIn, etPasswordSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mAuth = FirebaseAuth.getInstance();
        mAuth.getFirebaseAuthSettings().setAppVerificationDisabledForTesting(true);
        mAuth.getFirebaseAuthSettings().forceRecaptchaFlowForTesting(false);
        etEmailSignIn = findViewById(R.id.etEmailSignIn);
        etPasswordSignIn = findViewById(R.id.etPasswordSignIn);
        Button btnSignIn = findViewById(R.id.btnSignIn);
        TextView tvSwitchToSignUp = findViewById(R.id.tvSwitchToSignUp);

        btnSignIn.setOnClickListener(view -> signInUser());

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
                        startActivity(new Intent(SignInActivity.this, MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(SignInActivity.this, "Login Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

