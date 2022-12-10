package day10

import infiniteInts
import readInput

private const val ROW_LENGTH = 40

fun main() {
    val clockcycles = infiniteInts(0)
    val register = Register(1)
    val commands = parseInput(readInput("day10/input.txt"))
    commands.zip(clockcycles.asIterable())
        .forEach { (command, cycle) ->
            val positionInRow = cycle % ROW_LENGTH
            if (positionInRow == 0) println()
            if (positionInRow in spritePositions(register.value())) print("#") else print(".")
            command.execute(register)
        }
}

fun spritePositions(registerValue: Int): List<Int> {
    return listOf(registerValue, registerValue + 1, registerValue - 1)
}