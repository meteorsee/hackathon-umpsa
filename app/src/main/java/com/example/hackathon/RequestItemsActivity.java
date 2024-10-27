package com.example.hackathon;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hackathon.models.RequestItem;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RequestItemsActivity extends AppCompatActivity {
    private EditText itemNameEditText;
    private EditText itemCategoryEditText;
    private EditText itemQuantityEditText;
    private Button requestButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_items);

        itemNameEditText = findViewById(R.id.itemNameEditText);
        itemCategoryEditText = findViewById(R.id.itemCategoryEditText);
        itemQuantityEditText = findViewById(R.id.itemQuantityEditText);
        requestButton = findViewById(R.id.requestButton);

        requestButton.setOnClickListener(v -> submitRequest());
    }

    private void submitRequest() {
        String itemName = itemNameEditText.getText().toString().trim();
        String itemCategory = itemCategoryEditText.getText().toString().trim();
        String itemQuantity = itemQuantityEditText.getText().toString().trim();

        if (TextUtils.isEmpty(itemName) || TextUtils.isEmpty(itemCategory) || TextUtils.isEmpty(itemQuantity)) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a new request
        RequestItem requestItem = new RequestItem(itemName, itemCategory, itemQuantity);

        // Save to Firebase
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("request_items");
        databaseReference.push().setValue(requestItem).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(RequestItemsActivity.this, "Request submitted successfully", Toast.LENGTH_SHORT).show();
                // Clear input fields after submission
                itemNameEditText.setText("");
                itemCategoryEditText.setText("");
                itemQuantityEditText.setText("");
            } else {
                Toast.makeText(RequestItemsActivity.this, "Failed to submit request: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
