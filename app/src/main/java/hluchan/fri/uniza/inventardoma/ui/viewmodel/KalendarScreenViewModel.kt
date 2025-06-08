package hluchan.fri.uniza.inventardoma.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hluchan.fri.uniza.inventardoma.database.dao.ItemDao
import hluchan.fri.uniza.inventardoma.database.entity.ItemEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class KalendarScreenViewModel(
    itemDao: ItemDao
) : ViewModel() {
    val borrowedItems: StateFlow<List<ItemEntity>> =
        itemDao.getBorrowedItems().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}