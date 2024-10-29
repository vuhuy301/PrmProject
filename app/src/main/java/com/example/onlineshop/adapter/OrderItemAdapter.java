package com.example.onlineshop.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
    }

    @Override
    public int getItemCount() {
        return orderItemList.size();
    }

    static class OrderItemViewHolder extends RecyclerView.ViewHolder {

        TextView productNameTextView;
        TextView quantityTextView;
        TextView priceTextView;

        public OrderItemViewHolder(@NonNull View itemView) {
            super(itemView);
            productNameTextView = itemView.findViewById(R.id.productNameTextView);
            quantityTextView = itemView.findViewById(R.id.quantityTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
        }
    }
}
