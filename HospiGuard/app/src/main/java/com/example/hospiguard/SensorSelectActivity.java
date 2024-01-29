package com.example.hospiguard;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class SensorSelectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_sensor_select);

        // Assuming you have buttons in your layout with these IDs
        Button luminosityButton = findViewById(R.id.luminosityButton);
        Button temperatureButton = findViewById(R.id.temperatureButton);
        Button heartRateButton = findViewById(R.id.heartRateButton);

        luminosityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Replace the current fragment with the LightSensorFragment
                replaceFragment(new LightSensorFragment());
            }
        });

        temperatureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Replace the current fragment with the TemperatureSensorFragment
                replaceFragment(new TemperatureSensorFragment());
            }
        });

        heartRateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Replace the current fragment with the HeartRateSensorFragment
                replaceFragment(new HeartRateSensorFragment());
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, fragment); // R.id.fragmentContainer should be the ID of the container in your layout
        fragmentTransaction.addToBackStack(null); // Optional: Add to back stack if you want to navigate back
        fragmentTransaction.commit();
    }
}