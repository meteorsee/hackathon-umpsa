package com.example.hackathon;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class EditProfile extends AppCompatActivity {

    private Button logoutButton, editProfileButton;
    private TextView welcomeTextView; // For displaying the user's name
    private String uid; // Declare uid here


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        welcomeTextView = findViewById(R.id.welcomeTextView); // Assuming you have a TextView with this ID
        editProfileButton = findViewById(R.id.editProfileButton); // Assuming you have a Button with this ID

        // Set the welcome message
        //welcomeTextView.setText("Welcome, " + firstName + " " + lastName + "!");

        // Retrieve the user's name from the Intent
        String firstName = getIntent().getStringExtra("firstName");
        String lastName = getIntent().getStringExtra("lastName");
        logoutButton = findViewById(R.id.logoutButton);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });

        // Set up the button click listener
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the UserProfileActivity to edit details
                Intent intent = new Intent(EditProfile.this, UserProfileActivity.class);
                intent.putExtra("uid", "uid"); // Pass UID to the new activity
                startActivity(intent);
            }
        });
    }

    private void logoutUser() {
        // Clear user data from SharedPreferences if you are using it
        SharedPreferences sharedPreferences = getSharedPreferences("YourAppPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear(); // or remove specific keys
        editor.apply();

        // If using Firebase Authentication
        FirebaseAuth.getInstance().signOut();

        // Redirect to the login activity or another appropriate activity
        Intent intent = new Intent(EditProfile.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish(); // Optionally, finish the current activity
    }


}