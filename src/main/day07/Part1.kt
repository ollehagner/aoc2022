package day07

import groupUntil
import readInput

fun main() {
    val testinput = readInput("day07/testinput.txt")
    val input = readInput("day07/input.txt")

    val commands = input
        .groupUntil { it.startsWith("$") }

    val filesystem = Filesystem()
    commands.forEach { filesystem.createFromCommandData(it.first(), it.drop(1)) }

    val totalSum = filesystem.allDirectories()
        .filter { it.value() < 100000 }
        .sumOf { it.value() }
    println("Day 7 part 1. Total sum of dirs below 100000 in size: $totalSum")
}

