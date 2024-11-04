package com.example.onlineshop.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
import com.example.onlineshop.activity.DetailActivity;
import com.example.onlineshop.databinding.ViewholderPupListBinding;
import com.example.onlineshop.model.Product;

import java.util.ArrayList;

public class PopularProductAdapter extends RecyclerView.Adapter<PopularProductAdapter.Viewhholder>
{
    ArrayList<Product> items;
    Context context;
    ViewholderPupListBinding binding;

    public PopularProductAdapter(ArrayList<Product> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public PopularProductAdapter.Viewhholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ViewholderPupListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        context = parent.getContext();
        return new Viewhholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PopularProductAdapter.Viewhholder holder, int position) {
        Product product = items.get(position);
        holder.binding.titleTxt.setText(product.getName());
        holder.binding.feeTxt.setText("$" + product.getPrice());


        int drawableResourceId = holder.itemView.getResources().getIdentifier(product.getImages(),
                "drawable", holder.itemView.getContext().getPackageName());

        Glide.with(context)
                .load(drawableResourceId)
                .transform(new GranularRoundedCorners(30, 30, 0, 0))
                .into(holder.binding.pic);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("object", product);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class Viewhholder extends RecyclerView.ViewHolder {
        private final ViewholderPupListBinding binding;

        public Viewhholder(ViewholderPupListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


}
