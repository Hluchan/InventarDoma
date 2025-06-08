package hluchan.fri.uniza.inventardoma.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hluchan.fri.uniza.inventardoma.database.dao.LocationDao
import hluchan.fri.uniza.inventardoma.database.entity.LocationEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class LokacieScreenViewModel(
    dao: LocationDao
) : ViewModel() {
    val lokacie: StateFlow<List<LocationEntity>> = dao
        .getAllLocations()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}