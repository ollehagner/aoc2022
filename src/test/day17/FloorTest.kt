package day17

import common.Point
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class FloorTest {
    private val floor = Floor(IntRange(0,6).map { Point(it, 0) })

    @Test
    fun shouldTouch() {
        assertTrue(floor.touches(Rock(listOf(Point(2,2), Point(2,1), Point(3, 2), Point(4,3)))))
    }

    @Test
    fun shouldNotTouch() {
        assertFalse(floor.touches(Rock(listOf(Point(2,2), Point(2,3), Point(3, 2), Point(4,3)))))
    }

    @Test
    fun shouldAddRockToFloor() {
        val rock = Rock(listOf(Point(2,2), Point(2,1), Point(3, 1), Point(4,0)))
        assertEquals(floor.addRock(rock), Floor(listOf(
            Point(0, 0),
            Point(1, 0),
            Point(2, 2),
            Point(3, 1),
            Point(4, 0),
            Point(5, 0),
            Point(6, 0),
        )))
    }

}