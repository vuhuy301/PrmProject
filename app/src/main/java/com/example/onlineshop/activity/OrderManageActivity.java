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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderManageActivity extends AppCompatActivity {
    private RecyclerView recyclerViewOrders;
    private OrderAdapter orderAdapter;
    private List<Order> orderList;
    private Map<String, String> orderIdMap = new HashMap<>();
    private DatabaseReference databaseReference;

    private String id;

    private String userRole;

    private String[] orderStatuses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_order);
        userRole = "shipper";
        Spinner statusSpinner = findViewById(R.id.statusSpinner);
        if ("admin".equals(userRole)) {
            orderStatuses = new String[]{"Tất cả", "Chưa xác nhận", "Đã xác nhận", "Đang giao", "Giao thành công", "Đã hủy", "Giao không thành công"};
        } else if ("shipper".equals(userRole)) {
            orderStatuses = new String[]{"Tất cả", "Đã xác nhận", "Đang giao", "Giao thành công"};
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, orderStatuses);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(adapter);


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

        orderAdapter.setOnItemClickListener((order) -> {
            String id = orderIdMap.get(order.getOrderId());
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

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orderList.clear();
                orderIdMap.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    id = snapshot.getKey();
                    Order order = snapshot.getValue(Order.class);
                    String id = snapshot.getKey();
                    if ("admin".equals(userRole)) {
                        orderList.add(order);
                        orderIdMap.put(order.getOrderId(), id);
                    } else if ("shipper".equals(userRole)) {
                        if ("Đã xác nhận".equals(order.getStatus()) ||
                                "Đang giao".equals(order.getStatus()) ||
                                "Đã giao".equals(order.getStatus())) {
                            orderList.add(order);
                            orderIdMap.put(order.getOrderId(), id);
                        }
                    }
                }
                orderAdapter.notifyDataSetChanged();
                filterOrdersByStatus(getSelectedStatus(statusSpinner));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
        List<Order> filteredOrders = new ArrayList<>();
        for (Order order : orderList) {
            if (status.equals("Tất cả") || order.getStatus().equals(status)) {
                filteredOrders.add(order);
            }
        }
        orderAdapter.updateOrders(filteredOrders);
    }
    private String getSelectedStatus(Spinner spinner) {
        return spinner.getSelectedItem().toString();

    }
}
