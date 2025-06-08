package hluchan.fri.uniza.inventarizciadomcnosti

import hluchan.fri.uniza.inventarizciadomcnosti.database.entity.ItemEntity
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNull
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ItemEntityUnitTest {
    /**
     * Testovanie konstruktoru ItemEntity s prednastavenými hodnotami.
     */
    @Test
    fun itemEntity_defaults() {
        val item = ItemEntity(
            name = "Test",
            category = null,
            description = null,
            locationId = 0
        )

        assertFalse(item.isBorrowed)
        assertNull(item.imageUri)
    }
}