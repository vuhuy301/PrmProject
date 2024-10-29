package com.example.onlineshop.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlineshop.R;
import com.example.onlineshop.adapter.OrderItemAdapter;
import com.example.onlineshop.model.OrderItem;

import java.util.List;

public class OrderDetailActivity extends AppCompatActivity {
    private TextView orderIdTextView;
    private TextView nameTextView;
    private TextView phoneTextView;
    private TextView addressTextView;
    private TextView orderDateTextView;
    private TextView orderStatusTextView;
    private TextView totalAmountTextView;
    private RecyclerView productsRecyclerView;
    private OrderItemAdapter orderItemAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_detail);

        orderIdTextView = findViewById(R.id.orderId);
        nameTextView = findViewById(R.id.name);
        phoneTextView = findViewById(R.id.phone);
        addressTextView = findViewById(R.id.address);
        orderDateTextView = findViewById(R.id.orderDate);
        orderStatusTextView = findViewById(R.id.orderStatus);
        totalAmountTextView = findViewById(R.id.textView10);
        productsRecyclerView = findViewById(R.id.productsRecyclerView);

        String orderId = getIntent().getStringExtra("orderId");
        String name = getIntent().getStringExtra("orderName");
        String phone = getIntent().getStringExtra("orderPhone");
        String address = getIntent().getStringExtra("orderSp");
        String orderDate = getIntent().getStringExtra("orderDate");
        String orderStatus = getIntent().getStringExtra("orderStatus");
        double totalAmount = getIntent().getDoubleExtra("totalAmount", 0.0);
        List<OrderItem> orderItems = (List<OrderItem>)getIntent().getSerializableExtra("orderItems");

        orderIdTextView.setText("Mã đơn hàng: " + orderId);
        nameTextView.setText("Khách hàng: " + name);
        phoneTextView.setText("Số điện thoại: " + phone);
        addressTextView.setText("Địa chỉ: " + address);
        orderDateTextView.setText("Ngày đặt hàng: " + orderDate);
        orderStatusTextView.setText("Trạng thái: " + orderStatus);
        totalAmountTextView.setText("Tổng: " + totalAmount + " Đ");

        productsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        orderItemAdapter = new OrderItemAdapter(orderItems);
        productsRecyclerView.setAdapter(orderItemAdapter);
    }
}
