package com.example.stepcounter.presentation.edittarget

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch


@Composable
fun EditTargetScreen(
    navigateUp: () -> Unit
) {
    val viewModel: EditTargetViewModel = hiltViewModel()

    Scaffold {
        val pitch = 100
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxHeight()
                .padding(it.calculateTopPadding())
        ) {
            CardWithLazyColumnForSelect(
                num = viewModel.currentDay.value?.maxSteps ?: 0 / pitch,
                listSize = 500,
                text = "Цель шагов",
                pitch = pitch
            ) {
                if (it != null) {
                    viewModel.currentDay.value!!.maxSteps = it
                    viewModel.updateDay()
                }
                navigateUp()
            }
        }
    }
}

//карточка для выбора элемента
@Composable
fun CardWithLazyColumnForSelect(
    num: Int,  //начальный элемент
    listSize: Int,  //размер списка для выбора
    text: String,//текст оописание
    pitch: Int,///шаг между элеметами
    initialIndex: Int = 0,  //значение первого элемента если 0 то 0 и его выбрать нельзя если -1 то можно выбрать 0
    onDismiss: (Int?) -> Unit
) {
    var ret by remember { mutableIntStateOf(num) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row {
            ScrolableLazyColumn(
                (num - 1) / pitch,
                listSize,
                pitch,
                initialIndex = initialIndex
            ) {
                ret = it
            }
            Column(
                modifier = Modifier.height(190.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = text, style = MaterialTheme.typography.headlineMedium)
            }
        }
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Отмена",
                modifier = Modifier
                    .clickable { onDismiss(null) },
                style = MaterialTheme.typography.displaySmall
            )
            Text(
                text = "Сохранить",
                modifier = Modifier
                    .clickable { onDismiss(ret) },
                style = MaterialTheme.typography.displaySmall
            )
        }
    }
}


@Composable
fun ScrolableLazyColumn(
    num: Int,  //стартовое значение
    listSize: Int,//размер списка
    pitch: Int,//шаг значений для часов 1 2 3 для минут 5 10 15
    initialIndex: Int = 0, // если надо выбирать значение ноль что надо поставить -1
    onDismiss: (Int) -> Unit
) {

    val scrollState =
        rememberLazyListState(initialFirstVisibleItemIndex = maxOf(num - 1 - initialIndex, 0))
    val scope = rememberCoroutineScope()
    val itemHeight = 50.dp
    val firstIndex = scrollState.firstVisibleItemIndex
    val firstOffset = scrollState.firstVisibleItemScrollOffset


    LazyColumn(
        state = scrollState, modifier = Modifier
            .height(190.dp)
            .padding(20.dp)
    ) {
        items((initialIndex..listSize).toList(),
            key = { it }) { index ->
            ScrollCard(
                n = index * pitch,
                h = itemHeight,
                isMiddle = index == firstIndex + 1 + initialIndex
            )
        }
    }
    LaunchedEffect(firstIndex) {
        if (firstOffset != 0) {
            scope.launch {
                scrollState.animateScrollToItem(firstIndex + 1)
            }
        } else {
            scope.launch {
                scrollState.animateScrollToItem(firstIndex)
            }
        }
        onDismiss((firstIndex + 1 + initialIndex) * pitch)
    }
}


//карточка которая используется при выборе скролом
@Composable
fun ScrollCard(n: Int, h: Dp, isMiddle: Boolean = false) {
    if (n >= 0) {
        Box(
            modifier = Modifier
                .height(h)
                .width(100.dp), contentAlignment = Alignment.Center
        ) {
            if (isMiddle) {
                Text(
                    text = "$n",
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            } else {
                Text(
                    text = "$n",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.Gray
                )
            }
        }
    } else {
        Box(
            modifier = Modifier
                .height(h)
                .width(100.dp)
        ) {}
    }
}