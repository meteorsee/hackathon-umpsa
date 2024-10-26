package com.example.hackathon;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hackathon.models.Donation;
import com.example.hackathon.models.Item;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class PrePurchaseActivity extends AppCompatActivity {

    private EditText quantityInput;
    private LinearLayout detailsLayout;
    private Button generateFieldsButton, submitButton;
    private List<EditText> itemDetailsInputs;

    private DatabaseReference donationsRef;
    private String userId = "exampleUserId"; // Replace with actual user ID logic

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_purchase);

        quantityInput = findViewById(R.id.quantityInput);
        detailsLayout = findViewById(R.id.detailsLayout);
        generateFieldsButton = findViewById(R.id.generateFieldsButton);
        submitButton = findViewById(R.id.submitButton);
        itemDetailsInputs = new ArrayList<>();
        donationsRef = FirebaseDatabase.getInstance().getReference("donations"); // Initialize your Firebase reference

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
        List<Item> itemDetails = new ArrayList<>();
        String price = ""; // Get price if necessary

        for (EditText editText : itemDetailsInputs) {
            String detail = editText.getText().toString();
            if (!detail.isEmpty()) {
                itemDetails.add(new Item(detail)); // Assume Item constructor accepts detail
            }
        }

        if (itemDetails.isEmpty()) {
            Toast.makeText(this, "Please enter details for at least one item.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get current timestamp
        long timestamp = System.currentTimeMillis();

        // Create Donation object
        Donation donation = new Donation(userId, "", "Pre-Purchase", "", price, timestamp, itemDetails);

        // Store the donation in Firebase Realtime Database
        donationsRef.push().setValue(donation).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(PrePurchaseActivity.this, "Donation recorded successfully.", Toast.LENGTH_SHORT).show();
                // Optionally clear input fields
            } else {
                Toast.makeText(PrePurchaseActivity.this, "Failed to record donation. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
