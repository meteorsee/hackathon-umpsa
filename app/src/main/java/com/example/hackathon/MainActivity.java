package com.example.hackathon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView userGreeting;
    private Button makeDonationButton, viewHistoryButton, leaderboardButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        userGreeting = findViewById(R.id.userGreeting);
        makeDonationButton = findViewById(R.id.makeDonationButton);
        viewHistoryButton = findViewById(R.id.viewHistoryButton);
        leaderboardButton = findViewById(R.id.leaderboardButton);

        // Set User Name (assuming passed from login activity)
        String userName = getIntent().getStringExtra("userName");
        userGreeting.setText("Hello, " + userName + "!");

        // Navigate to Make a Donation Screen
        makeDonationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DonationActivity.class);
                startActivity(intent);
            }
        });

        // Navigate to Donation History
        viewHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
                startActivity(intent);
            }
        });

        // Navigate to Leaderboard
        leaderboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LeaderboardActivity.class);
                startActivity(intent);
            }
        });
    }
}
