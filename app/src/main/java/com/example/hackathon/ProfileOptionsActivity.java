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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileOptionsActivity extends AppCompatActivity {
    private static final String TAG = "ProfileOptionsActivity"; // Tag for logging
    private Button editProfileButton;
    private Button logoutButton;
    private String userId; // Variable to hold user ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_options);

        // Retrieve userId from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        userId = sharedPreferences.getString("userId", null);
        Log.d(TAG, "User ID retrieved from SharedPreferences: " + userId);

        if (userId == null) {
            Toast.makeText(this, "User ID not found. Please log in again.", Toast.LENGTH_SHORT).show();
            finish(); // Finish the activity if userId is not found
            return;
        }

        // Initialize the buttons
        editProfileButton = findViewById(R.id.editProfileButton); // Make sure to define this button in your layout
        logoutButton = findViewById(R.id.logoutButton); // Initialize the logout button here

        // Set click listener for edit profile button
        editProfileButton.setOnClickListener(v -> checkUserRoleAndNavigate());

        // Set click listener for logout button
        logoutButton.setOnClickListener(v -> {
            // Check if a user is currently signed in
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if (currentUser != null) {
                // Clear SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();

                // Sign out from Firebase Authentication
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(ProfileOptionsActivity.this, "Logged out successfully.", Toast.LENGTH_SHORT).show();

                // Redirect to LoginActivity
                Intent intent = new Intent(ProfileOptionsActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish(); // Finish current activity
            } else {
                Toast.makeText(ProfileOptionsActivity.this, "No user is currently logged in.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkUserRoleAndNavigate() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId);
        databaseReference.child("role").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String role = snapshot.getValue(String.class); // Get the user role
                    Log.d(TAG, "User role retrieved: " + role);
                    if ("Donor".equalsIgnoreCase(role)) {
                        // Navigate to DonorProfileActivity
                        Intent intent = new Intent(ProfileOptionsActivity.this, DonorProfileActivity.class);
                        intent.putExtra("userId", userId); // Pass userId to DonorProfileActivity
                        startActivity(intent);
                    } else if ("Recipient".equalsIgnoreCase(role)) {
                        // Navigate to RecipientProfileActivity
                        Intent intent = new Intent(ProfileOptionsActivity.this, RecipientProfileActivity.class);
                        intent.putExtra("userId", userId); // Pass userId to RecipientProfileActivity
                        startActivity(intent);
                    } else {
                        Toast.makeText(ProfileOptionsActivity.this, "Unknown role: " + role, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ProfileOptionsActivity.this, "User role not found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(ProfileOptionsActivity.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
