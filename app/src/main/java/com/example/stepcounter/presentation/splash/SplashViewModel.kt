package com.example.stepcounter.presentation.splash

import android.icu.util.Calendar
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stepcounter.data.local.room.steps.DaysDatabase
import com.example.stepcounter.domain.models.Day
import com.example.stepcounter.utils.StepsManager
import com.example.stepcounter.utils.Utils.createDefaultDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    val daysDatabase: DaysDatabase,
    private val stepsManager: StepsManager,
) : ViewModel() {

    suspend fun check(action: () -> Unit) {
        withContext(Dispatchers.Main) {
            if (daysDatabase.daysDao().getCount() == 0) {
                createRandomDays()
                stepsManager.saveLastSteps(-1)
            } else if (daysDatabase.daysDao().getDay(createDefaultDate()) == null) {
                val current = Calendar.getInstance()
                current.time = createDefaultDate()
                current.add(java.util.Calendar.DAY_OF_YEAR, -1)
                val lastDay = daysDatabase.daysDao().getDay(current.time)
                daysDatabase.daysDao().insert(
                    Day(
                        maxSteps = lastDay?.maxSteps ?: 10000,
                        date = createDefaultDate()
                    )
                )
            }
        }
        action()
    }

    fun createRandomDays() {
        viewModelScope.launch {
            val calendar = Calendar.getInstance()
            calendar.time = createDefaultDate()
            daysDatabase.daysDao().insert(
                Day(date = createDefaultDate())
            )
            for (i in 1..25) {
                calendar.time = createDefaultDate()
                calendar.add(java.util.Calendar.DAY_OF_YEAR, -i)
                daysDatabase.daysDao().insert(
                    createRandomDay(calendar.time)
                )
            }
        }
    }

    fun createRandomDay(date: Date) =
        Day(
            maxSteps = (7500..12000).random(),
            stepsByHour = (0..23).map { (0..2).random() * (0..1000).random() },
            date = date
        )
}