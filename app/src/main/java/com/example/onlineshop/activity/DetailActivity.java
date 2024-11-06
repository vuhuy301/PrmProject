package com.example.onlineshop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.onlineshop.databinding.ProductDetailBinding;
import com.example.onlineshop.model.CartItem;
import com.example.onlineshop.model.Product;
import com.example.onlineshop.service.CartService;

public class DetailActivity extends AppCompatActivity {

    private ProductDetailBinding binding;
    private Product object;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ProductDetailBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());

        getBundles();
        binding.backBtn.setOnClickListener(v -> finish());

        binding.buyBtnDetail.setOnClickListener(v -> {
            CartItem cartItem = new CartItem(0, object.getProductId(), object.getName(), 1, object.getPrice(), object.getImages());
            CartService cartService = new CartService();
            cartService.addCartItem(cartItem, "1", new CartService.OnCompleteListener() {
                @Override
                public void onComplete(boolean isSuccess, String mes) {
                    if(isSuccess){
                        Toast.makeText(DetailActivity.this, mes, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(DetailActivity.this, CartActivity.class));
                        finish();
                    }else {
                        Toast.makeText(DetailActivity.this, mes, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        });
    }

    private void getBundles() {
        object = (Product) getIntent().getSerializableExtra("object");

        if (object != null) {
            int drawableResourceId = this.getResources().getIdentifier(object.getImages(),
                    "drawable", this.getPackageName());
            Glide.with(this)
                    .load(drawableResourceId)
                    .into(binding.itemPic);

            binding.titleTxtDetail.setText(object.getName());
            binding.priceTxtDetail.setText(String.format("$%.2f", object.getPrice()));
            binding.descriptionTxtDetail.setText(object.getDescription());
        }
    }
}