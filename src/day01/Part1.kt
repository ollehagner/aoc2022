package day01

import common.Direction
import common.Point

fun main() {

    val start = Point(0, 0)
    val next = start.move(Direction.DOWN).move(Direction.LEFT)
    println(next)
}