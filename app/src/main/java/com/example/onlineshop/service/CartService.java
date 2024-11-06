package com.example.onlineshop.service;

import com.example.onlineshop.model.Cart;
import com.example.onlineshop.model.CartItem;
import com.example.onlineshop.model.Order;
import com.example.onlineshop.model.OrderItem;
import com.example.onlineshop.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class CartService {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("cart");

    public void addCartItem(CartItem cartItem, String userid, OnCompleteListener onCompleteListener) {
        databaseReference.child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Cart cart = dataSnapshot.getValue(Cart.class);

                    if (cart.getItems() != null) {
                        boolean isCartItemExit = false;
                        for (int i = 0; i < cart.getItems().size(); i++) {
                            CartItem cartItemm = cart.getItems().get(i);
                            if (cartItem.getProductId().equals(cartItemm.getProductId())) {
                                cartItemm.setQuantity(cartItemm.getQuantity() + cartItem.getQuantity());
                                cart.getItems().set(i, cartItemm);
                                isCartItemExit = true;
                                break;
                            }
                        }
                        if (!isCartItemExit) {
                            cart.getItems().add(cartItem);
                        }
                    }else {
                        List<CartItem> cartItems = new ArrayList<>();
                        cartItems.add(cartItem);
                        cart = new Cart(userid, cartItems);
                    }

                    databaseReference.child(userid).setValue(cart);
                    onCompleteListener.onComplete(true, "Thêm giỏ hàng thành công");
                } else {
                    List<CartItem> cartItems = new ArrayList<>();
                    cartItems.add(cartItem);
                    Cart cart = new Cart(userid, cartItems);
                    databaseReference.child(userid).setValue(cart);

                    onCompleteListener.onComplete(true, "Thêm giỏ hàng thành công");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                onCompleteListener.onComplete(false, "Thêm giỏ hàng thất bại");
            }
        });
    }

    public void updateCart(Cart cart, String userId, OnGetCartListener onCompleteListener) {
        databaseReference.child(userId).setValue(cart)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        getCart(userId, onCompleteListener);
                    } else {
                        onCompleteListener.onGetCartFailure("Cập nhật giỏ hàng thất bại");
                    }
                });
    }

    public void getCart(String userid, OnGetCartListener onCompleteListener) {
        databaseReference.child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Cart cart = dataSnapshot.getValue(Cart.class);
                    if (cart != null) {
                        onCompleteListener.onGetCartSuccess(cart);
                    } else {
                        onCompleteListener.onGetCartFailure("Giỏ hàng trống");
                    }
                } else {
                    onCompleteListener.onGetCartFailure("Giỏ hàng không tồn tại");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                onCompleteListener.onGetCartFailure("Lấy giỏ hàng thất bại");
            }
        });
    }

    public void orderCart(User user, Cart cart, OnCompleteListener onCompleteListener) {
        DatabaseReference databaseOrder = FirebaseDatabase.getInstance().getReference("order");

        Order order = new Order();
        order.setOrderId(generateOrderId());
        order.setUserId(Integer.parseInt(user.getId()));
        order.setName(user.getName());
        order.setPhone(user.getPhoneNumber());

        List<OrderItem> items = new ArrayList<>();
        double totalAmount = 0;
        for (int i = 0; i < cart.getItems().size(); i++) {
            CartItem cartItem = cart.getItems().get(i);

            OrderItem orderItem = new OrderItem();
            orderItem.setOtId(i);
            orderItem.setProductId(cartItem.getProductId());
            orderItem.setProductName(cartItem.getName());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getPrice());
            orderItem.setTotalPrice(cartItem.getQuantity() * cartItem.getPrice());
            orderItem.setImages(cartItem.getImages());
            items.add(orderItem);
            totalAmount += orderItem.getTotalPrice();
        }

        order.setItems(items);
        order.setTotalAmount(totalAmount);
        order.setStatus("Chưa xác nhận");

        long currentTimeMillis = System.currentTimeMillis();
        Date date = new Date(currentTimeMillis);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = formatter.format(date);

        order.setOrderDate(formattedDate);
        order.setShippingAddress(user.getAddress());

        databaseOrder.child(order.getOrderId()).setValue(order)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        cart.getItems().clear();
                        databaseReference.child(user.getId()).setValue(cart);
                        onCompleteListener.onComplete(true, "Đặt hàng thành công");
                    } else {
                        onCompleteListener.onComplete(false, "Cập nhật giỏ hàng thất bại");
                    }
                });
    }


    public interface OnCompleteListener {
        void onComplete(boolean isSuccess, String mes);
    }

    public interface OnGetCartListener {
        void onGetCartSuccess(Cart cart);

        void onGetCartFailure(String message);
    }

    private String generateOrderId() {
        long timestamp = System.currentTimeMillis();

        String timestampStr = String.valueOf(timestamp);

        String orderId = "ORD" + timestampStr.substring(timestampStr.length() - 10) + generateRandomDigits(4);

        return orderId;
    }

    private String generateRandomDigits(int length) {
        Random random = new Random();
        StringBuilder randomDigits = new StringBuilder();
        for (int i = 0; i < length; i++) {
            randomDigits.append(random.nextInt(10));
        }
        return randomDigits.toString();
    }
}
