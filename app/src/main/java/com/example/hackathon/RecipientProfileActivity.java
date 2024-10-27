package com.example.hackathon;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RecipientProfileActivity extends AppCompatActivity {
    private static final String PREFS_NAME = "UserPrefs";
    private static final String USER_ID_KEY = "userId"; // Key for storing user ID in SharedPreferences
    private String userId; // Variable to hold user ID
    private EditText firstNameEditText, lastNameEditText, emailEditText, addressEditText, phoneEditText, bankAccountEditText, bankNameEditText;
    private Spinner categorySpinner; // Spinner for category selection
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_recipient_profile);

        // Retrieve userId from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        userId = sharedPreferences.getString(USER_ID_KEY, null);

        if (userId == null) {
            Toast.makeText(this, "User ID not found. Please log in again.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        // Initialize EditTexts, Spinner, and Button
        firstNameEditText = findViewById(R.id.firstNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        addressEditText = findViewById(R.id.addressEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        categorySpinner = findViewById(R.id.categorySpinner); // Initialize the spinner
        bankAccountEditText = findViewById(R.id.bankAccountEditText);
        bankNameEditText = findViewById(R.id.bankNameEditText);
        saveButton = findViewById(R.id.saveButton);

        // Load categories from arrays.xml into spinner
        loadCategories();

        // Use userId to load user data from Firebase and populate fields
        loadUserProfile(userId);

        // Set up the save button click listener
        saveButton.setOnClickListener(v -> saveUserProfile(userId));
    }

    private void loadCategories() {
        // Load categories from arrays.xml
        String[] categories = getResources().getStringArray(R.array.recipient_categories);

        // Set up the ArrayAdapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);
    }

    private void loadUserProfile(String userId) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String firstName = snapshot.child("firstName").getValue(String.class);
                    String lastName = snapshot.child("lastName").getValue(String.class);
                    String email = snapshot.child("email").getValue(String.class);
                    String address = snapshot.child("address").getValue(String.class);
                    String phoneNumber = snapshot.child("phoneNumber").getValue(String.class);
                    String category = snapshot.child("category").getValue(String.class);
                    String bankAccount = snapshot.child("bankAccount").getValue(String.class);
                    String bankName = snapshot.child("bankName").getValue(String.class);

                    // Set the EditText fields with fetched data
                    firstNameEditText.setText(firstName);
                    lastNameEditText.setText(lastName);
                    addressEditText.setText(address);
                    emailEditText.setText(email);
                    phoneEditText.setText(phoneNumber);
                    int spinnerPosition = ((ArrayAdapter<String>) categorySpinner.getAdapter()).getPosition(category);
                    categorySpinner.setSelection(spinnerPosition);
                    bankAccountEditText.setText(bankAccount);
                    bankNameEditText.setText(bankName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(RecipientProfileActivity.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveUserProfile(String userId) {
        String updatedFirstName = firstNameEditText.getText().toString();
        String updatedLastName = lastNameEditText.getText().toString();
        String updatedEmail = emailEditText.getText().toString();
        String updatedAddress = addressEditText.getText().toString();
        String updatedPhoneNumber = phoneEditText.getText().toString();
        String updatedCategory = categorySpinner.getSelectedItem().toString();
        String updatedBankAccount = bankAccountEditText.getText().toString();
        String updatedBankName = bankNameEditText.getText().toString();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId);
        databaseReference.child("firstName").setValue(updatedFirstName);
        databaseReference.child("lastName").setValue(updatedLastName);
        databaseReference.child("email").setValue(updatedEmail);
        databaseReference.child("address").setValue(updatedAddress);
        databaseReference.child("phoneNumber").setValue(updatedPhoneNumber);
        databaseReference.child("category").setValue(updatedCategory);
        databaseReference.child("bankAccount").setValue(updatedBankAccount);
        databaseReference.child("bankName").setValue(updatedBankName).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(RecipientProfileActivity.this, "Profile updated successfully.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RecipientProfileActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(RecipientProfileActivity.this, "Failed to update profile. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
