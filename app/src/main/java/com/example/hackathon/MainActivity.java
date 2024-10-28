package com.example.hackathon;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private TextView userGreeting;
    private Button optionButton1, optionButton2, optionButton3;
    private String userRole;
    private String userId;

    private static final String PREFS_NAME = "MyPrefs";
    private static final String USER_ID_KEY = "userId";
    private static final String USER_ROLE_KEY = "userRole"; // New key for user role
    private static final String TAG = "MainActivity"; // Log tag

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        userGreeting = findViewById(R.id.userGreeting);
        optionButton1 = findViewById(R.id.optionButton1);
        optionButton2 = findViewById(R.id.optionButton2);
        optionButton3 = findViewById(R.id.optionButton3);
        Button profileOptionsButton = findViewById(R.id.profileOptionsButton);

        // Retrieve the user ID and role from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        userId = sharedPreferences.getString(USER_ID_KEY, null);
        userRole = sharedPreferences.getString(USER_ROLE_KEY, null);

        if (userId != null) {
            Log.d(TAG, "User ID found in SharedPreferences: " + userId);

            if (userRole != null) {
                Log.d(TAG, "User role found in SharedPreferences: " + userRole);
                updateUIBasedOnRole(); // If role is available, directly update UI
            } else {
                Log.d(TAG, "User role not found in SharedPreferences. Fetching from Firebase...");
                fetchUserRole(userId); // Fetch role from Firebase if not in SharedPreferences
            }

            // Set up profile options button
            profileOptionsButton.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, ProfileOptionsActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            });
        } else {
            Log.d(TAG, "No user ID found in SharedPreferences.");
            Toast.makeText(this, "No user logged in. Please log in first.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    // Fetch user role from Firebase if not found in SharedPreferences
    private void fetchUserRole(String userId) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    userRole = snapshot.child("role").getValue(String.class);
                    String firstName = snapshot.child("firstName").getValue(String.class);
                    String lastName = snapshot.child("lastName").getValue(String.class);

                    Log.d(TAG, "User role fetched from Firebase: " + userRole);

                    // Display greeting
                    userGreeting.setText("Hello, " + firstName + " " + lastName + "!");

                    // Store user role in SharedPreferences for future sessions
                    SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
                    editor.putString(USER_ROLE_KEY, userRole);
                    editor.apply();
                    Log.d(TAG, "User role stored in SharedPreferences: " + userRole);


                    updateUIBasedOnRole();
                } else {
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
        if ("Donor".equalsIgnoreCase(userRole)) {
            optionButton1.setText("Make a Donation");
            optionButton2.setText("View Donation History");
            optionButton3.setText("Leaderboard");

            optionButton1.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            });
            optionButton2.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, DonationHistoryActivity.class)));
            optionButton3.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, LeaderboardActivity.class)));
        } else if ("Seller".equalsIgnoreCase(userRole)) {
            // Uncomment or adjust this section based on the actual Seller options needed
//            optionButton1.setText("Incoming Goods");
//            optionButton2.setText("Request Items");
//            optionButton3.setText("Support Contacts");

//            optionButton1.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, IncomingGoodsActivity.class)));
//            optionButton2.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, RequestItemsActivity.class)));
//            optionButton3.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, SupportContactsActivity.class)));
        } else {
            optionButton1.setText("Explore Donations");
            optionButton2.setVisibility(View.GONE);
            optionButton3.setVisibility(View.GONE);
        }
    }
}
