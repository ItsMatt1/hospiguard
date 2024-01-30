package com.example.hospiguard;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class SensorSelectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_sensor_select);

        // Assuming you have buttons in your layout with these IDs
        Button luminosityButton = findViewById(R.id.luminosityButton);
        Button temperatureButton = findViewById(R.id.temperatureButton);
        Button heartRateButton = findViewById(R.id.heartRateButton);

        // Set click listeners for each button
        luminosityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch LuminositySensorActivity
                Intent intent = new Intent(SensorSelectActivity.this, LightSensorActivity.class);
                startActivity(intent);
            }
        });

        temperatureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch TemperatureSensorActivity
                Intent intent = new Intent(SensorSelectActivity.this, TemperatureSensorActivity.class);
                startActivity(intent);
            }
        });

        heartRateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch HeartRateSensorActivity
                Intent intent = new Intent(SensorSelectActivity.this, HeartRateSensorActivity.class);
                startActivity(intent);
            }
        });
    }
}