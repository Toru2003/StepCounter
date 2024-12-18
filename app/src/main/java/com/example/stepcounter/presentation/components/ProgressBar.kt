package com.example.stepcounter.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp


@Composable
fun ProgressBar(
    percent: Float,
    barWidth: Int,
    width: Int,
    color: Color,
) {
    val percent = minOf(1f, percent)
    Box {
        Canvas(
            modifier = Modifier
                .padding(10.dp)
                .width(width.dp)
        ) {
            drawLine(
                color = Color.Black,
                start = Offset(0f, 0f),
                end = Offset(size.width, 0f),
                strokeWidth = barWidth.dp.toPx(),
                cap = StrokeCap.Round
            )
            drawLine(
                color = color,
                start = Offset(0f, 0f),
                end = Offset(size.width * percent, 0f),
                strokeWidth = (barWidth + 5).dp.toPx(),
                cap = StrokeCap.Round,
            )
        }
    }
}