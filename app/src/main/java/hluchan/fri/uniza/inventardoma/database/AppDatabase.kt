package hluchan.fri.uniza.inventardoma.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import hluchan.fri.uniza.inventardoma.database.dao.ItemDao
import hluchan.fri.uniza.inventardoma.database.dao.LocationDao
import hluchan.fri.uniza.inventardoma.database.entity.ItemEntity
import hluchan.fri.uniza.inventardoma.database.entity.LocationEntity

@Database(
    entities = [
        LocationEntity::class,
        ItemEntity::class
    ],
    version = 4,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun locationDao(): LocationDao
    abstract fun itemDao(): ItemDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null
        // volatile je ze vsetky vlakna uvidia tuto INSTANCE hodnotu
        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "inventory_db"
                )
                    .fallbackToDestructiveMigration(true)
                    .build().also {
                    INSTANCE = it
                }
            }
        }
    }
}
