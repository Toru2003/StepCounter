package com.example.stepcounter.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class OnRestartBroadcastReceiver() : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            val serviceIntent = Intent(context, StepCounterService::class.java)
            context.startService(serviceIntent)
        }
    }
}