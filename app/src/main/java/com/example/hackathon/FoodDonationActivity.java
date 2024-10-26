package com.example.hackathon;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hackathon.models.Donation;
import com.example.hackathon.models.Item;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class FoodDonationActivity extends AppCompatActivity {

    private LinearLayout foodItemsLayout;
    private Button submitButton;
    private List<CheckBox> itemCheckBoxes;

    private DatabaseReference donationsRef;
    private String userId = "exampleUserId"; // Replace with actual user ID logic

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_donation);

        foodItemsLayout = findViewById(R.id.foodItemsLayout);
        submitButton = findViewById(R.id.submitButton);
        itemCheckBoxes = new ArrayList<>();
        donationsRef = FirebaseDatabase.getInstance().getReference("donations"); // Initialize your Firebase reference

        // Sample food items
        String[] foodItems = {"Canned Food", "Water Bottles", "Blankets", "First Aid Kits"};

        for (String item : foodItems) {
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(item);
            foodItemsLayout.addView(checkBox);
            itemCheckBoxes.add(checkBox);
        }

        submitButton.setOnClickListener(v -> submitDonation(userId));
    }

    private void submitDonation(String userId) {
        List<Item> selectedItems = new ArrayList<>();

        // Collect selected items
        for (CheckBox checkBox : itemCheckBoxes) {
            if (checkBox.isChecked()) {
                selectedItems.add(new Item(checkBox.getText().toString())); // Assume Item constructor accepts item name
            }
        }

        if (selectedItems.isEmpty()) {
            Toast.makeText(this, "Please select at least one item to donate.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get current timestamp
        long timestamp = System.currentTimeMillis();

        // Create Donation object
        Donation donation = new Donation(userId, "", "Food Donation", "", "", timestamp, selectedItems);

        // Store the donation in Firebase Realtime Database
        donationsRef.push().setValue(donation).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(FoodDonationActivity.this, "Donation recorded successfully.", Toast.LENGTH_SHORT).show();
                // Optionally clear selections
            } else {
                Toast.makeText(FoodDonationActivity.this, "Failed to record donation. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
