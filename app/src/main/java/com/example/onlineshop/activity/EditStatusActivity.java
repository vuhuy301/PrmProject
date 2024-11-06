package com.example.onlineshop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.onlineshop.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditStatusActivity extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private String userId;
    private Switch isActiveSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_status);

        ImageView backImageView = findViewById(R.id.imageView8);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Quay lại admin_dashboard
                Intent intent = new Intent(EditStatusActivity.this, UserListActivity.class);
                startActivity(intent);
                finish(); // Để không giữ lại activity hiện tại trên stack
            }
        });
        isActiveSwitch = findViewById(R.id.isActiveSwitch);
        userId = getIntent().getStringExtra("userId");
        boolean isActive = getIntent().getBooleanExtra("isActive", false);
        isActiveSwitch.setChecked(isActive);

        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        findViewById(R.id.saveButton).setOnClickListener(v -> {
            boolean newStatus = isActiveSwitch.isChecked();
            updateIsActiveStatus(userId, newStatus);
        });
    }

    private void updateIsActiveStatus(String userId, boolean newStatus) {
        databaseReference.child(userId).child("isActive").setValue(newStatus)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Status updated successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(this, "Failed to update status", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
