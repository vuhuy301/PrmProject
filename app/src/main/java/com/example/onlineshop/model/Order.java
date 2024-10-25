package com.example.onlineshop.model;

import java.util.Date;
import java.util.List;

public class Order {
    private String orderId;
    private int userId;
    private String name;
    private String phone;
    private List<OrderItem> items;
    private double totalAmount;
    private String status;
    private String orderDate;
    private String shippingAddress;

    public Order(String orderId, int userId, String name, String phone, List<OrderItem> items, double totalAmount, String status, String orderDate, String shippingAddress) {
        this.orderId = orderId;
        this.userId = userId;
        this.name = name;
        this.phone = phone;
        this.items = items;
        this.totalAmount = totalAmount;
        this.status = status;
        this.orderDate = orderDate;
        this.shippingAddress = shippingAddress;
    }

    public Order() {
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }
}
