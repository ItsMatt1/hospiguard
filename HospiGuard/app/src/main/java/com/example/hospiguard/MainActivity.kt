package com.example.hospiguard

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.hospiguard.ui.theme.HospiGuardTheme

class MainActivity : ComponentActivity() {
    private lateinit var sensorManager: SensorManager
    private lateinit var temperatureSensor: Sensor
    private val temperatureSensorList = mutableListOf<TemperatureSensor>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        temperatureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)!!

        if (temperatureSensor != null) {
            setContent {
                HospiGuardTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = Color.Black
                    ) {
                        HospiguardApp(onExit = { finish() }, temperatureSensorList) {
                            startActivity(Intent(this@MainActivity, TemperatureSensor::class.java))
                        }
                    }
                }
            }
        } else {
            Log.d("MainActivity2", "Ambient temperature sensor is not available on this device.")
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(temperatureListener, temperatureSensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(temperatureListener)
    }

    private val temperatureListener = object : SensorEventListener {
        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

        override fun onSensorChanged(event: SensorEvent) {
            if (event.sensor.type == Sensor.TYPE_AMBIENT_TEMPERATURE) {
                val temperature = event.values[0]
                temperatureSensorList.clear()
                temperatureSensorList.add(TemperatureSensor(temperature))
            }
        }
    }
}

@Composable
fun HospiguardApp(onExit: () -> Unit, temperatureSensorList: List<TemperatureSensor>, onTemperatureButtonClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top, // Updated to place the logo at the top
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HospiguardLogo()
        Spacer(modifier = Modifier.height(16.dp))
        TemperatureSensorList(sensorList = temperatureSensorList)
        Spacer(modifier = Modifier.height(16.dp))
        HospiguardMenu(onExit = onExit, onTemperatureButtonClick = onTemperatureButtonClick)
    }
}


@Composable
fun HospiguardLogo() {
    Image(
        painter = painterResource(id = R.drawable.hospiguardlogo),
        contentDescription = null,
        modifier = Modifier
            .size(300.dp)
            .clip(shape = MaterialTheme.shapes.medium)
    )
}

@Composable
fun HospiguardMenu(onExit: () -> Unit, onTemperatureButtonClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Button(onClick = { /* Handle login action */ }) {
            Text(text = "Login")
        }

        Spacer(modifier = Modifier.width(16.dp))

        Button(onClick = {
            // Handle exit action
            onExit.invoke()
        }) {
            Text(text = "Exit")
        }

        Spacer(modifier = Modifier.width(16.dp))

        Button(onClick = {
            // Handle temperature button click
            onTemperatureButtonClick.invoke()
        }) {
            Text(text = "Temperature")
        }
    }
}