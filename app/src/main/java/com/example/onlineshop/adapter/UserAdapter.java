package com.example.onlineshop.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlineshop.activity.EditStatusActivity;
import com.example.onlineshop.activity.UserDetailActivity;
import com.example.onlineshop.databinding.ViewholderUserBinding;
import com.example.onlineshop.model.User;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    ArrayList<User> users;
    Context context;

    public UserAdapter(ArrayList<User> users) {
        this.users = users;
    }

    @NonNull
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewholderUserBinding binding = ViewholderUserBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        context = parent.getContext();
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.ViewHolder holder, int position) {
        User user = users.get(position);
        holder.binding.userName.setText(user.getName());
        holder.binding.userEmail.setText(user.getEmail());
        holder.binding.userRole.setText(user.getRole());


        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, UserDetailActivity.class);
            intent.putExtra("user", user); // Truyền đối tượng User
            context.startActivity(intent);
        });

        String statusText = user.getIsActive() ? "Active" : "Inactive";
        holder.binding.userStatus.setText("Active Status: " + statusText);

        // Edit Status Button Click
        holder.binding.editStatusButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditStatusActivity.class);
            intent.putExtra("userId", user.getId());
            intent.putExtra("isActive", user.getIsActive());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ViewholderUserBinding binding;

        public ViewHolder(ViewholderUserBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
