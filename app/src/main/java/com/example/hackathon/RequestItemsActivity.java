package com.example.hackathon;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class RequestItemsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_items);

        TextView debugText = findViewById(R.id.debugText);
        debugText.setText("Request Items Activity - Debug Mode");
    }
}
