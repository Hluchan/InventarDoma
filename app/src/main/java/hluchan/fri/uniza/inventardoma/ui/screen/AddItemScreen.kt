package hluchan.fri.uniza.inventardoma.ui.screen


import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import coil.compose.AsyncImage
import hluchan.fri.uniza.inventardoma.R
import hluchan.fri.uniza.inventardoma.database.AppDatabase
import hluchan.fri.uniza.inventardoma.database.entity.ItemEntity
import hluchan.fri.uniza.inventardoma.ui.theme.AppTheme
import kotlinx.coroutines.launch

/**
 * Obrazovka na pridanie novej polozky do databazy.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddItemScreen(navController: NavController) {
    val context = LocalContext.current
    val db = AppDatabase.getInstance(context)
    val coroutineScope = rememberCoroutineScope()

    val nameState = rememberSaveable { mutableStateOf("") }
    val nameError = rememberSaveable { mutableStateOf(false) }
    val categoryState = rememberSaveable { mutableStateOf("") }
    val descriptionState = rememberSaveable { mutableStateOf("") }

    // lokacie z db
    val locations by db
        .locationDao().getAllLocations()
        .collectAsState(initial = emptyList())
    val selectedLocation = rememberSaveable { mutableStateOf<Int?>(null) }
    var expanded by rememberSaveable { mutableStateOf(false) }

    val imageUriState = rememberSaveable { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUriState.value = uri
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // hlava sekcie
        Text(
            text = stringResource(id = R.string.add_new_item),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 8.dp)
                .fillMaxWidth(),
            color = MaterialTheme.colorScheme.onBackground
        )
        // Nazov polozky
        TextField(
            value = nameState.value,
            onValueChange = { nameState.value = it },
            label = { Text(stringResource(R.string.name)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp),
            colors = TextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            ),
            singleLine = true,
            isError = nameError.value,
            supportingText = {
                if (nameError.value) {
                    Text(
                        text = stringResource(id = R.string.name_error),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        )

        // Kategoria
        Spacer(Modifier.height(8.dp))
        TextField(
            value = categoryState.value,
            onValueChange = { categoryState.value = it },
            label = { Text(stringResource(R.string.category)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp),
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            ),
        )

        // Popis
        Spacer(Modifier.height(8.dp))
        TextField(
            value = descriptionState.value,
            onValueChange = { descriptionState.value = it },
            label = { Text(stringResource(R.string.description)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp),
            minLines = 3,
            maxLines = 3,
            colors = TextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            ),
        )

        // Lokacia
        Spacer(Modifier.height(8.dp))
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(type = MenuAnchorType.PrimaryEditable, enabled = true)
                    .padding(start = 16.dp, end = 16.dp),
                readOnly = true,
                value = locations.firstOrNull { it.id == selectedLocation.value }?.name ?: "",
                onValueChange = {}, // read only..
                label = { Text(text = "Lokácia") },
                trailingIcon = @Composable {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                singleLine = true,
                colors = ExposedDropdownMenuDefaults.textFieldColors(
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                )
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                containerColor = MaterialTheme.colorScheme.surface,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = MaterialTheme.shapes.small
                    ),
                scrollState = rememberScrollState()
            ) {
                locations.forEach { location ->
                    DropdownMenuItem(
                        text = { Text(location.name) },
                        onClick = {
                            selectedLocation.value = location.id
                            expanded = false
                        }
                    )
                }
            }
        }

        // Vyber fotky
        Spacer(Modifier.height(8.dp))
        Button(
            onClick = { launcher.launch("image/*") },
            shape = MaterialTheme.shapes.small,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                contentColor = MaterialTheme.colorScheme.onPrimary,
                disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp)
        ) {
            Text(
                text = stringResource(R.string.choose_photo),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
        imageUriState.value?.let { uri ->
            Spacer(Modifier.height(8.dp))
            AsyncImage(
                model = uri,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
        }

        // Ulozit polozku
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = {
                if (nameState.value.isBlank()) {
                    nameError.value = true
                    return@Button
                }

                val locId = selectedLocation.value ?: return@Button
                val item = ItemEntity(
                    name = nameState.value.trim(),
                    category = categoryState.value.trim(),
                    description = descriptionState.value.ifBlank {
                        context.getString(R.string.no_desc_added) },
                    locationId = locId,
                    imageUri = imageUriState.value?.toString()
                )
                coroutineScope.launch {
                    db.itemDao().insertItem(item)
                    navController.popBackStack()
                }
            },
            shape = MaterialTheme.shapes.small,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.8f),
                contentColor = MaterialTheme.colorScheme.onSecondary,
                disabledContainerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp)
        ) {
            Text(
                text = stringResource(R.string.add_new_item),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
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