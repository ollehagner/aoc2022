import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class UtilsKtTest {

    @Test
    fun shouldGroupItems() {
        val input = listOf(1,2,3,4,5,6)
        val grouped = input.chunked(3)
        assertEquals(2, grouped.size)
        assertIterableEquals(listOf(1,2,3), grouped.first())
        assertIterableEquals(listOf(4,5,6), grouped.last())
    }
}