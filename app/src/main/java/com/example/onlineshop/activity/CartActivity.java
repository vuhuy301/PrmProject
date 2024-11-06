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
import com.example.onlineshop.databinding.ActivityCartBinding;
import com.example.onlineshop.model.Cart;
import com.example.onlineshop.model.CartItem;
import com.example.onlineshop.model.User;
import com.example.onlineshop.service.CartService;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    private CartAdapter cartAdapter;
    private ActivityCartBinding binding;
    private CartService cartService;
    private Cart cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        cartService = new CartService();

        cartService.getCart("1", new CartService.OnGetCartListener() {
            @Override
            public void onGetCartSuccess(Cart cartt) {
                cart = cartt;
                if (cart.getItems().isEmpty()) {
                    binding.emptyTxt.setVisibility(View.VISIBLE);
                    binding.cartView.setVisibility(View.GONE);
                } else {
                    binding.emptyTxt.setVisibility(View.GONE);
                    binding.cartView.setVisibility(View.VISIBLE);
                    setUpRecyclerView();
                    calculateTotals();
                }
            }

            @Override
            public void onGetCartFailure(String message) {

            }
        });

        binding.backBtn.setOnClickListener(v -> onBackPressed());

        binding.button2.setOnClickListener(v -> {
            User fakeUser = new User("1" , "Usertest", "nguyenthitha@example.com", "password123", "456 Tran Hung Dao, Hanoi", "0123456789", true, "customer");
            cartService.orderCart(fakeUser, cart, new CartService.OnCompleteListener() {
                @Override
                public void onComplete(boolean isSuccess, String mes) {
                    if(isSuccess){
                        Toast.makeText(CartActivity.this, mes, Toast.LENGTH_SHORT).show();
                        finish();
                    }else {
                        Toast.makeText(CartActivity.this, mes, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        });
    }

    private void setUpRecyclerView() {
        cartAdapter = new CartAdapter(cart.getItems(), new CartAdapter.onUpdateListener() {
            @Override
            public void onUpdate(int position, CartItem cartItem) {
                cart.getItems().set(position, cartItem);
                cartService.updateCart(cart, "1", new CartService.OnGetCartListener() {
                    @Override
                    public void onGetCartSuccess(Cart cartt) {
                        cart = cartt;
                        if (cart.getItems().isEmpty()) {
                            binding.emptyTxt.setVisibility(View.VISIBLE);
                            binding.cartView.setVisibility(View.GONE);
                        } else {
                            binding.emptyTxt.setVisibility(View.GONE);
                            binding.cartView.setVisibility(View.VISIBLE);
                            cartAdapter.updateCartItems(cart.getItems());
                            calculateTotals();
                        }
                    }

                    @Override
                    public void onGetCartFailure(String message) {

                    }
                });
            }

            @Override
            public void onDelete(int position) {
                cart.getItems().remove(position);
                cartService.updateCart(cart, "1", new CartService.OnGetCartListener() {
                    @Override
                    public void onGetCartSuccess(Cart cartt) {
                        cart = cartt;
                        if (cart.getItems().isEmpty()) {
                            binding.emptyTxt.setVisibility(View.VISIBLE);
                            binding.cartView.setVisibility(View.GONE);
                        } else {
                            binding.emptyTxt.setVisibility(View.GONE);
                            binding.cartView.setVisibility(View.VISIBLE);
                            cartAdapter.updateCartItems(cart.getItems());
                            calculateTotals();
                        }
                    }

                    @Override
                    public void onGetCartFailure(String message) {

                    }
                });
            }
        });
        binding.cartView.setLayoutManager(new LinearLayoutManager(this));
        binding.cartView.setAdapter(cartAdapter);
    }

    private void calculateTotals() {
        double total = 0;

        for (CartItem item : cart.getItems()) {
            total += item.getPrice() * item.getQuantity();
        }

        binding.totalFeeTxt.setText(String.format("$%.2f", total));
        binding.totalTxt.setText(String.format("$%.2f", total));

    }
}
