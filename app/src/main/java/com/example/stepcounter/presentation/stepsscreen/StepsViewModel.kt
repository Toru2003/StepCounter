package com.example.stepcounter.presentation.stepsscreen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stepcounter.data.local.room.steps.DaysDatabase
import com.example.stepcounter.domain.models.Day
import com.example.stepcounter.utils.StepsManager
import com.example.stepcounter.utils.Utils.createDefaultDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.Timer
import javax.inject.Inject
import kotlin.concurrent.timerTask

@HiltViewModel
class StepsViewModel @Inject constructor(
    private val database: DaysDatabase,
    private val stepsManager: StepsManager,
) : ViewModel() {
    private var currentPage = 0
    private val pageSize = 10
    var days: MutableState<List<Day>> = mutableStateOf(listOf())
    var daysCount: Int? = null

    val timer = Timer()

    fun getCurrentDay() =
        days.value.firstOrNull { it.date == createDefaultDate() }

    fun loadMoreDays(onComplete: () -> Unit) {
        if (daysCount ?: Int.MAX_VALUE > pageSize * currentPage)
            viewModelScope.launch {
                daysCount = database.daysDao().getCount()
                days.value = (days.value + (database.daysDao()
                    .getItems(pageSize * currentPage, pageSize * currentPage + pageSize)
                    ?: listOf())).distinct()
                currentPage += 1
                onComplete()
            }
    }

    fun resetState() {
        days.value = listOf()
        currentPage = 0
    }

    fun updateDayTimer () {
        timer.schedule(
            timerTask { updateDay() },
            5000L,5000L
        )
    }
    val currentDay =  mutableStateOf(createDefaultDate())


    private fun updateDay() {
        viewModelScope.launch {
            val day =  runBlocking {database.daysDao().getDay(createDefaultDate())}
            val newDays = days.value.toMutableList()
            newDays[newDays.indexOfFirst { it.date== createDefaultDate() }] = day!!
            days.value = newDays
        }
//        var lastHourSteps = runBlocking {  stepsManager.readLastHourSteps().first()}?: 0
//        viewModelScope.launch {
//            var steps = stepsCounter.getSteps()
//            if (lastHourSteps != steps) {
//                if (lastHourSteps == 0) {
//                    stepsManager.saveLastHourSteps(steps)
//                    lastHourSteps = 0
//                    steps = 0
//                }
//                val day = getCurrentDay()
//                val s = day!!.stepsByHour.toMutableList()
//                if (lastHourSteps < steps) {
//                    s[java.util.Calendar.getInstance().time.hours] =
//                        s[java.util.Calendar.getInstance().time.hours] + steps - lastHourSteps
//                    day.stepsByHour = s
//                    database.daysDao().update(day)
//                }
//                stepsManager.saveLastHourSteps(steps)
//                val updatedDays = days.value.toMutableList()
//                updatedDays[updatedDays.indexOfFirst { it.date == createDefaultDate() }].stepsByHour = s
//                days.value = updatedDays
//                currentDay.value = createDefaultDate()
//            }
//        }
    }
}