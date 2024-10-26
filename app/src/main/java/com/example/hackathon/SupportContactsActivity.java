package com.example.hackathon;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class SupportContactsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support_contacts);

        TextView debugText = findViewById(R.id.debugText);
        debugText.setText("Support Contacts Activity - Debug Mode");
    }
}
