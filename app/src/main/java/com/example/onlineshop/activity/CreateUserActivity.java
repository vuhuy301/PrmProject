package com.example.onlineshop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.onlineshop.R;
import com.example.onlineshop.model.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateUserActivity extends AppCompatActivity {
    private EditText editTextUserName, editTextUserEmail, editTextUserPassword;
    private Spinner spinnerRole;
    private Button buttonCreateUser, buttonCancel;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        editTextUserName = findViewById(R.id.editTextUserName);
        editTextUserEmail = findViewById(R.id.editTextUserEmail);
        editTextUserPassword = findViewById(R.id.editTextUserPassword);
        spinnerRole = findViewById(R.id.spinnerRole);
        buttonCreateUser = findViewById(R.id.buttonCreateUser);
        buttonCancel = findViewById(R.id.buttonCancel);

        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        // Thiết lập spinner với các giá trị role
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.user_roles, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRole.setAdapter(adapter);

        buttonCreateUser.setOnClickListener(v -> createUser());

        buttonCancel.setOnClickListener(v -> finish());


        ImageView backImageView = findViewById(R.id.imageView8);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Quay lại admin_dashboard
                Intent intent = new Intent(CreateUserActivity.this, AdminActivity.class);
                startActivity(intent);
                finish(); // Để không giữ lại activity hiện tại trên stack
            }
        });

    }

    private void createUser() {
        String name = editTextUserName.getText().toString().trim();
        String email = editTextUserEmail.getText().toString().trim();
        String password = editTextUserPassword.getText().toString().trim();
        String role = spinnerRole.getSelectedItem().toString(); // Lấy giá trị role từ spinner

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        String userId = databaseReference.push().getKey();

        User user = new User(userId,name, email, password, null, null, true, role);
        databaseReference.child(userId).setValue(user)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(CreateUserActivity.this, "User created successfully", Toast.LENGTH_SHORT).show();
                        finish(); // Quay lại activity trước đó
                    } else {
                        Toast.makeText(CreateUserActivity.this, "Failed to create user", Toast.LENGTH_SHORT).show();
                    }
                });

//        databaseReference.push().setValue(user)
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        Toast.makeText(CreateUserActivity.this, "User created successfully", Toast.LENGTH_SHORT).show();
//
//                        finish(); // Quay lại activity trước đó
//                    } else {
//                        Toast.makeText(CreateUserActivity.this, "Failed to create user", Toast.LENGTH_SHORT).show();
//                    }
//                });
//        DatabaseReference newUserRef = databaseReference.push(); // Tạo tham chiếu mới với ID tự động
//
//        newUserRef.setValue(user).addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                // Đã thêm người dùng thành công
//                Toast.makeText(CreateUserActivity.this, "User created successfully", Toast.LENGTH_SHORT).show();
//                // Quay lại AdminActivity
//                finish();
//            } else {
//                // Thông báo lỗi
//                Toast.makeText(CreateUserActivity.this, "Failed to create user", Toast.LENGTH_SHORT).show();
//            }
//        });
    }
}
