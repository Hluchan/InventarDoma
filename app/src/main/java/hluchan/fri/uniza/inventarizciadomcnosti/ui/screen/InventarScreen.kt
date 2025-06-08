package hluchan.fri.uniza.inventarizciadomcnosti.ui.screen

import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import hluchan.fri.uniza.inventarizciadomcnosti.R
import hluchan.fri.uniza.inventarizciadomcnosti.database.AppDatabase
import hluchan.fri.uniza.inventarizciadomcnosti.database.entity.ItemEntity
import hluchan.fri.uniza.inventarizciadomcnosti.ui.theme.AppTheme
import hluchan.fri.uniza.inventarizciadomcnosti.ui.viewmodel.InventarScreenViewModel
import hluchan.fri.uniza.inventarizciadomcnosti.ui.viewmodel.factory.InventarViewModelFactory

/**
 * obrazovka co nam ukazuje polozky ktore su v DB - konkretne itemy alebo nejake veci
 * ine ako lokacie pretoze lokacie mozu obsahovat tieto polozky.
 *
 * zobrazi hlavu sekcie, upraveny searchbar a zoznam poloziek
 * Fetchuje to cez kombinaciu VM Factory -> VM -> DAO -> polozka v databaze
 *
 * @param navController dovoluje prejst do obrazovky na pridanie polozky do DB a na upravu polozky v DB
 */
@Composable
fun InventarScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val db = AppDatabase.getInstance(context)
    val factory = InventarViewModelFactory(db.itemDao())

    val viewModel: InventarScreenViewModel = viewModel(factory = factory)

    var searchQuery by remember { mutableStateOf("") }
    // bezpecne nacitanie z viewmodelu
    val items by viewModel.items.collectAsState(initial = emptyList())

    val filteredItems = items.filter {
        it.name.contains(searchQuery, ignoreCase = true)
    }

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        Text(
            text = stringResource(id = R.string.inventarScreen_label),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(start = 16.dp, top = 24.dp, bottom = 8.dp, end = 16.dp)
                .fillMaxWidth()
        )

        SearchBarComposable(
            modifier = Modifier.padding(horizontal = 16.dp),
            onSearch = { searchQuery = it }
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            //zobrazit pridavaciu kartu VZDY ako prvu
            item {
                AddItemCard(
                    onClick = { navController.navigate("addItem") }
                )
            }

            items(filteredItems) { actualItem ->
                ItemCard(
                    item = actualItem,
                    onClick = { navController.navigate("edit_item/${actualItem.id}") })
            }

        }
    }
}

/**
 * Zjednoduseny search bar pretoze nechcem overlay cez celu obrazkovku -
 * vzdy ked vyhladavam podla mena.
 */
@Composable
fun SearchBarComposable(
    modifier: Modifier,
    onSearch: (String) -> Unit
) {
    var searchText by remember { mutableStateOf("") }

    TextField(
        value = searchText,
        onValueChange = {
            searchText = it
            onSearch(it)
        },
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp)),
        placeholder = { Text("Zadaj názov položky") },
        leadingIcon = {
            Icon(Icons.Default.Menu, contentDescription = "Menu")
        },
        trailingIcon = {
            Icon(Icons.Default.Search, contentDescription = "Search")
        },
        singleLine = true
    )
}

/**
 * Zobrazi basic info o polozke.
 * taktiez zobrazuje obrazok ak polozka obsahuje URI na neho.
 */
@Composable
fun ItemCard(
    item: ItemEntity,
    onClick: () -> Unit
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // cely tento if riesi to ze ked nie je obrazok tak tam da placeholder vo farbe temy
            if (item.imageUri != null) {
                AsyncImage(
                    model = item.imageUri,
                    contentDescription = "Obrázok položky",
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Image,
                    contentDescription = "Placeholder",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(12.dp)
                )
            }

            Spacer(
                modifier = Modifier.width(16.dp)
            )

            Column {
                Text(item.name, style = MaterialTheme.typography.titleMedium)
                item.category?.let {
                    Text(it, style = MaterialTheme.typography.bodySmall)
                }
                item.description?.let {
                    Text(
                        it,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis // ... na konci miesta
                    )
                }
            }
        }
    }
}

/**
 * Alternativne riesenie ako cez FAB - chcel som sa odlisit od normy Material dizajnu aspon trosku.
 * Jednoduche tlacitko ktore otvori screen na pridanie polozky.
 */
@Composable
fun AddItemCard(onClick: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(20.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Pridať položku",
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        shape = CircleShape
                    )
                    .padding(8.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = "Pridať novú položku",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun InventarScreenPreview() {
    AppTheme {
        val navController = rememberNavController()
        InventarScreen(navController = navController)
    }
}
