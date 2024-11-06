package com.example.onlineshop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlineshop.R;
import com.example.onlineshop.adapter.PopularProductAdapter;
import com.example.onlineshop.databinding.ActivityMainBinding;
import com.example.onlineshop.model.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    RecyclerView recyclerView;
    PopularProductAdapter adapter;
    ArrayList<Product> productList;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());

        recyclerView = binding.PopularView;
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        productList = new ArrayList<>();
        adapter = new PopularProductAdapter(productList);
        recyclerView.setAdapter(adapter);
        databaseReference = FirebaseDatabase.getInstance().getReference("products");
        fetchProducts();

        setUpCategoryClickListeners();
//        setContentView(R.layout.list_order);
        binding.imageView69.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CartActivity.class);
            startActivity(intent);
        });
        binding.imageView66.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, OrderManageActivity.class); // Chuyển tới màn OrderListActivity
            startActivity(intent);
        });

        binding.categoryLayoutAll.setOnClickListener(v -> fetchProducts());
    }

    private void setUpCategoryClickListeners() {

        binding.categoryLayout1.setOnClickListener(v -> loadProductsByCategory("1"));
        binding.categoryLayout2.setOnClickListener(v -> loadProductsByCategory("2"));
        binding.categoryLayout3.setOnClickListener(v -> loadProductsByCategory("3"));
        binding.categoryLayout4.setOnClickListener(v -> loadProductsByCategory("4"));
    }

    private void loadProductsByCategory(String categoryId) {
        databaseReference.orderByChild("categoryId").equalTo(categoryId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Product product = snapshot.getValue(Product.class);
                    if (product != null) {
                        productList.add(product);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void fetchProducts() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Product product = snapshot.getValue(Product.class);
                    productList.add(product);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}