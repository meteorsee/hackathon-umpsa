package com.example.hackathon;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import androidx.appcompat.app.AppCompatActivity;

public class DonationActivity extends AppCompatActivity {

    private RadioGroup donationTypeGroup;
    private RadioButton foodDonationButton, prePurchaseButton;
    private Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation);

        // Retrieve userId from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", ""); // Default to empty string if not found

        donationTypeGroup = findViewById(R.id.donationTypeGroup);
        nextButton = findViewById(R.id.nextButton);

        nextButton.setOnClickListener(v -> {
            int selectedId = donationTypeGroup.getCheckedRadioButtonId();
            Intent intent;
            if (selectedId == R.id.foodDonationOption) {
                intent = new Intent(DonationActivity.this, FoodDonationActivity.class);
            } else {
                intent = new Intent(DonationActivity.this, PrePurchaseActivity.class);
            }
            intent.putExtra("userId", userId); // Pass userId to the next activity
            startActivity(intent);
        });
    }
}
