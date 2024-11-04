package com.example.onlineshop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

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
    private String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_order);

        Spinner statusSpinner = findViewById(R.id.statusSpinner);
        String[] orderStatuses = {"Tất cả", "Chưa xác nhận", "Đã xác nhận", "Đang giao", "Giao thành công"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, orderStatuses);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(adapter);

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
            intent.putExtra("orderItems", (Serializable) order.getItems());
            intent.putExtra("id", id);
            startActivity(intent);
        });

        databaseReference = FirebaseDatabase.getInstance().getReference("order");

        // Đọc dữ liệu từ Firebase
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orderList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    id = snapshot.getKey();
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
        statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedStatus = orderStatuses[position];
                filterOrdersByStatus(selectedStatus);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        ImageView backButton = findViewById(R.id.imageView4);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
    private void filterOrdersByStatus(String status) {
        // Bộ lọc danh sách đơn hàng theo trạng thái
        List<Order> filteredOrders = new ArrayList<>();
        for (Order order : orderList) {
            if (status.equals("Tất cả") || order.getStatus().equals(status)) {
                filteredOrders.add(order);
            }
        }
        // Cập nhật Adapter với danh sách đã lọc
        orderAdapter.updateOrders(filteredOrders);
    }
}
