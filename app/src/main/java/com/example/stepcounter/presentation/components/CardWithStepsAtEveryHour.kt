package com.example.stepcounter.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.util.Calendar


@Composable
fun CardWithStepsAtEveryHour(stepsAtTheDay: List<Int>) {
    val max = maxOf(stepsAtTheDay.max(), 1)
    var time by remember { mutableStateOf(Calendar.getInstance().time.hours) }
    var current by remember { mutableStateOf("${stepsAtTheDay[time]}") }
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "${time}:00-${time + 1}:00 $current")

        LazyRow {
            itemsIndexed(stepsAtTheDay) { index, it ->
                val color = if (time == index) {
                    MaterialTheme.colorScheme.surfaceVariant
                } else {
                    MaterialTheme.colorScheme.background
                }
                VerticalProgressBar(
                    width = 7,
                    percent = it / max.toFloat(),
                    color = Color(255, 230, 5, 255),
                    h = 70,
                    padding = 7,
                    modifier = Modifier
                        .clickable {
                            current = "$it"
                            time = index
                        }
                        .background(color, CircleShape)
                )
            }
        }
        Row(
            Modifier
                .fillMaxWidth()
                .padding(start = 15.dp, end = 15.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = "0")
            Text(text = "6")
            Text(text = "12")
            Text(text = "18")
            Text(text = "h")
        }
    }
}

