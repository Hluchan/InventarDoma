package hluchan.fri.uniza.inventarizciadomcnosti.ui.navigation


import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import hluchan.fri.uniza.inventarizciadomcnosti.ui.screen.AppScreen


@Composable
fun AppBottomNavigationBar(
    navController: NavController,
    items: List<AppScreen>,
    modifier: Modifier = Modifier,
) {
    NavigationBar(
        modifier = modifier,
    ) {
        val backStackEntry = navController.currentBackStackEntryAsState()
        val currentRoute = backStackEntry.value?.destination?.route

        items.forEach {
            screen ->
            NavigationBarItem(
                icon = {Icon(imageVector = screen.icon, contentDescription = screen.label)},
                label = { Text(text = screen.label) },
                selected = currentRoute == screen.route,
                onClick = {
                    if (currentRoute != screen.route) { // nenavigujem sam na seba
                        navController.navigate(screen.route) {
                            // skoc na zaciatocny screen aby sa nerobil zbytocne dlhy back stack
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            // nechcem viac kopii rovnakeho screenu v back stacku
                            // a vratit stack do rovnakeho stavu ked sa vratim na item
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                alwaysShowLabel = false,
                colors = androidx . compose . material3 . NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    indicatorColor = MaterialTheme.colorScheme.surfaceContainerHighest
                )
            )
        }
    }
}
