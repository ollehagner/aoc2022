package day11

import infiniteInts
import readInput

fun main() {
    val monkeys = parseInput(readInput("day11/input.txt"))
    val totalMonkeyProduct = monkeys.map { it.modDivisor }.reduce(Long::times)
    infiniteInts(1)
        .take(10000)
        .forEach { round ->
            monkeys.forEach { monkey ->
                monkey.inspectAndThrow(monkeys) { value -> value % totalMonkeyProduct }
            }
        }

    val monkeyBusinessValue = monkeys
        .map { it.inspections }
        .sortedByDescending { it }
        .take(2)
        .reduce(Long::times)
    println("Day 11 part 2. Total monkey business: $monkeyBusinessValue")

}




