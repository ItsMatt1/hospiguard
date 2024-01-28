package com.example.hospiguard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

data class TemperatureSensor(
    val temperature: Float,
    // Add more properties as needed
) : Sensor(
    sensorType = "Temperature",
    status = "Active"
)

@Composable
fun TemperatureSensorCard(sensor: TemperatureSensor) {
    // Implement a composable to display the information of a temperature sensor
    // You can use Card, Column, Text, or any other Compose components to design the card
    // Here's a simple example using Card and Column:
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color.White), // Set background color using the background modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = "Sensor Type: ${sensor.sensorType}")
            Text(text = "Status: ${sensor.status}")
            Text(text = "Temperature: ${sensor.temperature} °C")
            // Add more Text components for other properties if needed
        }
    }
}

@Composable
fun CurrentTemperatureScreen(currentTemperature: Float) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(16.dp)
    ) {
        Text(
            text = "Current Temperature:",
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "$currentTemperature °C",
            modifier = Modifier.padding(bottom = 16.dp)
        )
        // You can add more components or UI elements as needed
    }
}


@Composable
fun TemperatureSensorList(sensorList: List<TemperatureSensor>) {
    LazyColumn {
        items(sensorList) { sensor ->
            TemperatureSensorCard(sensor = sensor)
        }
    }
}
