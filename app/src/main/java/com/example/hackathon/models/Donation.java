package com.example.hackathon.models;

import java.util.List;

public class Donation {
    private String userId;
    private String foodBank;
    private String donationType;
    private String donationDetails;
    private String address; // Only for pre-purchase
    private long timestamp;

    // Default constructor required for calls to DataSnapshot.getValue(Donation.class)
    public Donation() {
    }

    public Donation(String userId, String foodBank, String donationType, String donationDetails, String address, long timestamp, List<Item> selectedItems) {
        this.userId = userId;
        this.foodBank = foodBank;
        this.donationType = donationType;
        this.donationDetails = donationDetails;
        this.address = address;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFoodBank() {
        return foodBank;
    }

    public void setFoodBank(String foodBank) {
        this.foodBank = foodBank;
    }

    public String getDonationType() {
        return donationType;
    }

    public void setDonationType(String donationType) {
        this.donationType = donationType;
    }

    public String getDonationDetails() {
        return donationDetails;
    }

    public void setDonationDetails(String donationDetails) {
        this.donationDetails = donationDetails;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
