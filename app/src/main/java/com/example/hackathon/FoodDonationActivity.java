package com.example.hackathon;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String userId = getIntent().getStringExtra("userId");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_donation);

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

        submitButton.setOnClickListener(v -> submitDonation(userId));
    }

    private void submitDonation(String userId) {
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
                addDonationAndPoints(userId, 100);  // Adjust points as needed
                Toast.makeText(FoodDonationActivity.this, "Donation recorded successfully.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(FoodDonationActivity.this, "Failed to record donation. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addDonationAndPoints(String userId, int pointsToAdd) {
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
                // Handle database errors here
            }
        });
    }
}
