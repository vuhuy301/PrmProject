package com.example.onlineshop.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.onlineshop.R;
import com.example.onlineshop.model.CartItem;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<CartItem> cartItems;

    public CartAdapter(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CartViewHolder holder, int position) {
        CartItem cartItem = cartItems.get(position);
        holder.productName.setText(cartItem.getName());
        holder.price.setText(String.format("$%.2f", cartItem.getPrice()));
        holder.quantity.setText(String.format("Quantity: %d", cartItem.getQuantity()));
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView productName, price, quantity;

        public CartViewHolder(View itemView) {
            super(itemView);
//            productName = itemView.findViewById(R.id.productName);
//            price = itemView.findViewById(R.id.price);
//            quantity = itemView.findViewById(R.id.quantity);
        }
    }
}
