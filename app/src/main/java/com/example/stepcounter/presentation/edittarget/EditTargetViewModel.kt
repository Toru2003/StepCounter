package com.example.stepcounter.presentation.edittarget

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stepcounter.data.local.room.steps.DaysDatabase
import com.example.stepcounter.domain.models.Day
import com.example.stepcounter.utils.Utils.createDefaultDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class EditTargetViewModel @Inject constructor(
    val database: DaysDatabase
) : ViewModel() {
    val currentDay: MutableState<Day?> = mutableStateOf(getDay())
    fun getDay() = runBlocking {
        database.daysDao().getDay(createDefaultDate())
    }

    fun updateDay() {
        viewModelScope.launch {
            database.daysDao().update(currentDay.value!!)
        }
    }
}