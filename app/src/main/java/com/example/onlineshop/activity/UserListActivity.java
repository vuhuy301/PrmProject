package com.example.onlineshop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

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
import java.util.List;

public class UserListActivity extends AppCompatActivity {
    ActivityUserListBinding binding;
    RecyclerView recyclerView;
    UserAdapter adapter;
    ArrayList<User> userList, filteredList;
    DatabaseReference databaseReference;
    EditText searchEditText;
    Spinner roleSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        recyclerView = binding.recyclerViewUsers;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        searchEditText = binding.searchEditText;
        roleSpinner = binding.roleSpinner;

        userList = new ArrayList<>();
        filteredList = new ArrayList<>();
        adapter = new UserAdapter(filteredList); // Hiển thị danh sách đã lọc
        recyclerView.setAdapter(adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        fetchUsers();

        // Cấu hình cho Spinner lọc vai trò
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.role_options, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleSpinner.setAdapter(spinnerAdapter);

        // Lắng nghe thay đổi của ô tìm kiếm
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterUsers();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Lắng nghe thay đổi của Spinner
        roleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterUsers();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        ImageView backImageView = findViewById(R.id.imageView8);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserListActivity.this, AdminActivity.class);
                startActivity(intent);
                finish();
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
                    if (user != null) {
                        userList.add(user);
                    }
                }
                filterUsers(); // Lọc ngay sau khi tải dữ liệu từ Firebase
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    // Hàm lọc user theo tên và vai trò
    private void filterUsers() {
        String searchText = searchEditText.getText().toString().toLowerCase();
        String selectedRole = roleSpinner.getSelectedItem().toString();

        filteredList.clear();
        for (User user : userList) {
            boolean matchesName = user.getName().toLowerCase().contains(searchText);
            boolean matchesRole = selectedRole.equals("All") || user.getRole().equals(selectedRole);

            if (matchesName && matchesRole) {
                filteredList.add(user);
            }
        }
        adapter.notifyDataSetChanged();
    }
}
