package com.example.hackathon;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log; // Import Log
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

public class SellerProfileActivity extends AppCompatActivity {
    private static final String PREFS_NAME = "MyPrefs";
    private static final String USER_ID_KEY = "userId";
    private static final String TAG = "SellerProfileActivity"; // Log tag
    private String userId;
    private EditText firstNameEditText, lastNameEditText, emailEditText, addressEditText, phoneEditText, bankAccountEditText, bankNameEditText;
    private Button saveButton;
    private String role = "Seller"; // Set the role for seller

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_seller_profile);

        // Retrieve userId from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        userId = sharedPreferences.getString(USER_ID_KEY, null);

        // Log the userId fetched from SharedPreferences
        Log.d(TAG, "Fetched userId from SharedPreferences: " + userId);

        // Retrieve userId from Intent extras
        Intent intent = getIntent();
        String firstName = intent.getStringExtra("firstName");
        String lastName = intent.getStringExtra("lastName");
        String email = intent.getStringExtra("email");

        if (userId == null) {
            Toast.makeText(this, "User ID not found. Please log in again.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        // Initialize EditTexts and Button
        firstNameEditText = findViewById(R.id.firstNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        addressEditText = findViewById(R.id.addressEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        bankAccountEditText = findViewById(R.id.bankAccountEditText);
        bankNameEditText = findViewById(R.id.bankNameEditText);
        saveButton = findViewById(R.id.saveButton);

        // Set fields with initial data from Intent extras
        firstNameEditText.setText(firstName);
        lastNameEditText.setText(lastName);
        emailEditText.setText(email);

        // Load remaining user profile data from Firebase
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
                    // Fetch and set the remaining fields with data from Firebase
                    String firstName = snapshot.child("firstName").getValue(String.class);
                    String lastName = snapshot.child("lastName").getValue(String.class);
                    String email = snapshot.child("email").getValue(String.class);
                    String address = snapshot.child("address").getValue(String.class);
                    String phoneNumber = snapshot.child("phoneNumber").getValue(String.class);
                    String bankAccount = snapshot.child("bankAccount").getValue(String.class);
                    String bankName = snapshot.child("bankName").getValue(String.class);
                    firstNameEditText.setText(firstName);
                    lastNameEditText.setText(lastName);
                    addressEditText.setText(address);
                    emailEditText.setText(email);
                    phoneEditText.setText(phoneNumber);
                    bankAccountEditText.setText(bankAccount);
                    bankNameEditText.setText(bankName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SellerProfileActivity.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
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
        String updatedBankAccount = bankAccountEditText.getText().toString();
        String updatedBankName = bankNameEditText.getText().toString();

        // Update data in Firebase
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId);
        databaseReference.child("firstName").setValue(updatedFirstName);
        databaseReference.child("lastName").setValue(updatedLastName);
        databaseReference.child("email").setValue(updatedEmail);
        databaseReference.child("address").setValue(updatedAddress);
        databaseReference.child("phoneNumber").setValue(updatedPhoneNumber);
        databaseReference.child("bankAccount").setValue(updatedBankAccount);
        databaseReference.child("bankName").setValue(updatedBankName);
        databaseReference.child("role").setValue(role).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(SellerProfileActivity.this, "Profile updated successfully.", Toast.LENGTH_SHORT).show();
                // Redirect back to MainActivity
                Intent intent = new Intent(SellerProfileActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(SellerProfileActivity.this, "Failed to update profile. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
