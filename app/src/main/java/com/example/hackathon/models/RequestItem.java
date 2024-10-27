package com.example.hackathon.models;

public class RequestItem {
    private String name;
    private String category;
    private String quantity;

    // Required empty constructor for Firebase
    public RequestItem() {}

    public RequestItem(String name, String category, String quantity) {
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
