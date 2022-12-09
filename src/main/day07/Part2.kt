package day07

import groupUntil
import readInput

val MINIMUM_FREE_DISK_SPACE = 30000000

fun main() {

    val testinput = readInput("day07/testinput.txt")
    val input = readInput("day07/input.txt")

    val commands = input
        .groupUntil { it.startsWith("$") }

    val filesystem = Filesystem()
    commands.forEach { filesystem.createFromCommandData(it.first(), it.drop(1)) }

    val availableSpace = filesystem.availableDiskspace()
    val spaceToFreeUp = MINIMUM_FREE_DISK_SPACE - availableSpace

    val totalSizeOfDirectoryToDelete = filesystem.allDirectories()
        .filter { it.value() > spaceToFreeUp }
        .minOf { it.value() }
    println("Day 7 part 2. Total size of dir to delete: $totalSizeOfDirectoryToDelete")
}