package com.tuempresa.osornomarket.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = OsoHoney,
    secondary = OsoTan,
    tertiary = OsoCream,
    surface = OsoDarkBrown,
    onSurface = OsoCream,
    background = OsoDarkBrown,
    onBackground = OsoCream
)

private val LightColorScheme = lightColorScheme(
    primary = OsoMediumBrown,
    secondary = OsoHoney,
    tertiary = OsoDarkBrown,
    surface = OsoCream,
    onSurface = OsoDarkBrown,
    background = OsoCream,
    onBackground = OsoDarkBrown
)

@Composable
fun OsornoMarketTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Desactivamos dynamicColor por defecto para mantener nuestra identidad de marca
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}