package com.example.onlineshop.activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
import com.example.onlineshop.R;
import com.example.onlineshop.adapter.PopularProductAdapter;
import com.example.onlineshop.databinding.ActivityDetailBinding;
import com.example.onlineshop.databinding.ActivityMainBinding;
import com.example.onlineshop.databinding.ProductDetailBinding;
import com.example.onlineshop.model.Product;

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