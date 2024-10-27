package com.example.hackathon.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hackathon.R;
import com.example.hackathon.models.IncomingGoods;

import java.util.List;

public class IncomingGoodsAdapter extends RecyclerView.Adapter<IncomingGoodsAdapter.ViewHolder> {
    private List<IncomingGoods> incomingGoodsList;

    public IncomingGoodsAdapter(List<IncomingGoods> incomingGoodsList) {
        this.incomingGoodsList = incomingGoodsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_incoming_goods, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        IncomingGoods goods = incomingGoodsList.get(position);
        holder.nameTextView.setText(goods.getName());
        holder.categoryTextView.setText(goods.getCategory());
        holder.quantityTextView.setText(goods.getQuantity());
    }

    @Override
    public int getItemCount() {
        return incomingGoodsList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView categoryTextView;
        TextView quantityTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            categoryTextView = itemView.findViewById(R.id.categoryTextView);
            quantityTextView = itemView.findViewById(R.id.quantityTextView);
        }
    }
}
