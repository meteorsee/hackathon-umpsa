package com.example.hackathon; // MapsActivity.java

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hackathon.adapters.RestaurantAdapter;
import com.example.hackathon.models.Restaurant;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {
    private MapView mapView;
    private GoogleMap googleMap;

    private List<Restaurant> restaurantList = new ArrayList<>();
    private List<Restaurant> searchResults = new ArrayList<>();
    private RestaurantAdapter adapter;
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Initialize data
        populateRestaurantList();

        // Set up RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RestaurantAdapter(searchResults);
        recyclerView.setAdapter(adapter);

        // Set up Search input
        EditText searchInput = findViewById(R.id.searchEditText); // Updated ID

        // Initialize views
        mapView = findViewById(R.id.mapView);
        // Initialize the map
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);

        // Optionally, you can also add a TextWatcher for real-time searching
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                performSearch(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        // No markers to display since we are not using them for sample data
    }

    private void populateRestaurantList() {
        restaurantList.add(new Restaurant("Pizza Palace", "123 Main St", "Italian"));
        restaurantList.add(new Restaurant("Sushi Central", "456 Elm St", "Japanese"));
        restaurantList.add(new Restaurant("Burger Haven", "789 Oak St", "American"));
        restaurantList.add(new Restaurant("Taco Town", "101 Pine St", "Mexican"));
        restaurantList.add(new Restaurant("Veggie Delight", "202 Maple Ave", "Vegetarian"));
        // Add more restaurants as needed
    }

    private void performSearch(String keyword) {
        searchResults.clear();
        for (Restaurant restaurant : restaurantList) {
            if (restaurant.getName().toLowerCase().contains(keyword.toLowerCase()) ||
                    restaurant.getType().toLowerCase().contains(keyword.toLowerCase())) {
                searchResults.add(restaurant);
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }
        mapView.onSaveInstanceState(mapViewBundle);
    }
}
