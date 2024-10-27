package com.example.hackathon;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class ProfileOptionsActivity extends AppCompatActivity {
    private Button editProfileButton;
    private Button logoutButton;
    private String userId; // Variable to hold user ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_options);

        // Get userId from Intent
        userId = getIntent().getStringExtra("userId");

        // Initialize the buttons
        editProfileButton = findViewById(R.id.editProfileButton); // Make sure to define this button in your layout
        logoutButton = findViewById(R.id.logoutButton); // Initialize the logout button here

        // Set click listener for edit profile button
        editProfileButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileOptionsActivity.this, EditProfileActivity.class);
            intent.putExtra("userId", userId); // Pass userId to EditProfileActivity
            startActivity(intent);
        });

        // Set click listener for logout button
        logoutButton.setOnClickListener(v -> {
            // Handle logout logic here
            // For example, clear user session and navigate back to login screen
            // Clear user data from SharedPreferences if you are using it
            SharedPreferences sharedPreferences = getSharedPreferences("YourAppPreferences", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear(); // or remove specific keys
            editor.apply();

            // If using Firebase Authentication
            FirebaseAuth.getInstance().signOut();

            // Redirect to the login activity or another appropriate activity
            Intent intent = new Intent(ProfileOptionsActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish(); // Optionally, finish the current activity
        });
    }
}
