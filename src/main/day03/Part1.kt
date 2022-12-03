package day03

import readInput

fun main() {
    val testinput = readInput("day03/testinput.txt")
    val input = readInput("day03/input.txt")

    val sumOfPriorities = input
        .map { Rucksack(it).itemsInBothCompartments() }
        .map { itemsInBothCompartments ->
            itemsInBothCompartments.sumOf { Rucksack.priority(it) }
        }
        .sum()
    println("Day 3 part 1. Sum of priorities: $sumOfPriorities")
}

