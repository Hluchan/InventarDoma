package hluchan.fri.uniza.inventarizciadomcnosti.ui.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import hluchan.fri.uniza.inventarizciadomcnosti.database.dao.ItemDao
import hluchan.fri.uniza.inventarizciadomcnosti.ui.viewmodel.InventarScreenViewModel

/**
 * VM Factory pre InventarScreenViewModel
 * Vytvára inštancie InventarScreenViewModel s pripojeným Dao.
 *
 * @param itemDao Dao pre prácu s položkami typu ItemEntity
 */
@Suppress("UNCHECKED_CAST")
class InventarViewModelFactory(
    private val itemDao: ItemDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InventarScreenViewModel::class.java)) {
            return InventarScreenViewModel(itemDao) as T
        }
        throw IllegalArgumentException("Nespravy typ ViewModelu!")
    }
}