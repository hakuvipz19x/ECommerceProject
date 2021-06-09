package com.example.ecommerceproject.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Cart implements Serializable {
    private String key;
    private int quantity;

    public Cart() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
