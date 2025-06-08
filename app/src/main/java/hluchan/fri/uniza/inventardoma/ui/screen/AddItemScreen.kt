package hluchan.fri.uniza.inventardoma.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import hluchan.fri.uniza.inventardoma.R
import hluchan.fri.uniza.inventardoma.database.AppDatabase
import hluchan.fri.uniza.inventardoma.ui.theme.AppTheme

/**
 * Obrazovka na pridanie noveho itemu do databazy.
 */
@Composable
fun AddItemScreen(navController: NavController) {
    val context = LocalContext.current
    val db = AppDatabase.getInstance(context)
    val coroutineScope = rememberCoroutineScope()

    val nameState = remember { mutableStateOf("") }
    val categoryState = remember { mutableStateOf("") }
    val descriptionState = remember { mutableStateOf("") }
    val locationState = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // hlava sekcie
        Text(
            text = stringResource(id = R.string.addItem_label),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(start = 16.dp, top = 24.dp, bottom = 8.dp)
                .fillMaxWidth(),
            color = MaterialTheme.colorScheme.onBackground
        )
        // Nazov polozky
        TextField(
            value = nameState.value,
            onValueChange = { nameState.value = it },
            label = { Text("Názov") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp)

            )

        // Kategoria
        Spacer(Modifier.height(8.dp))
        TextField(
            value = categoryState.value,
            onValueChange = { categoryState.value = it },
            label = { Text("Kategória") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp)

        )

        //

    }
}

@Preview(showBackground = true)
@Composable
fun AddItemScreenPreview() {
    val navController = rememberNavController()
    AppTheme(darkTheme = false) {
        AddItemScreen(navController = navController)
    }

}