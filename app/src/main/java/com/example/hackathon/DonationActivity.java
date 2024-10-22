package com.example.hackathon;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hackathon.models.Donation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DonationActivity extends AppCompatActivity {

    private Spinner foodBankSpinner;
    private RadioGroup donationTypeGroup;
    private EditText addressInput, donationDetailsInput;
    private Button submitDonationButton;
    private RadioButton foodDonationButton, prePurchaseButton;

    private FirebaseAuth mAuth;
    private DatabaseReference donationsRef;
    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            // Redirect to login if the user is not authenticated
            finish();
            return;
        }

        String userId = currentUser.getUid(); // Get user ID of the logged-in donor

        // Initialize Firebase Realtime Database references
        donationsRef = FirebaseDatabase.getInstance().getReference("donations");
        usersRef = FirebaseDatabase.getInstance().getReference("users");

        // Initialize Views
        foodBankSpinner = findViewById(R.id.foodBankSpinner);
        donationTypeGroup = findViewById(R.id.donationTypeGroup);
        addressInput = findViewById(R.id.addressInput);
        donationDetailsInput = findViewById(R.id.donationDetailsInput);
        submitDonationButton = findViewById(R.id.submitDonationButton);
        foodDonationButton = findViewById(R.id.foodDonationButton);
        prePurchaseButton = findViewById(R.id.prePurchaseButton);

        // Handle donation type toggle visibility for address input
        donationTypeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.prePurchaseButton) {
                    addressInput.setVisibility(View.VISIBLE);
                } else {
                    addressInput.setVisibility(View.GONE);
                }
            }
        });

        // Submit Donation button click listener
        submitDonationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitDonation(userId); // Use userId instead of donorId
            }
        });
    }

    private void submitDonation(String userId) {
        // Get selected food bank
        String selectedFoodBank = foodBankSpinner.getSelectedItem().toString();

        // Get donation type
        boolean isPrePurchase = prePurchaseButton.isChecked();

        // Get donation details
        String donationDetails = donationDetailsInput.getText().toString();
        String address = null;

        // If it's a pre-purchase donation, get the address
        if (isPrePurchase) {
            address = addressInput.getText().toString();
            if (address.isEmpty()) {
                Toast.makeText(DonationActivity.this, "Please enter an address for pre-purchase.", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (donationDetails.isEmpty()) {
            Toast.makeText(DonationActivity.this, "Please provide the donation details.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get current timestamp
        long timestamp = System.currentTimeMillis();

        // Create Donation object
        Donation donation = new Donation(userId, selectedFoodBank, isPrePurchase ? "Pre-Purchase" : "Food Donation", donationDetails, address, timestamp);

        // Store the donation in Firebase Realtime Database
        donationsRef.push().setValue(donation).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // After successful donation, update points
                addDonationAndPoints(userId, 100); // Assuming 100 points per donation

                Toast.makeText(DonationActivity.this, "Donation recorded successfully.", Toast.LENGTH_SHORT).show();

                // Optionally, clear the form fields after submission
                donationDetailsInput.setText("");
                if (isPrePurchase) {
                    addressInput.setText("");
                }
            } else {
                Toast.makeText(DonationActivity.this, "Failed to record donation. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addDonationAndPoints(String userId, int pointsToAdd) {
        // Step 1: Update donor's points in Firebase
        DatabaseReference userRef = usersRef.child(userId);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Get the current points
                    Integer currentPoints = snapshot.child("points").getValue(Integer.class);

                    // Add points for the donation
                    int updatedPoints = (currentPoints != null ? currentPoints : 0) + pointsToAdd;

                    // Update points in the database
                    userRef.child("points").setValue(updatedPoints);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database errors here
            }
        });
    }
}

