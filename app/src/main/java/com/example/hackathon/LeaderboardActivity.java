package com.example.hackathon;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hackathon.models.LeaderboardEntry;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class LeaderboardActivity extends AppCompatActivity {

    private RecyclerView recyclerViewLeaderboard;
    private LeaderboardAdapter leaderboardAdapter;
    private ArrayList<LeaderboardEntry> leaderboardList = new ArrayList<>();

    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        // Initialize Firebase Database Reference
        usersRef = FirebaseDatabase.getInstance().getReference("users");

        // Initialize RecyclerView
        recyclerViewLeaderboard = findViewById(R.id.recyclerViewLeaderboard);
        recyclerViewLeaderboard.setLayoutManager(new LinearLayoutManager(this));
        leaderboardAdapter = new LeaderboardAdapter(leaderboardList);
        recyclerViewLeaderboard.setAdapter(leaderboardAdapter);

        // Fetch Leaderboard Data from Firebase
        fetchLeaderboardData();
    }

    private void fetchLeaderboardData() {
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                leaderboardList.clear(); // Clear the list before adding new data

                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    String name = userSnapshot.child("name").getValue(String.class);
                    Long points = userSnapshot.child("points").getValue(Long.class);

                    if (name != null && points != null) {
                        LeaderboardEntry entry = new LeaderboardEntry(name, points);
                        leaderboardList.add(entry);
                    }
                }

                // Sort by points (descending order)
                Collections.sort(leaderboardList, new Comparator<LeaderboardEntry>() {
                    @Override
                    public int compare(LeaderboardEntry o1, LeaderboardEntry o2) {
                        return Long.compare(o2.getPoints(), o1.getPoints());
                    }
                });

                leaderboardAdapter.notifyDataSetChanged(); // Refresh the RecyclerView
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("LeaderboardActivity", "Error fetching leaderboard data", error.toException());
                Toast.makeText(LeaderboardActivity.this, "Failed to load leaderboard", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
