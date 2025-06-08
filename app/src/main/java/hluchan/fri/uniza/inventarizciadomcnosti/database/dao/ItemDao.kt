package hluchan.fri.uniza.inventarizciadomcnosti.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import hluchan.fri.uniza.inventarizciadomcnosti.database.entity.ItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: ItemEntity)

    @Update
    suspend fun updateItem(item: ItemEntity)

    @Delete
    suspend fun deleteItem(item: ItemEntity)

    @Query("SELECT * FROM e_item ORDER BY name")
    fun getAllItems(): Flow<List<ItemEntity>>

    @Query("SELECT * FROM e_item WHERE locationId = :locationId")
    fun getItemsByLocation(locationId: Int): Flow<List<ItemEntity>>

    @Query("SELECT * FROM e_item WHERE name LIKE '%' || :query || '%'")
    fun searchItems(query: String): Flow<List<ItemEntity>>

    @Query("SELECT * FROM e_item WHERE isBorrowed = 1")
    fun getBorrowedItems(): Flow<List<ItemEntity>>
}