package common

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test
import kotlin.math.sqrt

class VectorTest {

    @Test
    fun testMagnitude() {
        val vector = Vector(Point(1,1), Point(4, 5))
        assertThat(vector.magnitude(), equalTo(5.0))
    }

    @Test
    fun testMagnitudeWithNegativeDirection() {
        val vector = Vector(Point(4,5), Point(10, 13))
        assertThat(vector.magnitude(), equalTo(10.0))
    }

    @Test
    fun testAddPositive() {
        val first = Vector(Point(0, 0), Point(3,3))
        val second = Vector(Point(0, 0), Point(-2, 2))
        val added = first.add(second)
        assertThat(added.horizontal(), equalTo(1))
        assertThat(added.vertical(), equalTo(5))
        assertThat(added.magnitude(), equalTo(sqrt(26.0)))
    }

}

