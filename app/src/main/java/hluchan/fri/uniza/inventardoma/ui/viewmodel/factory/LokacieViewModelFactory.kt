package hluchan.fri.uniza.inventardoma.ui.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import hluchan.fri.uniza.inventardoma.database.dao.LocationDao
import hluchan.fri.uniza.inventardoma.ui.viewmodel.LokacieScreenViewModel

@Suppress("UNCHECKED_CAST")
class LokacieViewModelFactory(
    private val dao: LocationDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LokacieScreenViewModel::class.java)) {
            return LokacieScreenViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}

