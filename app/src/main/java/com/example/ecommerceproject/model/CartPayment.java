package com.example.ecommerceproject.model;

import java.io.Serializable;

public class CartPayment implements Serializable {
    private String paymentKey;
    private String userKey;
    private String productKey;
    private int quantity;
    private String status;
    private String location;
    private String date;

    public CartPayment() {
    }

    public CartPayment(String paymentKey, String userKey, String productKey, int quantity, String status, String location, String date) {
        this.paymentKey = paymentKey;
        this.userKey = userKey;
        this.productKey = productKey;
        this.quantity = quantity;
        this.status = status;
        this.location = location;
        this.date = date;
    }

    public String getPaymentKey() {
        return paymentKey;
    }

    public void setPaymentKey(String paymentKey) {
        this.paymentKey = paymentKey;
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
