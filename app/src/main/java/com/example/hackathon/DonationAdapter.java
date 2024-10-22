package com.example.hackathon;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hackathon.models.Donation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class DonationAdapter extends RecyclerView.Adapter<DonationAdapter.DonationViewHolder> {

    private ArrayList<Donation> donations;

    public DonationAdapter(ArrayList<Donation> donations) {
        this.donations = donations;
    }

    @NonNull
    @Override
    public DonationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.donation_item, parent, false);
        return new DonationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DonationViewHolder holder, int position) {
        Donation donation = donations.get(position);

        holder.tvFoodBank.setText("Food Bank: " + donation.getFoodBank());
        holder.tvDonationType.setText("Type: " + donation.getDonationType());
        holder.tvDonationDetails.setText("Details: " + donation.getDonationDetails());

        // Format and set the timestamp
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm a", Locale.getDefault());
        Date resultDate = new Date(donation.getTimestamp());
        holder.tvDonationTimestamp.setText("Date: " + sdf.format(resultDate));
    }

    @Override
    public int getItemCount() {
        return donations.size();
    }

    public static class DonationViewHolder extends RecyclerView.ViewHolder {

        TextView tvFoodBank, tvDonationType, tvDonationDetails, tvDonationTimestamp;

        public DonationViewHolder(@NonNull View itemView) {
            super(itemView);

            tvFoodBank = itemView.findViewById(R.id.tvFoodBank);
            tvDonationType = itemView.findViewById(R.id.tvDonationType);
            tvDonationDetails = itemView.findViewById(R.id.tvDonationDetails);
            tvDonationTimestamp = itemView.findViewById(R.id.tvDonationTimestamp);
        }
    }
}
