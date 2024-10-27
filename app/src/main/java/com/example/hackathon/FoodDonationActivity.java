package com.example.hackathon;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hackathon.models.Donation;
import com.example.hackathon.models.Item;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FoodDonationActivity extends AppCompatActivity {
    private Spinner foodBankSpinner;
    private LinearLayout foodItemsLayout;
    private Button submitButton;
    private List<CheckBox> itemCheckBoxes;
    private DatabaseReference donationsRef;
    private DatabaseReference usersRef;

    private String userId;

    private static final String PREFS_NAME = "MyPrefs";
    private static final String USER_ID_KEY = "userId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_donation);

        // Retrieve userId from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        userId = sharedPreferences.getString(USER_ID_KEY, null);

        if (userId == null) {
            Toast.makeText(this, "No user logged in. Please log in first.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        foodBankSpinner = findViewById(R.id.foodBankSpinner);
        foodItemsLayout = findViewById(R.id.foodItemsLayout);
        submitButton = findViewById(R.id.submitButton);
        itemCheckBoxes = new ArrayList<>();
        donationsRef = FirebaseDatabase.getInstance().getReference("donations");
        usersRef = FirebaseDatabase.getInstance().getReference("users");

        // Sample food items
        String[] foodItems = {"Canned Food", "Water Bottles", "Blankets", "First Aid Kits"};
        for (String item : foodItems) {
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(item);
            foodItemsLayout.addView(checkBox);
            itemCheckBoxes.add(checkBox);
        }

        submitButton.setOnClickListener(v -> submitDonation());
    }

    private void submitDonation() {
        String selectedFoodBank = foodBankSpinner.getSelectedItem().toString();

        List<Item> selectedItems = new ArrayList<>();
        for (CheckBox checkBox : itemCheckBoxes) {
            if (checkBox.isChecked()) {
                selectedItems.add(new Item(checkBox.getText().toString()));
            }
        }

        if (selectedItems.isEmpty()) {
            Toast.makeText(this, "Please select at least one item to donate.", Toast.LENGTH_SHORT).show();
            return;
        }

        long timestamp = System.currentTimeMillis();
        Donation donation = new Donation(userId, selectedFoodBank, "Food Donation", "", timestamp, selectedItems);

        donationsRef.push().setValue(donation).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                addDonationAndPoints(100);  // Adjust points as needed
                Toast.makeText(FoodDonationActivity.this, "Donation recorded successfully.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(FoodDonationActivity.this, "Failed to record donation. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addDonationAndPoints(int pointsToAdd) {
        DatabaseReference userRef = usersRef.child(userId);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Integer currentPoints = snapshot.child("points").getValue(Integer.class);
                    int updatedPoints = (currentPoints != null ? currentPoints : 0) + pointsToAdd;
                    userRef.child("points").setValue(updatedPoints);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(FoodDonationActivity.this, "Error updating points: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
