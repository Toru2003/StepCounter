package com.example.stepcounter.utils

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.IBinder
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.stepcounter.data.local.room.steps.DaysDatabase
import com.example.stepcounter.utils.Utils.createDefaultDate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import java.util.Calendar
import javax.inject.Inject

@AndroidEntryPoint
class StepCounterService() : Service(), SensorEventListener {
    @Inject
    lateinit var database: DaysDatabase

    @Inject
    lateinit var stepsManager: StepsManager


    companion object {
        const val CHANNEL_ID = "StepCounterChannel2"
        const val NOTIFICATION_ID = 113
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACTIVITY_RECOGNITION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            startForeground(NOTIFICATION_ID, createNotification().build())
            val sensorManager: SensorManager =
                getSystemService(Context.SENSOR_SERVICE) as SensorManager
            val stepSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
            if (stepSensor != null) {
                sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_NORMAL)
            }
        }
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_STEP_COUNTER) {
            runBlocking {
                val day = database.daysDao().getDay(createDefaultDate())

                if (day != null) {
                    val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
                    val steps = day.stepsByHour.toMutableList()

                    val lastSteps = stepsManager.readLastSteps().first() ?: 0
                    if (currentHour < steps.size) {
                        steps[currentHour] += (event.values[0] - if (lastSteps > 0) lastSteps else event.values[0].toInt()).toInt()
                        stepsManager.saveLastSteps(event.values[0].toInt())
                    }
                    day.stepsByHour = steps
                    database.daysDao().update(day)

                    if (steps.sum() > day.maxSteps && runBlocking { stepsManager.readTargetSuccess() } ?: true) {
                        createTargetAchievedNotification()
                    }
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Step Counter Channel",
                NotificationManager.IMPORTANCE_HIGH
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    private fun createNotification(): NotificationCompat.Builder {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Счетчик шагов")
            .setContentText("Обновление показателей шагов")
            .setPriority(NotificationCompat.PRIORITY_LOW)
    }

    private fun createTargetAchievedNotification(): NotificationCompat.Builder {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Цели достигнуты")
            .setContentText("Вы выполнили цели по шагам!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
    }
}


class StepsManager @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) {
    private val lastStepsKey = intPreferencesKey("lastStepsKey")
    private val lastTargetSuccessKey = stringPreferencesKey("LastTargetSuccessKey")

    suspend fun readTargetSuccess(): Boolean? {
        val data = dataStore.data.firstOrNull()?.get(lastTargetSuccessKey)
        if (data == createDefaultDate().toString()) {
            return false
        } else {
            saveTargetSuccess()
            return true
        }
    }

    private suspend fun saveTargetSuccess() {
        dataStore.edit { preferences ->
            preferences[lastTargetSuccessKey] = createDefaultDate().toString()
        }
    }

    suspend fun readLastSteps(): Flow<Int?> = dataStore.data.map { preferences ->
        preferences[lastStepsKey]
    }

    suspend fun saveLastSteps(value: Int) {
        dataStore.edit { preferences ->
            preferences[lastStepsKey] = value
        }
    }
}