package com.example.hackathon.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hackathon.R;
import com.example.hackathon.models.LeaderboardEntry;

import java.util.ArrayList;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.LeaderboardViewHolder> {

    private ArrayList<LeaderboardEntry> leaderboardList;

    public LeaderboardAdapter(ArrayList<LeaderboardEntry> leaderboardList) {
        this.leaderboardList = leaderboardList;
    }

    @NonNull
    @Override
    public LeaderboardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.leaderboard_item, parent, false);
        return new LeaderboardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LeaderboardViewHolder holder, int position) {
        LeaderboardEntry entry = leaderboardList.get(position);

        // Rank starts from 1
        holder.tvRank.setText("Rank: " + (position + 1));
        holder.tvDonorName.setText("Donor: " + entry.getName());
        holder.tvPoints.setText("Points: " + entry.getPoints());
    }

    @Override
    public int getItemCount() {
        return leaderboardList.size();
    }

    public static class LeaderboardViewHolder extends RecyclerView.ViewHolder {

        TextView tvRank, tvDonorName, tvPoints;

        public LeaderboardViewHolder(@NonNull View itemView) {
            super(itemView);

            tvRank = itemView.findViewById(R.id.tvRank);
            tvDonorName = itemView.findViewById(R.id.tvDonorName);
            tvPoints = itemView.findViewById(R.id.tvPoints);
        }
    }
}
