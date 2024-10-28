package com.example.hackathon;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class ChooseRoleActivity extends AppCompatActivity {

    private Button donorButton, sellerButton;
    private String firstName, lastName, email, uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_role);

        donorButton = findViewById(R.id.donorButton);
        sellerButton = findViewById(R.id.sellerButton);

        // Retrieve user details from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        firstName = sharedPreferences.getString("firstName", "");
        lastName = sharedPreferences.getString("lastName", "");
        email = sharedPreferences.getString("email", "");
        uid = sharedPreferences.getString("userId", "");

        // Set up donor button click listener
        donorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent donorIntent = new Intent(ChooseRoleActivity.this, DonorProfileActivity.class);
                donorIntent.putExtra("firstName", firstName);
                donorIntent.putExtra("lastName", lastName);
                donorIntent.putExtra("email", email);
                donorIntent.putExtra("uid", uid);
                startActivity(donorIntent);
                finish();
            }
        });

        // Set up seller button click listener
        sellerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sellerIntent = new Intent(ChooseRoleActivity.this, SellerProfileActivity.class);
                sellerIntent.putExtra("firstName", firstName);
                sellerIntent.putExtra("lastName", lastName);
                sellerIntent.putExtra("email", email);
                sellerIntent.putExtra("uid", uid);
                startActivity(sellerIntent);
                finish();
            }
        });
    }
}
