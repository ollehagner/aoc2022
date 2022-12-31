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

    @Test
    fun shouldPermutate() {
        val first = listOf("a", "b")
        val second = listOf(1, 2, 3)
        val permutations = first.permutations(second)
        assertIterableEquals(listOf(listOf("a",1, "a", 2), listOf("a", 3), listOf("b", 1), listOf("b", 2), listOf("b", 3)), permutations)
    }

    @Test
    fun shouldAddToPermutation() {
        val first = listOf(listOf("a"), listOf("b"))
        val second = listOf(1, 2, 3)
        val permutations = first.addPermutation(second)
        assertIterableEquals(listOf("a" to 1, "a" to 2, "a" to 3, "b" to 1, "b" to 2, "b" to 3), permutations)
    }
}