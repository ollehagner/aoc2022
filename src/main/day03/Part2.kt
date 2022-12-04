package day03

import readInput
typealias RucksackContents = Set<Char>

fun main() {
    val testinput = readInput("day03/testinput.txt")
    val allRucksackContents = readInput("day03/input.txt")

    val sumOfBadges = allRucksackContents
        .map { it.toSet() as RucksackContents }
        .chunked(3)
        .map { groupRucksackContents ->
            groupRucksackContents
                .reduce() { first, second -> first.intersect(second) }
        }.sumOf { commonItems -> commonItems.sumOf { Rucksack.priority(it) } }

    println("Day 3 part 2. Sum of badge priorities $sumOfBadges")
}
