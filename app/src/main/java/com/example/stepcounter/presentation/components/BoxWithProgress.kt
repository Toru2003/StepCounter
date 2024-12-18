package com.example.stepcounter.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun BoxWithProgress(target: Int, columnSize: Float) {
    val textColor = MaterialTheme.colorScheme.onBackground.hashCode()
    Box(modifier = Modifier.height(210.dp), contentAlignment = Alignment.BottomStart) {
        Canvas(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
                .height(150.dp)
        ) {

            drawLine(
                color = Color.Green,
                start = Offset(0f, size.height * (1 - 1 / columnSize)),
                end = Offset(size.width, size.height * (1 - 1 / columnSize)),
                strokeWidth = 1.dp.toPx(),
            )
            val s = size.height * (1 - 1 / (2 * columnSize))
            drawLine(
                color = Color.Yellow,
                start = Offset(0f, s),
                end = Offset(size.width, s),
                strokeWidth = 1.dp.toPx(),
            )
            val paint = Paint().asFrameworkPaint().apply {
                isAntiAlias = true
                textSize = 13.sp.toPx()
                color = textColor
            }

            drawContext.canvas.nativeCanvas.drawText(
                "$target",
                size.width - 100, size.height * (1 - 1 / columnSize) + 33f,
                paint
            )
            drawContext.canvas.nativeCanvas.drawText(
                "${(target / 2)}",
                size.width - 100, s + 33f,
                paint
            )
        }
    }
}