package com.example.hackathon.models;

public class LeaderboardEntry {
    private String name;
    private long points;

    public LeaderboardEntry() {
        // Default constructor required for Firebase
    }

    public LeaderboardEntry(String name, long points) {
        this.name = name;
        this.points = points;
    }

    public String getName() {
        return name;
    }

    public long getPoints() {
        return points;
    }
}
