package com.example.hackathon.models;

// Recipient.java (extends User for Recipient-specific data)
public class Recipient extends User {
    public String category, bankName, bankAccount;

    public Recipient() {
    }

    public Recipient(String firstName, String lastName, String email, String address, String phoneNumber, String role, String category, String bankName, String bankAccount) {
        super(firstName, lastName, email, address, phoneNumber, role);
        this.category = category;
        this.bankName = bankName;
        this.bankAccount = bankAccount;
    }
}
