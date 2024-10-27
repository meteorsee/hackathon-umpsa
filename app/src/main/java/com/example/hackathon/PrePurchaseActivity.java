package com.example.hackathon;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
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

public class PrePurchaseActivity extends AppCompatActivity {
    private Spinner foodBankSpinner;
    private EditText quantityInput;
    private LinearLayout detailsLayout;
    private Button generateFieldsButton, submitButton;
    private List<EditText> itemDetailsInputs;
    private DatabaseReference donationsRef;
    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String userId = getIntent().getStringExtra("userId");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_purchase);

        foodBankSpinner = findViewById(R.id.foodBankSpinner);
        quantityInput = findViewById(R.id.quantityInput);
        detailsLayout = findViewById(R.id.detailsLayout);
        generateFieldsButton = findViewById(R.id.generateFieldsButton);
        submitButton = findViewById(R.id.submitButton);
        itemDetailsInputs = new ArrayList<>();
        donationsRef = FirebaseDatabase.getInstance().getReference("donations");
        usersRef = FirebaseDatabase.getInstance().getReference("users");

        generateFieldsButton.setOnClickListener(v -> generateInputFields());
        submitButton.setOnClickListener(v -> submitDonation(userId));
    }

    private void generateInputFields() {
        detailsLayout.removeAllViews();
        int quantity;
        try {
            quantity = Integer.parseInt(quantityInput.getText().toString());
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter a valid quantity.", Toast.LENGTH_SHORT).show();
            return;
        }

        itemDetailsInputs.clear();
        for (int i = 0; i < quantity; i++) {
            EditText editText = new EditText(this);
            editText.setHint("Enter details for item " + (i + 1));
            detailsLayout.addView(editText);
            itemDetailsInputs.add(editText);
        }
    }

    private void submitDonation(String userId) {
        String selectedFoodBank = foodBankSpinner.getSelectedItem().toString();
        List<Item> itemDetails = new ArrayList<>();

        for (EditText editText : itemDetailsInputs) {
            String detail = editText.getText().toString();
            if (!detail.isEmpty()) {
                itemDetails.add(new Item(detail));
            }
        }

        if (itemDetails.isEmpty()) {
            Toast.makeText(this, "Please enter details for at least one item.", Toast.LENGTH_SHORT).show();
            return;
        }

        long timestamp = System.currentTimeMillis();
        Donation donation = new Donation(userId, selectedFoodBank, "Pre-Purchase", "", timestamp, itemDetails);

        donationsRef.push().setValue(donation).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                addDonationAndPoints(userId, 100);  // Adjust points as needed
                Toast.makeText(PrePurchaseActivity.this, "Donation recorded successfully.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(PrePurchaseActivity.this, "Failed to record donation. Please try again.", Toast.LENGTH_SHORT).show();
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
