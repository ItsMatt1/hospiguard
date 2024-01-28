package com.example.hospiguard

import androidx.compose.runtime.Composable

open class Sensor(
    val sensorType: String,
    val status: String,
    // Add more properties as needed
)

@Composable
fun SensorCard(sensor: Sensor) {
    // Implement a composable to display the information of a sensor
}