package hluchan.fri.uniza.inventardoma.ui.screen

import android.annotation.SuppressLint
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
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
import coil.compose.AsyncImage
import hluchan.fri.uniza.inventardoma.R
import hluchan.fri.uniza.inventardoma.database.AppDatabase
import hluchan.fri.uniza.inventardoma.database.entity.ItemEntity
import hluchan.fri.uniza.inventardoma.ui.theme.AppTheme
import kotlinx.coroutines.launch
import android.app.DatePickerDialog
import androidx.compose.foundation.clickable
import java.util.Calendar
import java.text.SimpleDateFormat
import java.util.Locale

@SuppressLint("UseKtx")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditDetailItemScreen(
    itemId: Int,
    navController: NavController
) {
    val context = LocalContext.current
    val db = AppDatabase.getInstance(context)
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val items by db.itemDao().getAllItems().collectAsState(initial = emptyList())
    val currentItem = items.find { it.id == itemId }

    val locations by db.locationDao().getAllLocations().collectAsState(initial = emptyList())

    val selectedLocation = rememberSaveable { mutableStateOf<Int?>(null) }

    val nameState = rememberSaveable { mutableStateOf(currentItem?.name ?: "") }
    val categoryState = rememberSaveable { mutableStateOf(currentItem?.category ?: "") }
    val descriptionState = rememberSaveable { mutableStateOf(currentItem?.description ?: "") }

    val isBorrowedState = rememberSaveable { mutableStateOf(currentItem?.isBorrowed == true) }
    val borrowedToState = rememberSaveable { mutableStateOf(currentItem?.borrowedTo ?: "") }

    val calendar = rememberSaveable { Calendar.getInstance() }
    val dateFormat = rememberSaveable { SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()) }
    val returnDateState = rememberSaveable { mutableStateOf(currentItem?.returnDate ?: "") }

    // parsovanie predosleho datumu
    LaunchedEffect(Unit) {
        try {
            if (returnDateState.value.isNotBlank()) {
                dateFormat.parse(returnDateState.value)?.let {
                    calendar.time = it
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    var expanded by rememberSaveable { mutableStateOf(false) }
    val imageUriState = rememberSaveable {
        mutableStateOf<Uri?>(currentItem?.imageUri?.let { Uri.parse(it) })
    }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUriState.value = uri
    }

    // aby ostali z itemu hodnoty vo fieldoch
    var initialized by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(currentItem) {
        if (currentItem != null && !initialized) {
            nameState.value = currentItem.name
            categoryState.value = currentItem.category ?: ""
            descriptionState.value = currentItem.description ?: ""
            selectedLocation.value = currentItem.locationId
            isBorrowedState.value = currentItem.isBorrowed
            borrowedToState.value = currentItem.borrowedTo ?: ""
            returnDateState.value = currentItem.returnDate ?: ""
            imageUriState.value = currentItem.imageUri?.let { Uri.parse(it) }

            initialized = true
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // hlavicka
            Text(
                text = stringResource(R.string.edit_item),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 24.dp, bottom = 8.dp, end = 16.dp),
                color = MaterialTheme.colorScheme.onBackground
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

                // kategoria
                item {
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
                }

                //popis
                item {
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
                }

                //vyber lokacie
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
                            value = locations.firstOrNull { it.id == selectedLocation.value }?.name ?: "",
                            onValueChange = {},
                            label = { Text(text = stringResource(id = R.string.location)) },
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
                }

                // check ci je pozicane
                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        Checkbox(
                            checked = isBorrowedState.value,
                            onCheckedChange = { isBorrowedState.value = it }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(R.string.is_borrowed),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                // ak je pozicane tak komu
                if (isBorrowedState.value) {
                    item {
                        TextField(
                            value = borrowedToState.value,
                            onValueChange = { borrowedToState.value = it },
                            label = { Text(stringResource(R.string.borrowed_to_name)) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            singleLine = true,
                            colors = TextFieldDefaults.colors(
                                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                focusedContainerColor = MaterialTheme.colorScheme.surface,
                                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                            )
                        )
                    }

                    //datum navratu
                    item {
                        TextField(
                            value = returnDateState.value,
                            onValueChange = {},
                            label = { Text(stringResource(R.string.return_date_label)) },
                            readOnly = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .clickable {
                                    DatePickerDialog(
                                        context,
                                        { _, year, month, dayOfMonth ->
                                            calendar.set(year, month, dayOfMonth)
                                            returnDateState.value = dateFormat.format(calendar.time)
                                        },
                                        calendar.get(Calendar.YEAR),
                                        calendar.get(Calendar.MONTH),
                                        calendar.get(Calendar.DAY_OF_MONTH)
                                    ).show()
                                },
                            colors = TextFieldDefaults.colors(
                                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                focusedContainerColor = MaterialTheme.colorScheme.surface,
                                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                            )
                        )
                    }
                }

                // Vyber obrázku
                item {
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
                }

                // ak je obrazok tak ukaz
                if (imageUriState.value != null) {
                    item {
                        AsyncImage(
                            model = imageUriState.value,
                            contentDescription = "Náhľad obrázku",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        )
                    }
                }

                // ulozenie
                item {
                    Button(
                        onClick = {
                            val locId = selectedLocation.value ?: currentItem?.locationId ?: return@Button
                            val item = ItemEntity(
                                id = itemId,
                                name = nameState.value.trim().ifBlank { currentItem?.name ?: "" },
                                category = categoryState.value.trim(),
                                description = descriptionState.value.ifBlank {
                                    context.getString(R.string.no_desc_added)
                                },
                                locationId = locId,
                                imageUri = imageUriState.value?.toString(),
                                isBorrowed = isBorrowedState.value,
                                borrowedTo = if (isBorrowedState.value) borrowedToState.value else null,
                                returnDate = if (isBorrowedState.value) returnDateState.value else null
                            )
                            coroutineScope.launch {
                                db.itemDao().insertItem(item)
                                navController.previousBackStackEntry?.savedStateHandle?.set(
                                    "snackbar_message",
                                    context.getString(R.string.item_edited)
                                )
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
                            text = stringResource(R.string.save),
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
                                currentItem?.let { item ->
                                    db.itemDao().deleteItem(item)
                                    navController.previousBackStackEntry?.savedStateHandle?.set(
                                        "snackbar_message",
                                        context.getString(R.string.item_deleted)
                                    )
                                    navController.popBackStack()
                                }
                            }
                        },
                        shape = MaterialTheme.shapes.small,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error.copy(alpha = 0.8f)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.delete),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun EditDetailItemScreenPreview() {
    AppTheme(darkTheme = true) {
        val navController = rememberNavController()
        val testItemId = 1

        EditDetailItemScreen(
            itemId = testItemId,
            navController = navController
        )
    }
}