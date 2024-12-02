package com.stu70973.assignment7

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.maps.android.compose.*
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize Google Maps SDK
        MapsInitializer.initialize(applicationContext)

        // Google Places API directly with the key
        Places.initialize(applicationContext, "AIzaSyCuPW6nrdeRxnEw7e3PebrBfcOFONqQiDw")

        setContent {
            GoogleMapWithMarker()
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun GoogleMapWithMarker() {
    var latitude by remember { mutableStateOf("") }
    var longitude by remember { mutableStateOf("") }
    var markerPosition by remember { mutableStateOf(LatLng(0.0, 0.0)) }

    Column(modifier = Modifier.fillMaxSize()) {
        // Input Fields
        OutlinedTextField(
            value = latitude,
            onValueChange = { latitude = it },
            label = { Text("Enter Latitude") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = longitude,
            onValueChange = { longitude = it },
            label = { Text("Enter Longitude") },
            modifier = Modifier.fillMaxWidth()
        )

        // Button to Place Marker
        Button(
            onClick = {
                val lat = latitude.toDoubleOrNull()
                val lng = longitude.toDoubleOrNull()
                if (lat != null && lng != null) {
                    markerPosition = LatLng(lat, lng) // Update marker position
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Place Marker")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Google Map
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(markerPosition, 10f) // Camera focuses on the marker
            }
        ) {
            Marker(
                state = MarkerState(position = markerPosition), // Correct parameter
                title = "Custom Marker",
                snippet = "Lat: ${markerPosition.latitude}, Lng: ${markerPosition.longitude}"
            )
        }
    }
}