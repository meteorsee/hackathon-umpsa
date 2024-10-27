package com.example.hackathon;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hackathon.adapters.IncomingGoodsAdapter;
import com.example.hackathon.models.IncomingGoods;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class IncomingGoodsActivity extends AppCompatActivity {
    private RecyclerView incomingGoodsRecyclerView;
    private IncomingGoodsAdapter incomingGoodsAdapter;
    private List<IncomingGoods> incomingGoodsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incoming_goods);

        incomingGoodsRecyclerView = findViewById(R.id.incomingGoodsRecyclerView);
        incomingGoodsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        incomingGoodsList = new ArrayList<>();
        incomingGoodsAdapter = new IncomingGoodsAdapter(incomingGoodsList);
        incomingGoodsRecyclerView.setAdapter(incomingGoodsAdapter);

        loadIncomingGoods();
    }

    private void loadIncomingGoods() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("incoming_goods");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                incomingGoodsList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    IncomingGoods goods = dataSnapshot.getValue(IncomingGoods.class);
                    if (goods != null) {
                        incomingGoodsList.add(goods);
                    }
                }
                incomingGoodsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(IncomingGoodsActivity.this, "Failed to load goods: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
