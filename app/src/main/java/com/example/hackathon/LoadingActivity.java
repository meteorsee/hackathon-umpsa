package com.example.hackathon;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LoadingActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private TextView percentageText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading); // Use your layout XML

        progressBar = findViewById(R.id.progressBar);
        percentageText = findViewById(R.id.percentageText);

        startCountdown();
    }

    private void startCountdown() {
        new CountDownTimer(5000, 50) { // 5000ms = 5 seconds, tick every 50ms
            int[] progress = {0}; // Use an array to update progress in inner class

            @Override
            public void onTick(long millisUntilFinished) {
                progress[0] += 1; // Increment progress
                progressBar.setProgress(progress[0]);
                percentageText.setText(progress[0] + "%");
            }

            @Override
            public void onFinish() {
                progressBar.setProgress(100);
                percentageText.setText("100%");
                goToLoginActivity();
            }
        }.start();
    }

    private void goToLoginActivity() {
        Intent intent = new Intent(LoadingActivity.this, LoginActivity.class);
        startActivity(intent);
        finish(); // Close the loading activity
    }
}
