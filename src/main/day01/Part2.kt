package day01

import readInput;
import java.util.*

fun main() {

    val testInput = readInput("day01/testinput.txt")
    val input = readInput("day01/input.txt")

    val topThreeTotalCalories = input
        .fold(LinkedList<MutableList<Int>>()) { acc, value ->
            if(value.isEmpty()) {
                acc.push(LinkedList())
            } else {
                val current = if(acc.isEmpty()) LinkedList<Int>() else acc.pop()
                current.add(value.toInt())
                acc.push(current)
            }
            acc
        }
        .map { it.sum() }
        .sortedDescending()
        .take(3)
        .sum()

    println("Day 01 part 2 total top three calories: $topThreeTotalCalories")
}