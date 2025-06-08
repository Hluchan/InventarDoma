package hluchan.fri.uniza.inventarizciadomcnosti.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hluchan.fri.uniza.inventarizciadomcnosti.database.dao.ItemDao
import hluchan.fri.uniza.inventarizciadomcnosti.database.entity.ItemEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class InventarScreenViewModel(
    itemDao: ItemDao
) : ViewModel() {
    val items: StateFlow<List<ItemEntity>> =
        itemDao
            .getAllItems()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )
}