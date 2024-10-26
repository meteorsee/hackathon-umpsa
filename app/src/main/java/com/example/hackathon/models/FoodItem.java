package com.example.hackathon.models;

public class FoodItem {
    private String name;
    private boolean isSelected;

    public FoodItem(String name) {
        this.name = name;
        this.isSelected = false;
    }

    public String getName() {
        return name;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
