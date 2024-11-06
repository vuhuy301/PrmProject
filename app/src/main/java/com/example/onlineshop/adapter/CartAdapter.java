package com.example.onlineshop.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.onlineshop.R;
import com.example.onlineshop.model.Cart;
import com.example.onlineshop.model.CartItem;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<CartItem> cartItems;
    private onUpdateListener onUpdateListener;

    public CartAdapter(List<CartItem> cartItems, onUpdateListener onUpdateListener) {
        this.cartItems = cartItems;
        this.onUpdateListener = onUpdateListener;
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
        holder.price.setSelected(true);
        holder.quantity.setText("" + cartItem.getQuantity());
        holder.totalPrice.setText(String.format("$%.2f", cartItem.getPrice() * cartItem.getQuantity()));
        holder.btnDelete.setOnClickListener(v -> onUpdateListener.onDelete(position));
        holder.btnPlus.setOnClickListener(v -> {
            cartItem.setQuantity(cartItem.getQuantity() + 1);
            onUpdateListener.onUpdate(position, cartItem);
        });
        holder.btnMinus.setOnClickListener(v -> {
            if (cartItem.getQuantity() > 1) {
                cartItem.setQuantity(cartItem.getQuantity() - 1);
                onUpdateListener.onUpdate(position, cartItem);
            } else {
                onUpdateListener.onDelete(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView productName, price, btnPlus, btnMinus, totalPrice, quantity;
        ImageView btnDelete;

        public CartViewHolder(View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.TitleTxt);
            price = itemView.findViewById(R.id.feeEachItem);
            quantity = itemView.findViewById(R.id.numberItemTxt);
            btnPlus = itemView.findViewById(R.id.plusCartBtn);
            btnMinus = itemView.findViewById(R.id.minusCartItem);
            totalPrice = itemView.findViewById(R.id.totalEachItem);
            btnDelete = itemView.findViewById(R.id.btnDelete);

        }
    }

    public void updateCartItems(List<CartItem> newCartItems) {
        cartItems = newCartItems;
        notifyDataSetChanged();
    }

    public interface onUpdateListener {
        void onUpdate(int position, CartItem cartItem);

        void onDelete(int position);
    }
}
