package hluchan.fri.uniza.inventarizciadomcnosti.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 *    id je autogenerovane.
 *    category je string ktory si uzivatel nastavi
 *    isBorrowed je boolean ci je predmet pozicany.
 *    borrowedTo je nazov osoby ktorej sme vypozicali.
 *    returnDate je datum vratenia.
 *    a este je tu foreign key ktory odkazuje na lokaciu
 *    kde sa nachadza predmet.
 *
 *    !!pocet tu nie je preto lebo o to sa moja aplikacia nestara -
 *    ide o to kde sa veci nachadzaju alebo komu sa vypozicaju!!
 */
@Entity(
    tableName = "e_item",
    foreignKeys = [
        ForeignKey(
            entity = LocationEntity::class,
            parentColumns = ["id"],
            childColumns = ["locationId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("locationId")]
)
data class ItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val category: String?,
    val description: String?,
    val imageUri: String? = null,
    val isBorrowed: Boolean = false,
    val borrowedTo: String? = null,
    val returnDate: String? = null,
    val locationId: Int // referuje na LocationEntity
)
