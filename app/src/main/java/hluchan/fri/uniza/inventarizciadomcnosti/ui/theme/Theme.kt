// hluchan.fri.uniza.inventarizciadomcnosti.ui.theme.Theme.kt
package hluchan.fri.uniza.inventarizciadomcnosti.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

/**
 * Farby prebrate z catpuccin palety na githube - https://github.com/catppuccin/palette
 * tento subor je vytvoreny za pomoci AI.
 */
// --- Catppuccin Latte for Light Theme ---
private val CatppuccinLatteLightColorScheme = lightColorScheme(
    primary = CatppuccinLatteSky,
    onPrimary = CatppuccinLatteBase, // Text on primary buttons (often a very light/dark color)
    primaryContainer = CatppuccinLatteSurface0, // Or a lighter shade of Mauve
    onPrimaryContainer = CatppuccinLatteText,

    secondary = CatppuccinLatteGreen,
    onSecondary = CatppuccinLatteBase,
    secondaryContainer = CatppuccinLatteSurface0,
    onSecondaryContainer = CatppuccinLatteText,

    tertiary = CatppuccinLattePeach,
    onTertiary = CatppuccinLatteBase,
    tertiaryContainer = CatppuccinLatteSurface0,
    onTertiaryContainer = CatppuccinLatteText,

    error = CatppuccinLatteRed,
    onError = CatppuccinLatteCrust, // Or a very light color like CatppuccinLatteCrust
    errorContainer = CatppuccinLatteFlamingo, // A lighter, less intense error bg
    onErrorContainer = CatppuccinLatteText, // Text on the error container

    background = CatppuccinLatteBase,
    onBackground = CatppuccinLatteText,

    surface = CatppuccinLatteMantle, // Cards, sheets, menus
    onSurface = CatppuccinLatteText,
    surfaceVariant = CatppuccinLatteSurface1, // For elements needing slight differentiation
    onSurfaceVariant = CatppuccinLatteSubtext0,

    outline = CatppuccinLatteOverlay0, // Borders, dividers
    outlineVariant = CatppuccinLatteOverlay2,

    scrim = CatppuccinLatteCrust.copy(alpha = 0.4f), // For overlays like behind dialogs
    surfaceTint = CatppuccinLatteMauve, // Often same as primary for subtle tinting

    inversePrimary = CatppuccinFrappeSky, // Primary color from the opposite theme
    inverseSurface = CatppuccinFrappeBase, // Surface from the opposite theme
    inverseOnSurface = CatppuccinFrappeText // onSurface from the opposite theme
)

// --- Catppuccin Frappé for Dark Theme ---
private val CatppuccinFrappeDarkColorScheme = darkColorScheme(
    primary = CatppuccinFrappeSky,
    onPrimary = CatppuccinFrappeBase, // Text on primary buttons
    primaryContainer = CatppuccinFrappeSurface0, // Or a darker shade of Mauve
    onPrimaryContainer = CatppuccinFrappeText,

    secondary = CatppuccinFrappeGreen,
    onSecondary = CatppuccinFrappeBase,
    secondaryContainer = CatppuccinFrappeSurface0,
    onSecondaryContainer = CatppuccinFrappeText,

    tertiary = CatppuccinFrappePeach,
    onTertiary = CatppuccinFrappeBase,
    tertiaryContainer = CatppuccinFrappeSurface0,
    onTertiaryContainer = CatppuccinFrappeText,

    error = CatppuccinFrappeRed,
    onError = CatppuccinFrappeBase,
    errorContainer = CatppuccinFrappeMaroon, // A less intense error bg
    onErrorContainer = CatppuccinFrappeText,

    background = CatppuccinFrappeBase,
    onBackground = CatppuccinFrappeText,

    surface = CatppuccinFrappeMantle, // Cards, sheets, menus
    onSurface = CatppuccinFrappeText,
    surfaceVariant = CatppuccinFrappeSurface1,
    onSurfaceVariant = CatppuccinFrappeSubtext0,

    outline = CatppuccinFrappeOverlay0,
    outlineVariant = CatppuccinFrappeOverlay2,

    scrim = CatppuccinFrappeCrust.copy(alpha = 0.6f),
    surfaceTint = CatppuccinFrappeMauve,

    inversePrimary = CatppuccinLatteSky, // Primary from the opposite (Latte) theme
    inverseSurface = CatppuccinLatteBase,
    inverseOnSurface = CatppuccinLatteText
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Set to false to always use Catppuccin
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> CatppuccinFrappeDarkColorScheme
        else -> CatppuccinLatteLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = typography,
        shapes = Shapes,
        content = content
    )
}