package com.lukelorusso.presentation.ui.theme

import android.content.Context
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import com.lukelorusso.presentation.R

@Composable
fun darkColors(context: Context) = darkColors(
    primary = Color(ContextCompat.getColor(context, R.color.color_primary_dark)),
    primaryVariant = Color(ContextCompat.getColor(context, R.color.color_primary_dark)),
    secondary = Color(ContextCompat.getColor(context, R.color.color_accent)),
    secondaryVariant = Color(ContextCompat.getColor(context, R.color.color_accent))
)

@Composable
fun lightColors(context: Context) = lightColors(
    primary = Color(ContextCompat.getColor(context, R.color.color_primary)),
    primaryVariant = Color(ContextCompat.getColor(context, R.color.color_primary)),
    secondary = Color(ContextCompat.getColor(context, R.color.color_accent)),
    secondaryVariant = Color(ContextCompat.getColor(context, R.color.color_accent))
)
