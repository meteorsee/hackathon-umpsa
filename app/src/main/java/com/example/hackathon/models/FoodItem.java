package com.example.hackathon.models;

public class FoodItem {
    private String name;
    private String description;
    private double price;
    private int imageResourceId;
    private boolean selected; // Add this field

    public FoodItem(String name, String description, double price, int imageResourceId) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageResourceId = imageResourceId;
        this.selected = false; // Default to not selected
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public boolean isSelected() { // Getter for selected
        return selected;
    }

    public void setSelected(boolean selected) { // Setter for selected
        this.selected = selected;
    }
}
