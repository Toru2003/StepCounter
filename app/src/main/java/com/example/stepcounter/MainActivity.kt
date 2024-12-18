package com.example.stepcounter

import android.Manifest
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.app.ActivityCompat
import com.example.stepcounter.navigation.NavigationGraph
import com.example.stepcounter.ui.theme.StepCounterTheme
import com.example.stepcounter.utils.StepCounterService
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            StepCounterTheme {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        Manifest.permission.ACTIVITY_RECOGNITION,
                        Manifest.permission.POST_NOTIFICATIONS,
                        Manifest.permission.RECEIVE_BOOT_COMPLETED,
                        Manifest.permission.SET_ALARM,
                        Manifest.permission.FOREGROUND_SERVICE_HEALTH,
                        Manifest.permission.FOREGROUND_SERVICE
                    ), 1
                )
                NavigationGraph()
            }
        }
        val serviceIntent = Intent(this, StepCounterService::class.java)

        startService(serviceIntent)

    }

}
