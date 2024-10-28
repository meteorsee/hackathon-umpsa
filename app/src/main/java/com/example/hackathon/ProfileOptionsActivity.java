package com.example.hackathon;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileOptionsActivity extends AppCompatActivity {
    private static final String TAG = "ProfileOptionsActivity";
    private Button editProfileButton;
    private Button logoutButton;
    private String userId; // Variable to hold user ID
    private String userRole;   // Variable to hold user role

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_options);

        // Retrieve userId and role from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        userId = sharedPreferences.getString("userId", null);
        userRole = sharedPreferences.getString("userRole", null); // Retrieve role

        Log.d(TAG, "User ID retrieved from SharedPreferences: " + userId);
        Log.d(TAG, "User role retrieved from SharedPreferences: " + userRole);

        if (userId == null || userRole == null) {
            Toast.makeText(this, "User ID or role not found. Please log in again.", Toast.LENGTH_SHORT).show();
            finish(); // Finish the activity if userId or role is not found
            return;
        }

        // Initialize the buttons
        editProfileButton = findViewById(R.id.editProfileButton);
        logoutButton = findViewById(R.id.logoutButton);

        // Set click listener for edit profile button
        editProfileButton.setOnClickListener(v -> checkUserRoleAndNavigate());

        // Set click listener for logout button
        logoutButton.setOnClickListener(v -> {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if (currentUser != null) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();

                FirebaseAuth.getInstance().signOut();
                Toast.makeText(ProfileOptionsActivity.this, "Logged out successfully.", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(ProfileOptionsActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(ProfileOptionsActivity.this, "No user is currently logged in.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkUserRoleAndNavigate() {
        if ("Donor".equalsIgnoreCase(userRole)) {
            Intent intent = new Intent(ProfileOptionsActivity.this, DonorProfileActivity.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        } else if ("Seller".equalsIgnoreCase(userRole)) {
            Intent intent = new Intent(ProfileOptionsActivity.this, SellerProfileActivity.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        } else {
            Toast.makeText(ProfileOptionsActivity.this, "Unknown role: " + userRole, Toast.LENGTH_SHORT).show();
        }
    }
}
