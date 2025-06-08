package hluchan.fri.uniza.inventardoma.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
import hluchan.fri.uniza.inventardoma.database.entity.ItemEntity
import hluchan.fri.uniza.inventardoma.database.entity.LocationEntity
import hluchan.fri.uniza.inventardoma.ui.theme.AppTheme
import kotlinx.coroutines.launch

/**
 * editovanie detailov lokacie
 * snackbar zobrazi ci sa ukladanie editu alebo zmazanie podarilo
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditDetailLocationScreen(
    navController: NavController,
    locationId: Int
) {
    val context = LocalContext.current
    val db = AppDatabase.getInstance(context)
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val locations by db.locationDao().getAllLocations().collectAsState(initial = emptyList())
    val currentLocation = locations.find { it.id == locationId }

    val items by db.itemDao().getAllItems().collectAsState(initial = emptyList())
    val itemsInLocation: List<ItemEntity> = items.filter { it.locationId == locationId }

    val nameState = remember { mutableStateOf(currentLocation?.name ?: "") }
    val descState = remember { mutableStateOf(currentLocation?.description ?: "") }
    var expanded by remember { mutableStateOf(false) }
    var selectedIcon by remember { mutableStateOf(currentLocation?.iconName ?: "") }

    val iconOption = listOf(
        "Home", "Living Room", "Work", "School", "Warehouse", "Garage",
        "Kitchen", "Bathroom", "Toilet", "Bedroom", "Office", "Closet",
        "Library", "Utility"
    )

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.onBackground)
        ) {
            // hlavicka
            Text(
                text = "Upraviť lokáciu",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 24.dp, bottom = 8.dp, end = 16.dp)
            )

            // stlpec s formularmi
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                // nazov
                item {
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
                        singleLine = true
                    )
                }

                // popis
                item {
                    TextField(
                        value = descState.value,
                        onValueChange = { descState.value = it },
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
                }

                // ikona dropdown
                item {
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
                            value = selectedIcon,
                            onValueChange = {}, // read only..
                            label = { Text(text = stringResource(id = R.string.icon)) },
                            trailingIcon = @Composable {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                            },
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
                            iconOption.forEach { selectedOption ->
                                DropdownMenuItem(
                                    text = {
                                        Row (
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                painter = getIconPainter(selectedOption),
                                                contentDescription = selectedOption,
                                                tint = MaterialTheme.colorScheme.primary,
                                                modifier = Modifier
                                                    .size(24.dp)
                                                    .padding(end = 4.dp)
                                            )

                                            Text(
                                                text = selectedOption,
                                                style = MaterialTheme.typography.bodyLarge,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                        }
                                    },
                                    onClick = {
                                        selectedIcon = selectedOption
                                        expanded = false
                                    },
                                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                                )
                            }
                        }
                    }
                }

                // ulozenie
                item {
                    Button(
                        onClick = {
                            // ulozenie do DB ale ak sa nezmenilo tak ostane to iste
                            val location = LocationEntity(
                                id = locationId,
                                name = nameState.value.trim().ifBlank { currentLocation?.name ?: "" },
                                description = if (descState.value.isBlank()) currentLocation?.description.orEmpty()
                                else descState.value.trim(),
                                iconName = if (selectedIcon.isBlank()) currentLocation?.iconName.orEmpty()
                                else selectedIcon
                            )
                            coroutineScope.launch {
                                db.locationDao().insertLocation(location)

                                // paralelne spustenie kvoli suspend funkcii
                                launch {
                                    snackbarHostState.showSnackbar(context.getString(R.string.location_edited))
                                }

                                // ulozenie snackbaru do navControlleru pre predoslu obrazovku
                                navController.previousBackStackEntry
                                    ?.savedStateHandle
                                    ?.set(
                                        "snackbar_message",
                                        context.getString(R.string.location_edited)
                                    )
                                navController.popBackStack()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp),
                        shape = MaterialTheme.shapes.small,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.8f),
                            contentColor = MaterialTheme.colorScheme.onSecondary,
                            disabledContainerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
                        )
                    ) {
                        Text(
                            text = stringResource(id = R.string.save),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // zmazanie
                item {
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                currentLocation?.let { location ->
                                    db.locationDao().deleteLocation(location)

                                    // paralelne spustenie kvoli suspend funkcii snackbaru
                                    launch {
                                        snackbarHostState.showSnackbar(context.getString(R.string.location_deleted))
                                    }

                                    navController.previousBackStackEntry
                                        ?.savedStateHandle
                                        ?.set(
                                            "snackbar_message",
                                            context.getString(R.string.location_deleted)
                                        )
                                    navController.popBackStack()
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp),
                        shape = MaterialTheme.shapes.small,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error.copy(alpha = 0.8f)
                        )
                    ) {
                        Text(
                            text = stringResource(id = R.string.delete),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // zoznam poloziek na danej lokacii
                items(itemsInLocation) { item ->
                    Column(
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        Text(
                            text = "• ${item.name} (${item.category ?: "bez kategórie"})",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EditDeatilLocationScreenPreview() {
    AppTheme {
        val navController = rememberNavController()
        EditDetailLocationScreen(navController = navController, locationId = 1)
    }
}

