package hluchan.fri.uniza.inventarizciadomcnosti

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
import hluchan.fri.uniza.inventarizciadomcnosti.ui.navigation.AppBottomNavigationBar
import hluchan.fri.uniza.inventarizciadomcnosti.ui.screen.AddItemScreen
import hluchan.fri.uniza.inventarizciadomcnosti.ui.screen.AppScreen
import hluchan.fri.uniza.inventarizciadomcnosti.ui.screen.InventarScreen
import hluchan.fri.uniza.inventarizciadomcnosti.ui.screen.KalendarScreen
import hluchan.fri.uniza.inventarizciadomcnosti.ui.screen.LokacieScreen
import hluchan.fri.uniza.inventarizciadomcnosti.ui.screen.bottomNavItems
import hluchan.fri.uniza.inventarizciadomcnosti.ui.theme.AppTheme

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
            composable(AppScreen.LokacieScreen.route) {
                LokacieScreen(navController)
            }
            composable(AppScreen.InventarScreen.route) {
                InventarScreen(navController)
            }
            composable("addItem") {
                AddItemScreen(navController)
            }
            composable(AppScreen.KalendarScreen.route) {
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