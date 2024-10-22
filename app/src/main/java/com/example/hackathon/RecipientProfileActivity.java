package com.example.hackathon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hackathon.models.Recipient;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RecipientProfileActivity extends AppCompatActivity {

    private EditText firstNameEditText, lastNameEditText, emailEditText, addressEditText, phoneEditText, bankNameEditText, bankAccountEditText;
    private Spinner categorySpinner;
    private Button saveButton;
    private DatabaseReference databaseReference;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipient_profile);

        // Initialize views
        firstNameEditText = findViewById(R.id.firstNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        addressEditText = findViewById(R.id.addressEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        bankNameEditText = findViewById(R.id.bankNameEditText);
        bankAccountEditText = findViewById(R.id.bankAccountEditText);
        categorySpinner = findViewById(R.id.categorySpinner);
        saveButton = findViewById(R.id.saveButton);

        // Set up the Spinner adapter after initializing the Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.recipient_categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        // Get Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        // Retrieve the UID from the Intent
        uid = getIntent().getStringExtra("uid");

        // Pre-fill fields if available
        String firstName = getIntent().getStringExtra("firstName");
        String lastName = getIntent().getStringExtra("lastName");
        String email = getIntent().getStringExtra("email");

        firstNameEditText.setText(firstName);
        lastNameEditText.setText(lastName);
        emailEditText.setText(email);

        // Handle the Save button click
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveRecipientProfile();
            }
        });
    }

    private void saveRecipientProfile() {
        String firstName = firstNameEditText.getText().toString().trim();
        String lastName = lastNameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String address = addressEditText.getText().toString().trim();
        String phoneNumber = phoneEditText.getText().toString().trim();
        String bankName = bankNameEditText.getText().toString().trim();
        String bankAccount = bankAccountEditText.getText().toString().trim();
        String category = categorySpinner.getSelectedItem().toString();

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || bankName.isEmpty() || bankAccount.isEmpty()) {
            Toast.makeText(RecipientProfileActivity.this, "Please fill out all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a User object with UID and save to Firebase Database
        Recipient recipient = new Recipient(firstName, lastName, email, address, phoneNumber, "Recipient", category, bankName, bankAccount);
        databaseReference.child(uid).setValue(recipient)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(RecipientProfileActivity.this, "Recipient profile saved successfully", Toast.LENGTH_SHORT).show();
                        // Redirect to MainActivity
                        Intent intent = new Intent(RecipientProfileActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(RecipientProfileActivity.this, "Failed to save recipient profile", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
