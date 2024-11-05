package com.example.onlineshop.activity;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlineshop.R;
import com.example.onlineshop.adapter.OrderItemAdapter;
import com.example.onlineshop.model.OrderItem;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

    private DatabaseReference order;
    private String orderId;
    private String id;

    private String userRole;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_detail);
        userRole = "admin";
        orderIdTextView = findViewById(R.id.orderId);
        nameTextView = findViewById(R.id.name);
        phoneTextView = findViewById(R.id.phone);
        addressTextView = findViewById(R.id.address);
        orderDateTextView = findViewById(R.id.orderDate);
        orderStatusTextView = findViewById(R.id.orderStatus);
        totalAmountTextView = findViewById(R.id.textView10);
        productsRecyclerView = findViewById(R.id.productsRecyclerView);

        id = getIntent().getStringExtra("id");
        orderId = getIntent().getStringExtra("orderId");
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

        order = FirebaseDatabase.getInstance().getReference("order").child(id);
        ImageView imageView = findViewById(R.id.imageView2);
        registerForContextMenu(imageView);

        ImageView backButton = findViewById(R.id.imageView8);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.imageView2) {
            menu.setHeaderTitle("Chọn trạng thái");
            if(userRole.equals("shipper")){
                menu.add(0, v.getId(), 0, "Đang giao");
                menu.add(0, v.getId(), 1, "Đã giao");
                menu.add(0, v.getId(), 2, "Giao không thành công");
            }else{
                menu.add(0, v.getId(), 0, "Đã xác nhận");
                menu.add(0, v.getId(), 1, "Đã huỷ");
            }

        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        String selectedStatus = item.getTitle().toString();
        updateOrderStatusInFirebase(selectedStatus);
        return super.onContextItemSelected(item);
    }
    private void updateOrderStatusInFirebase(String status) {
        order.child("status").setValue(status)
                .addOnSuccessListener(aVoid -> {
                    orderStatusTextView.setText("Trạng thái: " + status);
                    Toast.makeText(this, "", Toast.LENGTH_SHORT).makeText(OrderDetailActivity.this, "Đã cập nhật trạng thái: " + status, Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(OrderDetailActivity.this, "Lỗi khi cập nhật trạng thái", Toast.LENGTH_SHORT).show();
                });
    }



}
