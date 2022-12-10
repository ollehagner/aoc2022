package day10

import infiniteInts
import readInput

val checkintervals = listOf(20, 60, 100, 140, 180, 220)

fun main() {
    val clockcycles = infiniteInts(1)
    val register = Register(1)
    val commands = parseInput(readInput("day10/input.txt"))
    val result =  commands.zip(clockcycles.asIterable())
        .fold(0) { acc, (command, cycle) ->

            val accumulatedSignalStrength = if(cycle in checkintervals) {
                acc + register.signalStrength(cycle)
            } else {
                acc
            }
            command.execute(register)
            accumulatedSignalStrength
        }
    println("Day 10 part 1. Total added signalstrength: $result")
}

