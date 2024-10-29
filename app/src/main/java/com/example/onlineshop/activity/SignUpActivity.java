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

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText etEmailSignUp, etPasswordSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
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
                        Toast.makeText(SignUpActivity.this, "Sign-Up Successful!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(SignUpActivity.this, "Sign-Up Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

