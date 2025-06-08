package hluchan.fri.uniza.inventardoma

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import hluchan.fri.uniza.inventardoma.ui.navigation.AppBottomNavigationBar
import hluchan.fri.uniza.inventardoma.ui.screen.AddItemScreen
import hluchan.fri.uniza.inventardoma.ui.screen.AddLocationScreen
import hluchan.fri.uniza.inventardoma.ui.screen.EditDetailLocationScreen
import hluchan.fri.uniza.inventardoma.ui.screen.InventarScreen
import hluchan.fri.uniza.inventardoma.ui.screen.KalendarScreen
import hluchan.fri.uniza.inventardoma.ui.screen.LokacieScreen
import hluchan.fri.uniza.inventardoma.ui.screen.bottomNavItems
import hluchan.fri.uniza.inventardoma.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme { // tuto pouzivam custom temu
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            AppBottomNavigationBar(
                navController,
                items = bottomNavItems
            )
        }
    )
    { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "inventar",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("lokacie") {
                LokacieScreen(navController)
            }
            composable("inventar") {
                InventarScreen(navController)
            }
            composable("addItem") {
                AddItemScreen(navController)
            }
            composable("addLocation") {
                AddLocationScreen(navController)
            }
            composable("editLocation/{locationId}") { backStackEntry ->
                val locationId = backStackEntry.arguments?.getString("locationId")?.toIntOrNull()
                if (locationId != null) {
                    EditDetailLocationScreen(
                        navController = navController,
                        locationId = locationId
                    )
                }
            }
            composable("kalendar") {
                KalendarScreen()
            }
        }
    }
}

@Preview(showBackground = true, name = "Light Mode")
@Composable
fun MainScreenPreviewLight() {
    AppTheme(darkTheme = false) {
        MainScreen()
    }
}