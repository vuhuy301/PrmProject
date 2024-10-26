package com.example.onlineshop.activity;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.onlineshop.R;
import com.example.onlineshop.model.User;

public class UserDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        TextView nameTextView = findViewById(R.id.nameTextView);
        TextView emailTextView = findViewById(R.id.emailTextView);
        TextView roleTextView = findViewById(R.id.roleTextView);
        TextView addressTextView = findViewById(R.id.addressTextView);
        TextView phoneTextView = findViewById(R.id.phoneTextView);
        TextView statusTextView = findViewById(R.id.statusTextView);

        // Nhận dữ liệu từ Intent
        User user = (User) getIntent().getSerializableExtra("user");

        if (user != null) {
            nameTextView.setText("Name: " + user.getName());
            emailTextView.setText("Email: " + user.getEmail());
            roleTextView.setText("Role: " + user.getRole());
            addressTextView.setText("Address: " + user.getAddress());
            phoneTextView.setText("Phone Number: " + user.getPhoneNumber());
            statusTextView.setText("Active Status: " + (user.getActive() ? "Active" : "Inactive"));
        }
    }
}
