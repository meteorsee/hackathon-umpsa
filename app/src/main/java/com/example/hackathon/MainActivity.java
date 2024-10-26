package com.example.hackathon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private TextView userGreeting;
    private Button optionButton1, optionButton2, optionButton3;
    private FirebaseAuth mAuth;
    private String userRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize views
        userGreeting = findViewById(R.id.userGreeting);
        optionButton1 = findViewById(R.id.optionButton1);
        optionButton2 = findViewById(R.id.optionButton2);
        optionButton3 = findViewById(R.id.optionButton3);

        // Get the logged-in user
        FirebaseUser user = mAuth.getCurrentUser();
        Intent intent = getIntent();
        boolean isGuestMode = intent.getBooleanExtra("guest_mode", false); // Check if guest mode is enabled

        if (isGuestMode) {
            // Set default role and greeting for guest
            userRole = "Guest"; // Default role for guest
            userGreeting.setText("Welcome, Guest!");
            updateUIBasedOnRole(); // Update UI for guest
        } else if (user != null) {
            String userId = user.getUid();
            fetchUserRole(userId);
        } else {
            Toast.makeText(this, "No user logged in.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    // Fetch user role from Firebase
    private void fetchUserRole(String userId) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    userRole = snapshot.child("role").getValue(String.class);
                    String firstName = snapshot.child("firstName").getValue(String.class);
                    String lastName = snapshot.child("lastName").getValue(String.class);

                    // Display greeting
                    userGreeting.setText("Hello, " + firstName + " " + lastName + "!");

                    // Update UI based on user role
                    updateUIBasedOnRole();
                }
                else {
                    Toast.makeText(MainActivity.this, "User role not found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Update UI based on user role
    private void updateUIBasedOnRole() {
        if (userRole.equals("Donor")) {
            optionButton1.setText("Make a Donation");
            optionButton2.setText("View Donation History");
            optionButton3.setText("Leaderboard");
        } else if (userRole.equals("Recipient")) {
            optionButton1.setText("Incoming Goods");
            optionButton2.setVisibility(View.GONE);
            optionButton3.setVisibility(View.GONE);
        } else {
            // Default options for guest
            optionButton1.setText("Explore Donations");
            optionButton2.setVisibility(View.GONE);
            optionButton3.setVisibility(View.GONE);
        }
    }
}
