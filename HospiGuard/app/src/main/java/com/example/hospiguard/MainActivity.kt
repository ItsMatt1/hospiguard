package com.example.hospiguard

import android.os.Bundle
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
import com.example.hospiguard.HospitalRoom

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HospiGuardTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Black
                ) {
                    HospiguardApp(onExit = { finish() })
                }
            }
        }
    }
}

@Composable
fun HospiguardApp(onExit: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HospiguardLogo()
        Spacer(modifier = Modifier.height(16.dp))
        HospiguardMenu(onExit = onExit)
    }
}

@Composable
fun HospiguardLogo() {
    Image(
        painter = painterResource(id = R.drawable.hospiguardlogo),
        contentDescription = null,
        modifier = Modifier
            .size(120.dp)
            .clip(shape = MaterialTheme.shapes.medium)
    )
}

@Composable
fun HospiguardMenu(onExit: () -> Unit) {
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
    }
}