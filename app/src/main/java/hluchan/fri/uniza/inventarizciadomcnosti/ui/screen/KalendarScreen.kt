package hluchan.fri.uniza.inventarizciadomcnosti.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import hluchan.fri.uniza.inventarizciadomcnosti.ui.viewmodel.KalendarScreenViewModel

@Composable
fun KalendarScreen() {
    val viewModel: KalendarScreenViewModel = viewModel()
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Obrazovka 3: Kalendar", fontSize = 24.sp)
    }
}