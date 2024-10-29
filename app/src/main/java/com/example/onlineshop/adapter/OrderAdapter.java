package com.example.onlineshop.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlineshop.R;
import com.example.onlineshop.model.Order;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    private List<Order> orderList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Order order);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
    public OrderAdapter(List<Order> orderList) {
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_item, parent, false);
        return new OrderAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.ViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.bind(order, listener);
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView date;
        private TextView place;
        private TextView price;
        private TextView status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.order_time);
            place = itemView.findViewById(R.id.order_place);
            price = itemView.findViewById(R.id.order_price);
            status = itemView.findViewById(R.id.order_status);
        }

        public void bind(Order order, OnItemClickListener listener) {
            date.setText(order.getOrderDate());
            place.setText(order.getName());
            price.setText(String.valueOf(order.getTotalAmount()));
            status.setText(order.getStatus());

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(order);
                }
            });
        }


    }
}
