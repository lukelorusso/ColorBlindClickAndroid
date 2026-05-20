package com.lukelorusso.colorblindclick.presentation.ui.theme

import android.content.Context
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import com.lukelorusso.colorblindclick.presentation.R

@Composable
fun darkColors(context: Context) = darkColorScheme(
    primary = Color(ContextCompat.getColor(context, R.color.color_primary_dark)),
    secondary = Color(ContextCompat.getColor(context, R.color.color_accent))
)

@Composable
fun lightColors(context: Context) = lightColorScheme(
    primary = Color(ContextCompat.getColor(context, R.color.color_primary)),
    secondary = Color(ContextCompat.getColor(context, R.color.color_accent))
)
