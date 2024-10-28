package com.example.hackathon;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CheckoutActivity extends AppCompatActivity {

    private TextView restaurantDetails;
    private TextView totalAmount;
    private RadioGroup paymentOptions;
    private Button payNowButton;
    private TextView countdownText; // TextView to display countdown
    private View successLayout; // Layout for success message

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        restaurantDetails = findViewById(R.id.restaurant_details);
        totalAmount = findViewById(R.id.total_amount);
        paymentOptions = findViewById(R.id.payment_options);
        payNowButton = findViewById(R.id.button_pay_now);
        countdownText = findViewById(R.id.countdown_text); // Initialize countdown TextView
        successLayout = findViewById(R.id.success_layout); // Initialize success layout

        // Assuming these values are passed from the previous activity
        String restaurantName = getIntent().getStringExtra("RESTAURANT_NAME");
        String amount = getIntent().getStringExtra("TOTAL_AMOUNT");

        restaurantDetails.setText(restaurantName);
        totalAmount.setText("Total Amount: $" + amount);

        payNowButton.setOnClickListener(v -> {
            int selectedPaymentId = paymentOptions.getCheckedRadioButtonId();
            RadioButton selectedPayment = findViewById(selectedPaymentId);

            if (selectedPayment != null) {
                String paymentMethod = selectedPayment.getText().toString();
                // Proceed with payment processing (you can implement payment APIs here)
                Toast.makeText(this, "Processing payment via " + paymentMethod, Toast.LENGTH_SHORT).show();

                // Show success layout and start countdown
                showSuccessScreen();
            } else {
                Toast.makeText(this, "Please select a payment method", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showSuccessScreen() {
        // Set the success layout visible with a green background
        successLayout.setVisibility(View.VISIBLE);
        successLayout.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light)); // Set green background

        // Start countdown for 5 seconds
        new CountDownTimer(5000, 1000) {
            int secondsRemaining = 5;

            @Override
            public void onTick(long millisUntilFinished) {
                countdownText.setText("Redirecting in " + secondsRemaining + " seconds...");
                secondsRemaining--;
            }

            @Override
            public void onFinish() {
                countdownText.setText("Redirecting now...");
                // Redirect to MainActivity
                Intent intent = new Intent(CheckoutActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // Optional: Call finish() to close the CheckoutActivity
            }
        }.start();
    }
}
