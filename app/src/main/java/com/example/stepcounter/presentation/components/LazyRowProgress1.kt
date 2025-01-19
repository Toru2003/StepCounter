package com.example.stepcounter.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.stepcounter.domain.models.Day
import com.example.stepcounter.utils.Utils.createDefaultDate
import com.example.stepcounter.utils.Utils.formatDateDayMonth
import kotlinx.coroutines.launch
import java.util.Date
import kotlin.math.max


@Composable
fun lazyRowProgress(
    color: Color,
    onClick: (Day) -> Unit,
    days: List<Day>,
    loadMore: () -> Unit,
    currentDay: MutableState<Date>
): String {

    val current: MutableState<String> =
        remember { mutableStateOf(createDefaultDate().formatDateDayMonth()) }
    LaunchedEffect(currentDay.value) {
        if (currentDay.value != null)
            current.value = currentDay.value.formatDateDayMonth()
    }

    val scope = rememberCoroutineScope()
    val scrollState = rememberLazyListState()
    val firstIndex = scrollState.firstVisibleItemIndex
    val firstOffset = scrollState.firstVisibleItemScrollOffset
    LaunchedEffect(firstIndex) {
        if (firstOffset != 0) {
            scope.launch {
                scrollState.animateScrollToItem(firstIndex + 1)
            }
            current.value = days[firstIndex + 1].date.formatDateDayMonth()

        } else {
            scope.launch {
                scrollState.animateScrollToItem(firstIndex)
            }
            current.value =
                if (days.size > 0) days[firstIndex].date.formatDateDayMonth() else createDefaultDate().toString()

        }
    }

    val visibleDays = scrollState.layoutInfo.visibleItemsInfo.map { it.key }
    val target =
        (days.filter { day -> visibleDays.contains(day.date) }.map { it.maxSteps }).maxOrNull()?.toDouble()
            ?: 2500.0
    val daysMaxValue =
        (days.filter { day -> visibleDays.contains(day.date) }.map { it.getSteps() }).maxOrNull()
    val columnSize = max((daysMaxValue?.toDouble() ?: target) / target, 1.0)

    Box {
        BoxWithProgress(target.toInt(), columnSize.toFloat())
        LazyRow(
            state = scrollState,
            modifier = Modifier.padding(end = 45.dp),
            reverseLayout = true
        ) {
            items(days, key = { it.date }) { date ->
                val color = if (date.getSteps() < date.maxSteps) {
                    Color.Gray
                } else {
                    color
                }

                Column(
                    Modifier
                        .padding(7.dp)
                        .height(230.dp)
                        .width(42.sp.value.dp)
                        .background(
                            if (date.date.formatDateDayMonth() == current.value) {
                                Color(red = 124, green = 124, blue = 124, alpha = 100)
                            } else (MaterialTheme.colorScheme.background.copy(alpha = 0f)),
                            CircleShape
                        )
                        .clickable {
                            current.value = date.date.formatDateDayMonth()
                            onClick.invoke(date)
                        },
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    VerticalProgressBar(
                        columnSize.toFloat(),
                        percent =  (date.getSteps().toFloat() / target.toFloat()),
                        h = 150,
                        color = color,
                        width = 9,
                    )
                    Box(Modifier.height(20.dp), contentAlignment = Alignment.Center) {
                        if (date.date.formatDateDayMonth() == current.value) {
                            Text(
                                text = date.date.formatDateDayMonth().substring(0, 5),
                                color = MaterialTheme.colorScheme.background,
                                modifier = Modifier.background(color, CircleShape),
                                fontSize = 14.sp,
                                maxLines = 1,
                                fontWeight = FontWeight.Bold
                            )
                        } else {
                            Text(
                                text = date.date.formatDateDayMonth().substring(0, 2),
                                color =MaterialTheme.colorScheme.onBackground,
                                fontSize = 19.sp
                            )
                        }
                    }
                }
            }
            item {
                LaunchedEffect(Unit) {
                    loadMore()
                }
            }
        }
    }
    return current.value
}
