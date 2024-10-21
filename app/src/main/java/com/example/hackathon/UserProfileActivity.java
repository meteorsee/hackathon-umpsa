package com.example.hackathon;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
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

public class UserProfileActivity extends AppCompatActivity {

    private EditText nameEditText, phoneEditText, addressEditText, bankNameEditText, bankAccountEditText;
    private Button saveButton;
    private DatabaseReference databaseReference;
    private String uid;
    private String role; // User role (donor or recipient)

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // Initialize views
        nameEditText = findViewById(R.id.nameEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        addressEditText = findViewById(R.id.addressEditText);
        bankNameEditText = findViewById(R.id.bankNameEditText);
        bankAccountEditText = findViewById(R.id.bankAccountEditText);
        saveButton = findViewById(R.id.saveButton);

        // Get Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        // Retrieve the UID and role from the Intent
        uid = getIntent().getStringExtra("uid");
        role = getIntent().getStringExtra("role"); // Pass the role to this activity

        // Load existing user details
        loadUserDetails();

        // Handle the Save button click
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserProfile();
            }
        });
    }

    private void loadUserDetails() {
        databaseReference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String name = dataSnapshot.child("firstName").getValue(String.class) + " " +
                            dataSnapshot.child("lastName").getValue(String.class);
                    String phone = dataSnapshot.child("phone").getValue(String.class);
                    String address = dataSnapshot.child("address").getValue(String.class);

                    // Set the common fields
                    nameEditText.setText(name);
                    phoneEditText.setText(phone);
                    addressEditText.setText(address);

                    // Check user role to display additional fields
                    if (role.equals("Recipient")) {
                        String bankName = dataSnapshot.child("bankName").getValue(String.class);
                        String bankAccount = dataSnapshot.child("bankAccount").getValue(String.class);
                        bankNameEditText.setText(bankName);
                        bankAccountEditText.setText(bankAccount);
                    } else if (role.equals("Donor")) {
                        // Hide bank fields for donor
                        bankNameEditText.setVisibility(View.GONE);
                        bankAccountEditText.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(UserProfileActivity.this, "Failed to load user details", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveUserProfile() {
        String name = nameEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        String address = addressEditText.getText().toString().trim();
        String bankName = bankNameEditText.getText().toString().trim();
        String bankAccount = bankAccountEditText.getText().toString().trim();

        if (name.isEmpty() || phone.isEmpty() || address.isEmpty() || (role.equals("Recipient") && (bankName.isEmpty() || bankAccount.isEmpty()))) {
            Toast.makeText(UserProfileActivity.this, "Please fill out all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Update the user details in the database
        databaseReference.child(uid).child("firstName").setValue(name.split(" ")[0]);
        databaseReference.child(uid).child("lastName").setValue(name.split(" ")[1]);
        databaseReference.child(uid).child("phone").setValue(phone);
        databaseReference.child(uid).child("address").setValue(address);

        if (role.equals("Recipient")) {
            databaseReference.child(uid).child("bankName").setValue(bankName);
            databaseReference.child(uid).child("bankAccount").setValue(bankAccount);
        } else if (role.equals("Donor")) {
            // Ensure bank fields are removed for donors if they exist
            databaseReference.child(uid).child("bankName").removeValue();
            databaseReference.child(uid).child("bankAccount").removeValue();
        }

        Toast.makeText(UserProfileActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
        finish(); // Close the activity and return to MainActivity
    }
}
