package com.example.onlineshop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ToggleButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.onlineshop.R;
import com.example.onlineshop.model.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditUserActivity extends AppCompatActivity {

    private ToggleButton toggleButtonActive;
    private Button buttonSave;
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        ImageView backImageView = findViewById(R.id.imageView8);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Quay lại activity_user_details

                Intent intent = new Intent(EditUserActivity.this, UserDetailActivity.class);
                startActivity(intent);
                finish(); // Để không giữ lại activity hiện tại trên stack
            }
        });
        toggleButtonActive = findViewById(R.id.toggleButtonActive);
        buttonSave = findViewById(R.id.buttonSave);

        // Nhận đối tượng User từ Intent
        user = (User) getIntent().getSerializableExtra("user");

        // Thiết lập trạng thái ban đầu cho ToggleButton
        if (user != null) {
            toggleButtonActive.setChecked(user.getIsActive());
        }

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cập nhật trạng thái mới
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(user.getName());
                databaseReference.child("isActive").setValue(toggleButtonActive.isChecked());
                finish(); // Quay lại activity trước đó
            }
        });
    }
}