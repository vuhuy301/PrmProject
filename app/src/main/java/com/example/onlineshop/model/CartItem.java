package com.example.onlineshop.model;

public class CartItem {

    private int ciId;
    private String productId;
    private String name;
    private int quantity;
    private double price;
    private String images;

    public CartItem() {
    }

    public CartItem(int ciId, String productId, String name, int quantity, double price, String images) {
        this.ciId = ciId;
        this.productId = productId;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.images = images;
    }

    public int getCiId() {
        return ciId;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public void setCiId(int ciId) {
        this.ciId = ciId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
