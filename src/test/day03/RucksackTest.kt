package day03

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.lang.IllegalArgumentException

class RucksackTest {

    @Test
    fun shouldSplitInMiddle() {
        val rucksack = Rucksack("aaaabbbb")
        assertEquals("aaaa", rucksack.firstCompartmentContents)
        assertEquals("bbbb", rucksack.secondCompartmentContents)
    }

    @Test
    fun shouldSplitInMiddle2() {
        val rucksack = Rucksack("abcabcedfedf")
        assertEquals("abcabc", rucksack.firstCompartmentContents)
        assertEquals("edfedf", rucksack.secondCompartmentContents)
    }

    @Test
    fun shouldNotAllowUnevenContentLength() {
        Assertions.assertThrows(IllegalArgumentException::class.java) {
            Rucksack("abc")
        }
    }

    @Test
    fun shouldFindAllItemsThatExistInBothCompartments() {
        val rucksack = Rucksack("abca")
        val existsInBoth = rucksack.itemsInBothCompartments()
        assertIterableEquals(setOf('a'), existsInBoth)
    }

    @Test
    fun shouldFindAllItemsThatExistInBothCompartments2() {
        val rucksack = Rucksack("abcdefca")
        val existsInBoth = rucksack.itemsInBothCompartments()
        assertIterableEquals(setOf('a', 'c'), existsInBoth)
    }

    @Test
    fun shouldFindAllItemsThatExistInBothCompartments3() {
        val rucksack = Rucksack("abcdABCD")
        val existsInBoth = rucksack.itemsInBothCompartments()
        assertTrue(existsInBoth.isEmpty())
    }

    @Test
    fun shouldHaveCorrectPriorities() {
        assertEquals(1, Rucksack.priority('a'))
        assertEquals(26, Rucksack.priority('z'))
        assertEquals(27, Rucksack.priority('A'))
        assertEquals(52, Rucksack.priority('Z'))
    }
}
