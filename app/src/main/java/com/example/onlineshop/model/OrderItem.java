package com.example.onlineshop.model;

import java.io.Serializable;

public class OrderItem implements Serializable {

    private String productId;
    private String productName;
    private int quantity;
    private double price;

    private String images;
    public OrderItem() {
    }

    public OrderItem( String productId, String productName, int quantity, double price, String images) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;

        this.images = images;
    }


    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }


    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }
}
