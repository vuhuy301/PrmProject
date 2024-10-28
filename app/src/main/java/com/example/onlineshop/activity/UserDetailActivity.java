package com.example.onlineshop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
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

        ImageView backImageView = findViewById(R.id.imageView8);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Quay lại activity_user_list
                Intent intent = new Intent(UserDetailActivity.this, UserListActivity.class);
                startActivity(intent);
                finish(); // Để không giữ lại activity hiện tại trên stack
            }
        });

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
            statusTextView.setText("Active Status: " + (user.getIsActive() ? "Active" : "Inactive"));
        }
    }
}
