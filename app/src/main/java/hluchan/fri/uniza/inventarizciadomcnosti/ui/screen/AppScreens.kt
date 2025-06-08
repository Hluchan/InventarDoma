package hluchan.fri.uniza.inventarizciadomcnosti.ui.screen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddLocation
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * nieco ako enum pre kazdy screen ktory je v nav bare
 */
sealed class AppScreen(
    val route: String,
    val label: String,
    val icon: ImageVector
) {
    object LokacieScreen : AppScreen("lokacie", "Lokácie", Icons.Default.AddLocation)
    object InventarScreen : AppScreen("inventar", "Inventár", Icons.Default.Inventory)
    object KalendarScreen : AppScreen("kalendar", "Kalendár", Icons.Default.CalendarMonth)
}

val bottomNavItems = listOf(
    AppScreen.LokacieScreen,
    AppScreen.InventarScreen,
    AppScreen.KalendarScreen
)

