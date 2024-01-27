package com.example.hospiguard

import androidx.compose.runtime.Composable

data class HospitalRoom(
    val roomNumber: Int,
    val temperature: Float,
    val humidity: Float,
    val isOccupied: Boolean,

)

@Composable
fun HospitalRoomCard(hospitalRoom: HospitalRoom) {
    // Implement a composable to display the information of a hospital room
}