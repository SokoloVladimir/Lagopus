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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@Composable
fun InternalCheckBox(
    value: String,
    checked: Boolean,
    enabled: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    animationDelayMs: Int = 350
) {
    val primaryColor = MaterialTheme.colorScheme.onPrimary
    val secondaryColor = MaterialTheme.colorScheme.primary
    val disabledPrimaryColor = MaterialTheme.colorScheme.secondary
    val disabledSecondaryColor = MaterialTheme.colorScheme.secondaryContainer

    val bgColor = remember { Animatable(primaryColor) }
    val fgColor = remember { Animatable(secondaryColor) }
    var disabledBgColor = if (checked) disabledPrimaryColor else disabledSecondaryColor
    var disabledFgColor = if (!checked) disabledPrimaryColor else disabledSecondaryColor

    LaunchedEffect(checked) {
        if (checked) {
            launch {
                bgColor.animateTo(secondaryColor, animationSpec = tween(animationDelayMs))
            }
            launch {
                fgColor.animateTo(primaryColor, animationSpec = tween(animationDelayMs))
            }

            disabledBgColor = disabledPrimaryColor
            disabledFgColor =  disabledPrimaryColor
        } else {
            launch {
                bgColor.animateTo(primaryColor, animationSpec = tween(animationDelayMs))
            }
            launch {
                fgColor.animateTo(secondaryColor, animationSpec = tween(animationDelayMs))
            }

            disabledBgColor = disabledSecondaryColor
            disabledFgColor = disabledSecondaryColor
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
                color = if (enabled) bgColor.value else disabledBgColor,
                shape = MaterialTheme.shapes.small
            )
            .padding(5.dp)

    ) {
        Text(
            text = value,
            fontSize = 20.sp,
            color = if (enabled) fgColor.value else disabledFgColor
        )
    }
}