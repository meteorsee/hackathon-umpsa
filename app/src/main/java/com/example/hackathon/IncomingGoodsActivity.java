package com.example.hackathon;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class IncomingGoodsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incoming_goods);

        TextView debugText = findViewById(R.id.debugText);
        debugText.setText("Incoming Goods Activity - Debug Mode");
    }
}
