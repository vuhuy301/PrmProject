package com.example.onlineshop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlineshop.R;
import com.example.onlineshop.adapter.CategoryAdapter;
import com.example.onlineshop.adapter.OrderAdapter;
import com.example.onlineshop.model.Category;
import com.example.onlineshop.model.Order;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OrderManageActivity extends AppCompatActivity {
    private RecyclerView recyclerViewOrders;
    private OrderAdapter orderAdapter;
    private List<Order> orderList;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_order);

        recyclerViewOrders = findViewById(R.id.recyclerViewOrders);
        recyclerViewOrders.setLayoutManager(new LinearLayoutManager(this));

        orderList = new ArrayList<>();
        orderAdapter = new OrderAdapter(orderList);
        recyclerViewOrders.setAdapter(orderAdapter);

        orderAdapter.setOnItemClickListener(order -> {
            Intent intent = new Intent(OrderManageActivity.this, OrderDetailActivity.class);
            intent.putExtra("orderId", order.getOrderId());
            intent.putExtra("orderName", order.getName());
            intent.putExtra("orderPhone", order.getPhone());
            intent.putExtra("orderSp", order.getShippingAddress());
            intent.putExtra("orderStatus", order.getStatus());
            intent.putExtra("orderDate", order.getOrderDate());
            intent.putExtra("totalAmount", order.getTotalAmount());
            intent.putExtra("orderItems", (Serializable) order.getItems()); // truyền orderItems dưới dạng Serializable
            startActivity(intent);
        });

        databaseReference = FirebaseDatabase.getInstance().getReference("order");

        // Đọc dữ liệu từ Firebase
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orderList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Order order = snapshot.getValue(Order.class);
                    orderList.add(order);
                }
                orderAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi khi không thể đọc dữ liệu từ Firebase
            }
        });
    }
}
