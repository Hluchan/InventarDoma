package hluchan.fri.uniza.inventardoma.ui.screen

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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
import hluchan.fri.uniza.inventardoma.R
import hluchan.fri.uniza.inventardoma.database.AppDatabase
import hluchan.fri.uniza.inventardoma.database.entity.LocationEntity
import hluchan.fri.uniza.inventardoma.ui.theme.AppTheme
import hluchan.fri.uniza.inventardoma.ui.viewmodel.LokacieScreenViewModel
import hluchan.fri.uniza.inventardoma.ui.viewmodel.factory.LokacieViewModelFactory

/**
 * TODO - napisat popis
 */
@Composable
fun LokacieScreen(
    navController: NavController,
) {
    val context = LocalContext.current
    val db = AppDatabase.getInstance(context)
    val factory = LokacieViewModelFactory(db.locationDao())
    val viewModel: LokacieScreenViewModel = viewModel(factory = factory)

    val locations by viewModel.lokacie.collectAsState(initial = emptyList())

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Hlavicka
        Text(
            text = stringResource(id = R.string.lokacieScreen_label),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(start = 16.dp, top = 24.dp, bottom = 8.dp, end = 16.dp)
                .fillMaxWidth(),
            color = MaterialTheme.colorScheme.onBackground
        )

        // Zoznam lokacií
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                AddLocationCard {
                    navController.navigate("addLocation")
                }
            }

            items(locations) { location ->
                LocationCard(
                    location = location,
                    onClick = { /* TODO navigácia */ }
                )
            }

        }
    }
}

/**
 * diferenciacia od FAB-u vzdy prva polozka funguje na navigaciu
 * do obrazovky ako pridavanie lokacie do DB.
 */
@Composable
fun AddLocationCard(onClick: () -> Unit) {
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
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(R.string.add_new_location),
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        shape = CircleShape
                    )
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = stringResource(R.string.add_new_location),
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

/**
 * Karticka reprezentujuca lokaciu - miesto fotky jednoducha ikonka
 * aby sa dalo rychlo identifikovat na ktorej obrazovke sa nachadzam.
 * kliknutie otvrori edit screen na lokaciu.
 */
@Composable
fun LocationCard(
    location: LocationEntity,
    onClick: () -> Unit
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                painter = getIconPainter(location.iconName),
                contentDescription = "Ikona lokácie",
                modifier = Modifier
                    .size(64.dp) //TODO mozno dat 48??
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(8.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(location.name,
                    style = MaterialTheme.typography.titleMedium
                )
                location.description?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis // ... ak je text nad limit
                    )
                }
            }
        }
    }
}

/**
 * Funkcia na prepinanie ikony podla stringu
 */
@Composable
fun getIconPainter(iconName: String?): Painter {
    return when (iconName) {
        "Home" -> painterResource(id = R.drawable.home_icon)
        "Living Room" -> painterResource(id = R.drawable.living_room_icon)
        "Work" -> painterResource(id = R.drawable.work_icon)
        "School" -> painterResource(id = R.drawable.school_icon)
        "Warehouse" -> painterResource(id = R.drawable.warehouse_icon)
        "Garage" -> painterResource(id = R.drawable.garage_icon)
        "Kitchen" -> painterResource(id = R.drawable.kitchen_icon)
        "Bathroom" -> painterResource(id = R.drawable.bathroom_icon)
        "Toilet" -> painterResource(id = R.drawable.toilet_icon)
        "Bedroom" -> painterResource(id = R.drawable.bedroom_icon)
        "Office" -> painterResource(id = R.drawable.office_icon)
        "Closet" -> painterResource(id = R.drawable.closet_icon)
        "Library" -> painterResource(id = R.drawable.library_icon)
        "Utility" -> painterResource(id = R.drawable.utility_icon)
        else -> painterResource(id = R.drawable.location_placeholder_icon)
    }
}

@Preview(showBackground = true)
@Composable
fun LokacieScreenPreview() {
    AppTheme(darkTheme = false) {
        val navController = rememberNavController()
        LokacieScreen(navController = navController)
    }
}