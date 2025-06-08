package hluchan.fri.uniza.inventardoma.ui.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import hluchan.fri.uniza.inventardoma.database.dao.ItemDao
import hluchan.fri.uniza.inventardoma.ui.viewmodel.KalendarScreenViewModel

@Suppress("UNCHECKED_CAST")
class KalendarViewModelFactory(
    private val itemDao: ItemDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(KalendarScreenViewModel::class.java)) {
            return KalendarScreenViewModel(itemDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}