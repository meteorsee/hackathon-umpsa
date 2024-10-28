package com.example.hackathon; // FoodMenuActivity.java

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hackathon.adapters.FoodAdapter;
import com.example.hackathon.models.FoodItem;

import java.util.ArrayList;
import java.util.List;

public class FoodMenuActivity extends AppCompatActivity {

    private TextView restaurantName;
    private TextView restaurantDescription;
    private RecyclerView foodRecyclerView;
    private FoodAdapter foodAdapter;
    private List<FoodItem> foodList = new ArrayList<>();
    private Button checkoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_menu);

        // Get data from Intent
        String name = getIntent().getStringExtra("RESTAURANT_NAME");
        String description = getIntent().getStringExtra("RESTAURANT_DESCRIPTION");

        // Ensure values are not null
        if (name == null) name = "Unknown Restaurant";
        if (description == null) description = "No description available.";

        // Initialize views
        restaurantName = findViewById(R.id.restaurantName);
        restaurantDescription = findViewById(R.id.restaurantDescription);
        foodRecyclerView = findViewById(R.id.foodRecyclerView);
        checkoutButton = findViewById(R.id.checkoutButton);

        // Set restaurant details
        restaurantName.setText(name);
        restaurantDescription.setText(description);

        // Populate food list
        populateFoodList();

        // Set up RecyclerView for food items
        foodRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        foodAdapter = new FoodAdapter(foodList);
        foodRecyclerView.setAdapter(foodAdapter);

        // Set up checkout button click listener
        String finalName = name;
        checkoutButton.setOnClickListener(v -> {
            double totalAmount = foodAdapter.getTotal();
            Intent checkoutIntent = new Intent(FoodMenuActivity.this, CheckoutActivity.class);
            checkoutIntent.putExtra("RESTAURANT_NAME", finalName);
            checkoutIntent.putExtra("TOTAL_AMOUNT", String.valueOf(totalAmount));
            startActivity(checkoutIntent);
        });

    }

    private void populateFoodList() {
        // Sample food items (replace with actual data)
        foodList.add(new FoodItem("Pizza", "Delicious cheese pizza", 9.99, R.drawable.pizza));
        foodList.add(new FoodItem("Sushi", "Fresh sushi rolls", 12.99, R.drawable.sushi));
        foodList.add(new FoodItem("Burger", "Juicy beef burger", 8.99, R.drawable.burger));
        // Add more food items as needed
    }

    private void proceedToCheckout() {
        // Calculate total amount (you'll need to implement your own logic here)
        double totalAmount = calculateTotalAmount();

        // Create intent to start CheckoutActivity
        Intent intent = new Intent(FoodMenuActivity.this, CheckoutActivity.class);
        intent.putExtra("RESTAURANT_NAME", restaurantName.getText().toString());
        intent.putExtra("TOTAL_AMOUNT", totalAmount);
        startActivity(intent);
    }

    private double calculateTotalAmount() {
        double total = 0;
        // Here, add your logic to calculate total amount based on selected food items
        // For example, if you have a method to get selected items from FoodAdapter, use it.
        // total += selectedFoodItem.getPrice();
        return total; // Return the calculated total
    }
}
