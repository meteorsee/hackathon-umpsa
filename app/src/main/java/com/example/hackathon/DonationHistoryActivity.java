package com.example.hackathon;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hackathon.adapters.DonationAdapter;
import com.example.hackathon.models.Donation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class DonationHistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerViewDonations;
    private DonationAdapter donationAdapter;
    private ArrayList<Donation> donationList = new ArrayList<>();

    private DatabaseReference donationsRef;

    private String userId;

    private static final String PREFS_NAME = "MyPrefs";
    private static final String USER_ID_KEY = "userId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation_history);

        // Retrieve userId from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        userId = sharedPreferences.getString(USER_ID_KEY, null);

        if (userId == null) {
            // If user is not logged in, close the activity
            Toast.makeText(this, "Please log in to view donation history", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize Firebase Database Reference
        donationsRef = FirebaseDatabase.getInstance().getReference("donations");

        // Initialize RecyclerView
        recyclerViewDonations = findViewById(R.id.recyclerViewDonations);
        recyclerViewDonations.setLayoutManager(new LinearLayoutManager(this));
        donationAdapter = new DonationAdapter(donationList);
        recyclerViewDonations.setAdapter(donationAdapter);

        // Fetch Donation History from Firebase
        fetchDonationHistory();
    }

    private void fetchDonationHistory() {
        donationsRef.orderByChild("userId").equalTo(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                donationList.clear(); // Clear the list before adding new data

                for (DataSnapshot donationSnapshot : snapshot.getChildren()) {
                    Donation donation = donationSnapshot.getValue(Donation.class);
                    if (donation != null) {
                        donationList.add(donation);
                    }
                }

                donationAdapter.notifyDataSetChanged(); // Refresh the RecyclerView
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("DonationHistory", "Error fetching donation history", error.toException());
                Toast.makeText(DonationHistoryActivity.this, "Failed to load donation history", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Helper function to format the timestamp
    private String formatTimestamp(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm a", Locale.getDefault());
        Date resultDate = new Date(timestamp);
        return sdf.format(resultDate);
    }
}
