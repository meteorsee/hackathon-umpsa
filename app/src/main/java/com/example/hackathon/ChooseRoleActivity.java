package com.example.hackathon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class ChooseRoleActivity extends AppCompatActivity {

    private Button donorButton, recipientButton;
    private String firstName, lastName, email, uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_role);

        donorButton = findViewById(R.id.donorButton);
        recipientButton = findViewById(R.id.recipientButton);

        // Get user details passed from the login activity
        firstName = getIntent().getStringExtra("firstName");
        lastName = getIntent().getStringExtra("lastName");
        email = getIntent().getStringExtra("email");
        uid = getIntent().getStringExtra("uid");

        donorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent donorIntent = new Intent(ChooseRoleActivity.this, DonorProfileActivity.class);
                donorIntent.putExtra("firstName", firstName);
                donorIntent.putExtra("lastName", lastName);
                donorIntent.putExtra("email", email);
                donorIntent.putExtra("uid", uid);
                startActivity(donorIntent);
                finish();
            }
        });

        recipientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent recipientIntent = new Intent(ChooseRoleActivity.this, RecipientProfileActivity.class);
                recipientIntent.putExtra("firstName", firstName);
                recipientIntent.putExtra("lastName", lastName);
                recipientIntent.putExtra("email", email);
                recipientIntent.putExtra("uid", uid);
                startActivity(recipientIntent);
                finish();
            }
        });
    }
}
