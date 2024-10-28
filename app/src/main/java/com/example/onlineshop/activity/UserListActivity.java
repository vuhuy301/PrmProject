package com.example.onlineshop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlineshop.R;
import com.example.onlineshop.adapter.UserAdapter;
import com.example.onlineshop.databinding.ActivityUserListBinding;
import com.example.onlineshop.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserListActivity extends AppCompatActivity {
    ActivityUserListBinding binding;
    RecyclerView recyclerView;
    UserAdapter adapter;
    ArrayList<User> userList;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        recyclerView = binding.recyclerViewUsers;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userList = new ArrayList<>();
        adapter = new UserAdapter(userList);
        recyclerView.setAdapter(adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        fetchUsers();

        ImageView backImageView = findViewById(R.id.imageView8);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Quay lại admin_dashboard
                Intent intent = new Intent(UserListActivity.this, AdminActivity.class);
                startActivity(intent);
                finish(); // Để không giữ lại activity hiện tại trên stack
            }
        });
    }

    private void fetchUsers() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    userList.add(user);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
