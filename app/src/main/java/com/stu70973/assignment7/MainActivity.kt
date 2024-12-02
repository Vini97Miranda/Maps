package com.stu70973.assignment7

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

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
    val markerState = rememberMarkerState(position = LatLng(53.349722, -6.260278))
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(markerState.position, 12f)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(16.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Input Fields
                OutlinedTextField(
                    value = latitude,
                    onValueChange = { latitude = it },
                    label = { Text("Enter Latitude") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = latitude.toDoubleOrNull()?.let { it !in -90.0..90.0 } == true
                )
                OutlinedTextField(
                    value = longitude,
                    onValueChange = { longitude = it },
                    label = { Text("Enter Longitude") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = longitude.toDoubleOrNull()?.let { it !in -180.0..180.0 } == true
                )

                // Button to Place Marker
                Button(
                    onClick = {
                        val lat = latitude.toDoubleOrNull()
                        val lng = longitude.toDoubleOrNull()
                        if (lat != null && lng != null && lat in -90.0..90.0 && lng in -180.0..180.0) {
                            markerState.position = LatLng(lat, lng)
                            cameraPositionState.position = CameraPosition.fromLatLngZoom(markerState.position, 10f)
                        } else {
                            // Show error (could use Snackbar or Toast)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Place Marker")
                }
            }
        }

        // Google Map
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            onMapClick = { latLng: LatLng ->
                markerState.position = latLng
                cameraPositionState.position = CameraPosition.fromLatLngZoom(markerState.position, 10f)
            }
        ) {
            Marker(
                state = markerState,
                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED),
                draggable = true
            )
        }
    }
}