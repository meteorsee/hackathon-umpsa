package com.example.hackathon.adapters; // FoodAdapter.java

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hackathon.R;
import com.example.hackathon.models.FoodItem;

import java.util.HashMap;
import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {
    private List<FoodItem> foodList;
    private HashMap<FoodItem, Integer> selectedFoodItems = new HashMap<>();

    public FoodAdapter(List<FoodItem> foodList) {
        this.foodList = foodList;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        FoodItem foodItem = foodList.get(position);
        holder.foodName.setText(foodItem.getName());
        holder.foodDescription.setText(foodItem.getDescription());
        holder.foodPrice.setText("$" + foodItem.getPrice());
        holder.foodImage.setImageResource(foodItem.getImageResourceId());

        holder.quantity = selectedFoodItems.getOrDefault(foodItem, 0);
        holder.quantityView.setText(String.valueOf(holder.quantity));

        holder.addButton.setOnClickListener(v -> {
            holder.quantity++;
            holder.quantityView.setText(String.valueOf(holder.quantity));
            selectedFoodItems.put(foodItem, holder.quantity);
        });

        holder.subtractButton.setOnClickListener(v -> {
            if (holder.quantity > 0) {
                holder.quantity--;
                holder.quantityView.setText(String.valueOf(holder.quantity));
                selectedFoodItems.put(foodItem, holder.quantity);
            }
        });
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public double getTotal() {
        double total = 0;
        for (HashMap.Entry<FoodItem, Integer> entry : selectedFoodItems.entrySet()) {
            total += entry.getKey().getPrice() * entry.getValue();
        }
        return total;
    }

    public static class FoodViewHolder extends RecyclerView.ViewHolder {
        TextView foodName;
        TextView foodDescription;
        TextView foodPrice;
        TextView quantityView; // New TextView for quantity display
        ImageView foodImage;
        Button addButton;
        Button subtractButton;
        int quantity; // To keep track of the quantity

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            foodName = itemView.findViewById(R.id.foodName);
            foodDescription = itemView.findViewById(R.id.foodDescription);
            foodPrice = itemView.findViewById(R.id.foodPrice);
            quantityView = itemView.findViewById(R.id.quantityView); // Reference to quantity TextView
            foodImage = itemView.findViewById(R.id.foodImage);
            addButton = itemView.findViewById(R.id.addButton);
            subtractButton = itemView.findViewById(R.id.subtractButton);
        }
    }
}
