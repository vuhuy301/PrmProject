package com.example.onlineshop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlineshop.R;
import com.example.onlineshop.adapter.CartAdapter;
import com.example.onlineshop.model.CartItem;
import com.example.onlineshop.services.CartService;

import java.util.List;

public class CartActivity extends AppCompatActivity {

    private RecyclerView cartView;
    private CartAdapter cartAdapter;
    private TextView totalFeeTxt, totalTxt, taxTxt, deliveryTxt, emptyTxt;
    private CartService cartService;
    private List<CartItem> cartItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // Initialize views
        cartView = findViewById(R.id.cartView);
        totalFeeTxt = findViewById(R.id.totalFeeTxt);
        totalTxt = findViewById(R.id.totalTxt);
        taxTxt = findViewById(R.id.taxTxt);
        deliveryTxt = findViewById(R.id.deliveryTxt);
        emptyTxt = findViewById(R.id.emptyTxt);

        // Set default values
        taxTxt.setText("0");
        deliveryTxt.setText("0");

        // Initialize CartService (assuming this is your data provider)
        cartService = new CartService();

        // Get cart items for userId = 1
        cartItems = cartService.getCartItemsForUser(1);

        if (cartItems.isEmpty()) {
            emptyTxt.setVisibility(View.VISIBLE);
            cartView.setVisibility(View.GONE);
        } else {
            emptyTxt.setVisibility(View.GONE);
            cartView.setVisibility(View.VISIBLE);
            setUpRecyclerView();
            calculateTotals();
        }

        // Set up back button functionality
        ImageView backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> onBackPressed());

    }

    private void setUpRecyclerView() {
        cartAdapter = new CartAdapter(cartItems);
        cartView.setLayoutManager(new LinearLayoutManager(this));
        cartView.setAdapter(cartAdapter);
    }

    private void calculateTotals() {
        double total = 0;

        for (CartItem item : cartItems) {
            total += item.getPrice() * item.getQuantity();
        }

        totalFeeTxt.setText(String.format("$%.2f", total));
        totalTxt.setText(String.format("$%.2f", total));

        // Example of how tax and delivery could be set if you had logic for those
        // For now, they are set to 0
    }
}
