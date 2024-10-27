package com.example.hackathon;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DonorProfileActivity extends AppCompatActivity {
    private String userId;
    private EditText firstNameEditText, lastNameEditText, emailEditText, addressEditText, phoneEditText;
    private Button saveButton;

    private static final String PREFS_NAME = "MyPrefs";
    private static final String USER_ID_KEY = "userId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_donor_profile);

        // Retrieve userId from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        userId = sharedPreferences.getString(USER_ID_KEY, null);

        if (userId == null) {
            Toast.makeText(this, "Please log in to view and edit profile", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize EditTexts and Button
        firstNameEditText = findViewById(R.id.firstNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        addressEditText = findViewById(R.id.addressEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        saveButton = findViewById(R.id.saveButton);

        // Load user profile data
        loadUserProfile();

        // Set up the save button click listener
        saveButton.setOnClickListener(v -> saveUserProfile());
    }

    private void loadUserProfile() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Populate your EditTexts with user data
                    String firstName = snapshot.child("firstName").getValue(String.class);
                    String lastName = snapshot.child("lastName").getValue(String.class);
                    String email = snapshot.child("email").getValue(String.class);
                    String address = snapshot.child("address").getValue(String.class);
                    String phoneNumber = snapshot.child("phoneNumber").getValue(String.class);

                    // Set the EditText fields with fetched data
                    firstNameEditText.setText(firstName);
                    lastNameEditText.setText(lastName);
                    addressEditText.setText(address);
                    emailEditText.setText(email);
                    phoneEditText.setText(phoneNumber);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DonorProfileActivity.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveUserProfile() {
        // Get updated values from EditTexts
        String updatedFirstName = firstNameEditText.getText().toString();
        String updatedLastName = lastNameEditText.getText().toString();
        String updatedEmail = emailEditText.getText().toString();
        String updatedAddress = addressEditText.getText().toString();
        String updatedPhoneNumber = phoneEditText.getText().toString();

        // Update data in Firebase
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId);
        databaseReference.child("firstName").setValue(updatedFirstName);
        databaseReference.child("lastName").setValue(updatedLastName);
        databaseReference.child("email").setValue(updatedEmail);
        databaseReference.child("address").setValue(updatedAddress);
        databaseReference.child("phoneNumber").setValue(updatedPhoneNumber).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(DonorProfileActivity.this, "Profile updated successfully.", Toast.LENGTH_SHORT).show();
                // Redirect back to MainActivity
                Intent intent = new Intent(DonorProfileActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(DonorProfileActivity.this, "Failed to update profile. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
