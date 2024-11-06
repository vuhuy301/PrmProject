package com.example.onlineshop.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
import com.example.onlineshop.R;
import com.example.onlineshop.model.OrderItem;

import java.util.List;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.OrderItemViewHolder> {

    private List<OrderItem> orderItemList;

    public OrderItemAdapter(List<OrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }

    @NonNull
    @Override
    public OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_detail_item, parent, false);
        return new OrderItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemViewHolder holder, int position) {
        // Gắn dữ liệu vào các view của từng item
        OrderItem orderItem = orderItemList.get(position);
        holder.productNameTextView.setText(orderItem.getProductName());
        holder.quantityTextView.setText("x" + orderItem.getQuantity());
        holder.priceTextView.setText("Giá: " + orderItem.getPrice() + " Đ");

        Log.d("OrderItem", "Image Name: " + orderItem.getImages());
        int drawableResourceId = holder.itemView.getContext().getResources().getIdentifier(orderItem.getImages(), "drawable", holder.itemView.getContext().getPackageName());


        if (drawableResourceId != 0) {
            Glide.with(holder.itemView.getContext())
                    .load(drawableResourceId)
                    .transform(new GranularRoundedCorners(30, 30, 0, 0)) // Nếu bạn cần làm tròn các góc
                    .into(holder.image);
        }


    }

    @Override
    public int getItemCount() {
        return orderItemList.size();
    }

    static class OrderItemViewHolder extends RecyclerView.ViewHolder {

        TextView productNameTextView;
        TextView quantityTextView;
        TextView priceTextView;
        ImageView image;

        public OrderItemViewHolder(@NonNull View itemView) {
            super(itemView);
            productNameTextView = itemView.findViewById(R.id.productNameTextView);
            quantityTextView = itemView.findViewById(R.id.quantityTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            image = itemView.findViewById(R.id.imageView7);
        }
    }
}
