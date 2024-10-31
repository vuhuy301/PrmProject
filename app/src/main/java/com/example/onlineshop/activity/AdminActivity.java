package com.example.onlineshop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.onlineshop.R;
import com.example.onlineshop.model.Order;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminActivity extends AppCompatActivity {
    private TextView textViewPendingOrder;
    private TextView textViewCompletedOrder;
    private TextView textViewTotalEarnings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.admin_dashboard);

        initializeViews();
        setupCardViewListeners();
        fetchOrderData(); // Gọi phương thức để lấy dữ liệu đơn hàng
    }

    private void initializeViews() {
        textViewPendingOrder = findViewById(R.id.textViewPendingOrder);
        textViewCompletedOrder = findViewById(R.id.textViewCompletedOrder);
        textViewTotalEarnings = findViewById(R.id.textViewTotalEarnings);
    }

    private void setupCardViewListeners() {
        CardView cardViewUserList = findViewById(R.id.cardViewUserList);
        cardViewUserList.setOnClickListener(v -> startActivity(new Intent(AdminActivity.this, UserListActivity.class)));

        CardView cardViewCreateUser = findViewById(R.id.cardViewCreateUser);
        cardViewCreateUser.setOnClickListener(v -> startActivity(new Intent(AdminActivity.this, CreateUserActivity.class)));
    }

    private void fetchOrderData() {
        DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference("order");
        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int pendingOrdersCount = 0;
                int completedOrdersCount = 0;
                double totalEarnings = 0;

                for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                    Order order = orderSnapshot.getValue(Order.class);
                    if (order != null) {
                        // Sử dụng đúng giá trị trạng thái "processing" và "success"
                        switch (order.getStatus()) {
                            case "Processing": // Trạng thái đang xử lý
                                pendingOrdersCount++;
                                break;
                            case "Shipped": // Trạng thái hoàn thành
                                completedOrdersCount++;
                                totalEarnings += order.getTotalAmount();
                                break;
                        }
                    }
                }

                updateOrderSummary(pendingOrdersCount, completedOrdersCount, totalEarnings);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý lỗi nếu cần
            }
        });
    }

    private void updateOrderSummary(int pendingOrdersCount, int completedOrdersCount, double totalEarnings) {
        textViewPendingOrder.setText(String.valueOf(pendingOrdersCount));
        textViewCompletedOrder.setText(String.valueOf(completedOrdersCount));
        textViewTotalEarnings.setText(String.format("%.2f $", totalEarnings)); // Định dạng số tiền với 2 chữ số thập phân
    }
}
