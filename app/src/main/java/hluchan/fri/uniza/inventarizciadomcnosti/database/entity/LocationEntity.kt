package hluchan.fri.uniza.inventarizciadomcnosti.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
    id - autogenerovane.
    name - nazov lokacie.
    iconName - string pre imageVector ikony danej lokacie.
    itemCount - pocet poloziek v lokacii.
    description - nejaky popis co si nastavi uzivatel.
 */
@Entity(tableName = "e_location")
data class LocationEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val iconName: String? = null,
    val itemCount: Int = 0,
    val description: String? = null
)