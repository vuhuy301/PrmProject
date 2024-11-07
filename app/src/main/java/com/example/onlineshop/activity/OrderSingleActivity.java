package com.example.onlineshop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.onlineshop.R;
import com.example.onlineshop.model.Order;
import com.example.onlineshop.model.OrderItem;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderSingleActivity extends AppCompatActivity {
    private EditText productQuantity, inputName, inputPhone, inputAddress;
    private TextView productName, productPrice;
    private ImageView productImage;
    private Button orderButton;
    private int userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkout_single_order);
        userId = 1;
        // Kết nối các view
        productQuantity = findViewById(R.id.productQuantity);
        inputName = findViewById(R.id.inputName);
        inputPhone = findViewById(R.id.inputPhone);
        inputAddress = findViewById(R.id.inputAddress);
        productName = findViewById(R.id.productName);
        productPrice = findViewById(R.id.productPrice);
        productImage = findViewById(R.id.productImage);
        orderButton = findViewById(R.id.orderButton);

        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        String name = intent.getStringExtra("productName");
        double price = intent.getDoubleExtra("productPrice", 0.0);
        String image = intent.getStringExtra("productImage");
        String id = intent.getStringExtra("productId");

        // Gắn dữ liệu vào các view
        productName.setText(name);
        productPrice.setText(String.format("Giá: %.2f $", price));

        // Nếu có hình ảnh, load nó vào ImageView
        if (image != null) {
            int drawableResourceId = this.getResources().getIdentifier(image, "drawable", this.getPackageName());
            Glide.with(this).load(drawableResourceId).into(productImage);
        }

        // Thực hiện đặt hàng khi nhấn nút
        orderButton.setOnClickListener(v -> {
            // Thực hiện hành động đặt hàng
            String quantityStr = productQuantity.getText().toString();
            String nameOrder = inputName.getText().toString();
            String phone = inputPhone.getText().toString();
            String address = inputAddress.getText().toString();

            // Xử lý dữ liệu đặt hàng
            if (quantityStr.isEmpty() || nameOrder.isEmpty() || phone.isEmpty() || address.isEmpty()) {
                Toast.makeText(OrderSingleActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            int quantity = Integer.parseInt(quantityStr);
            double totalAmount = price * quantity;  // Tính tổng tiền

            // Tạo đối tượng OrderItem
            OrderItem item = new OrderItem(id, name, quantity, price, image);

            // Tạo danh sách các sản phẩm trong đơn hàng
            List<OrderItem> items = new ArrayList<>();
            items.add(item);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String currentDate = sdf.format(new Date());
            // Tạo đối tượng Order
            String orderId = "ORD" + System.currentTimeMillis();  // Tạo ID đơn hàng duy nhất
            Order order = new Order(orderId, userId, name, phone, items, totalAmount, "Chưa xác nhận",currentDate,address);

            // Khởi tạo Firebase Database reference
            DatabaseReference database = FirebaseDatabase.getInstance().getReference();

            // Thêm đơn hàng vào Firebase
            database.child("order").child(orderId).setValue(order)
                    .addOnSuccessListener(aVoid -> {
                        // Thành công
                        Toast.makeText(OrderSingleActivity.this, "Đặt hàng thành công", Toast.LENGTH_SHORT).show();
                        finish();  // Quay lại trang trước
                    })
                    .addOnFailureListener(e -> {
                        // Thất bại
                        Toast.makeText(OrderSingleActivity.this, "Đặt hàng thất bại", Toast.LENGTH_SHORT).show();
                    });
        });
    }
}
