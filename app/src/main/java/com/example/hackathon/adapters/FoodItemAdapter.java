package com.example.hackathon.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hackathon.R;
import com.example.hackathon.models.FoodItem;

import java.util.List;

public class FoodItemAdapter extends RecyclerView.Adapter<FoodItemAdapter.FoodItemViewHolder> {

    private List<FoodItem> foodItems;

    public FoodItemAdapter(List<FoodItem> foodItems) {
        this.foodItems = foodItems;
    }

    @NonNull
    @Override
    public FoodItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_food, parent, false);
        return new FoodItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodItemViewHolder holder, int position) {
        FoodItem foodItem = foodItems.get(position);
        holder.foodName.setText(foodItem.getName());
        holder.checkBox.setChecked(foodItem.isSelected());

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> foodItem.setSelected(isChecked));
    }

    @Override
    public int getItemCount() {
        return foodItems.size();
    }

    static class FoodItemViewHolder extends RecyclerView.ViewHolder {
        TextView foodName;
        CheckBox checkBox;

        FoodItemViewHolder(View itemView) {
            super(itemView);
            foodName = itemView.findViewById(R.id.foodName);
            checkBox = itemView.findViewById(R.id.foodCheckBox);
        }
    }
}
