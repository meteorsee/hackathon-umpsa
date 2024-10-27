package com.example.hackathon.models;

public class IncomingGoods {
    private String name;
    private String category;
    private String quantity;

    // Required empty constructor for Firebase
    public IncomingGoods() {}

    public IncomingGoods(String name, String category, String quantity) {
        this.name = name;
        this.category = category;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getQuantity() {
        return quantity;
    }
}
