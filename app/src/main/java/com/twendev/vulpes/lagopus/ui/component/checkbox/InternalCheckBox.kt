package com.twendev.vulpes.lagopus.ui.component.checkbox

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@Composable
fun InternalCheckBox(
    value: String,
    checked: Boolean,
    enabled: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    val primaryColor = MaterialTheme.colorScheme.primary
    val secondaryColor = MaterialTheme.colorScheme.surface

    val bgColor = remember { Animatable(primaryColor) }
    val fgColor = remember { Animatable(secondaryColor) }

    LaunchedEffect(checked) {
        if (checked) {
            this.launch {
                bgColor.animateTo(secondaryColor, animationSpec = tween(500))
            }
            fgColor.animateTo(primaryColor, animationSpec = tween(500))
        } else {
            this.launch {
                bgColor.animateTo(primaryColor, animationSpec = tween(500))
            }
            fgColor.animateTo(secondaryColor, animationSpec = tween(500))
        }
    }

    Box(
        Modifier
            .clickable(
                enabled = enabled
            ) {
                onCheckedChange(!checked)
            }
            .border(1.dp, MaterialTheme.colorScheme.primary, MaterialTheme.shapes.small)
            .background(
                color = if (enabled) bgColor.value else Color.LightGray,
                shape = MaterialTheme.shapes.small
            )
            .padding(5.dp)

    ) {
        Text(
            text = value,
            fontSize = 20.sp,
            color = fgColor.value
        )
    }
}