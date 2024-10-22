package com.example.hackathon.models;

// User.java (for Donor)
public class User {
    public String firstName, lastName, email, address, phoneNumber, role;

    public int points;

    public User() {
    }

    public User(String firstName, String lastName, String email, String address, String phoneNumber, String role, int points) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.role = role; // "Donor" or "Recipient"
        this.points = 0;
    }
}
