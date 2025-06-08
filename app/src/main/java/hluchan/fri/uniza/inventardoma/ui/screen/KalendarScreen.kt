package hluchan.fri.uniza.inventardoma.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import hluchan.fri.uniza.inventardoma.R
import hluchan.fri.uniza.inventardoma.database.AppDatabase
import hluchan.fri.uniza.inventardoma.database.entity.ItemEntity
import hluchan.fri.uniza.inventardoma.ui.theme.AppTheme
import hluchan.fri.uniza.inventardoma.ui.viewmodel.KalendarScreenViewModel
import hluchan.fri.uniza.inventardoma.ui.viewmodel.factory.KalendarViewModelFactory

@Composable
fun KalendarScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val db = AppDatabase.getInstance(context)
    val factory = KalendarViewModelFactory(db.itemDao())
    val viewModel: KalendarScreenViewModel = viewModel(factory = factory)

    val items = viewModel.borrowedItems.collectAsState(initial = emptyList()).value

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // hlavicka
        Text(
            text = stringResource(id = R.string.screen3_label),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 24.dp, bottom = 8.dp, end = 16.dp)
        )

        // zoznam itemov alebo info text
        if (items.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = stringResource(R.string.kalendar_no_items))
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(items) { item ->
                    BorrowedItemCard(
                        item = item,
                        onClick = {
                            navController.navigate("editItem/${item.id}")
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun BorrowedItemCard(
    item: ItemEntity,
    onClick: () -> Unit = {}
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                item.name,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            item.borrowedTo?.let {
                Text(
                    stringResource(R.string.borrowed_to, it),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            item.returnDate?.let {
                Text(
                    stringResource(R.string.return_date_label, it),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Preview
@Composable
fun BorrowedItemCardPreview() {
    AppTheme {
        BorrowedItemCard(
            item = ItemEntity(
                id = 1,
                name = "Item 1",
                category = "Category 1",
                description = "Description 1",
                locationId = 1,
                borrowedTo = "John Doe",
                isBorrowed = true,
                returnDate = "2023-12-31"
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun KalendarScreenPreview() {
    AppTheme(darkTheme = true) {
        var navController = rememberNavController()
        KalendarScreen(navController)
    }
}