package com.example.stepcounter.presentation.stepsscreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.stepcounter.navigation.AppScreens
import com.example.stepcounter.presentation.components.CardWithStepsAtEveryHour
import com.example.stepcounter.presentation.components.ProgressBar
import com.example.stepcounter.presentation.components.lazyRowProgress
import com.example.stepcounter.utils.Utils.formatDateDayMonth
import kotlin.math.round


@Composable
fun MainScreen(navController: NavController) {
    val viewModel: StepsViewModel = hiltViewModel()
    val color = Color(255, 230, 5, 255)

    LaunchedEffect(Unit) {
        viewModel.resetState()
        viewModel.updateDayTimer()
    }

    Scaffold(
        topBar = {
            MainTopAppBar(editTarget = {
                navController.navigate(AppScreens.EditTarget.route)
            })
        }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = it.calculateTopPadding())
        ) {
            val day = lazyRowProgress(
                onClick = { viewModel.currentDay.value = it.date },
                days = viewModel.days.value,
                color = color,
                loadMore = {
                    viewModel.loadMoreDays {
                        if (viewModel.currentDay.value == null) {
                            viewModel.currentDay.value = viewModel.days.value.firstOrNull()!!.date
                        }
                    }
                },
                currentDay = viewModel.currentDay
            )
            if (day != null && viewModel.days.value.firstOrNull { day == it.date.formatDateDayMonth() } != null) {
                viewModel.currentDay.value =
                    viewModel.days.value.first { day == it.date.formatDateDayMonth() }.date
            }
            Card(Modifier.padding(10.dp)) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                    verticalArrangement = Arrangement.SpaceAround,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = "${
                                viewModel.days.value.firstOrNull { it.date == viewModel.currentDay.value }
                                    ?.getSteps()
                            } ",
                            style = MaterialTheme.typography.displaySmall,
                            color = Color.White
                        )
                        Text(
                            text = "Шагов",
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        ProgressBar(
                            percent = viewModel.days.value.firstOrNull { it.date == viewModel.currentDay.value }
                                ?.getSteps()
                                ?.div(
                                    viewModel.days.value.firstOrNull { it.date == viewModel.currentDay.value }?.maxSteps?.toFloat()
                                        ?: 0f
                                ) ?: 0f,
                            barWidth = 30,
                            width = 290,
                            color
                        )
                        Text(
                            text = "Цель ${viewModel.days.value.firstOrNull { it.date == viewModel.currentDay.value }?.maxSteps}",
                            modifier = Modifier.padding(5.dp),
                            Color.Gray
                        )
                    }
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                        Text(
                            text = "${
                                round((viewModel.days.value.firstOrNull { it.date == viewModel.currentDay.value }
                                    ?.getSteps() ?: 0) * 0.7 / 1000 * 100) / 100
                            } Км",
                            style = MaterialTheme.typography.headlineMedium
                        )
                        Text(
                            text = "${
                                round(((viewModel.days.value.firstOrNull { it.date == viewModel.currentDay.value }
                                    ?.getSteps() ?: 0) * 0.04 * 100)) / 100
                            } кал",
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }
                }
            }
            if (viewModel.days.value.firstOrNull { it.date == viewModel.currentDay.value }?.stepsByHour != null)
                CardWithStepsAtEveryHour(viewModel.days.value.firstOrNull { it.date == viewModel.currentDay.value }!!.stepsByHour)
        }
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopAppBar(editTarget: () -> Unit) {
    TopAppBar(title = { Text("Шаги") }, actions = {
        Icon(
            Icons.Default.Settings,
            contentDescription = "Изменить цель",
            modifier = Modifier.clickable { editTarget() })
    })
}