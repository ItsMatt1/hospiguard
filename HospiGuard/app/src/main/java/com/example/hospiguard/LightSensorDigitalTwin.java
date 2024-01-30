package com.example.hospiguard;

public class LightSensorDigitalTwin {

    // Additional properties representing the digital twin state
    private String deviceId;
    private boolean isConnected;
    private float currentLuxValue;

    // Constructor
    public LightSensorDigitalTwin(String deviceId) {
        this.deviceId = deviceId;
        this.isConnected = false;
        this.currentLuxValue = 0.0f;
    }

    // Connect to the physical sensor
    public void connect() {
        isConnected = true;
    }

    // Disconnect from the physical sensor
    public void disconnect() {
        isConnected = false;
    }

    // Update the digital twin with a new Lux value
    public void updateLuxValue(float newLuxValue) {
        currentLuxValue = newLuxValue;
    }

    // Get the current Lux value from the digital twin
    public float getCurrentLuxValue() {
        return currentLuxValue;
    }

}
