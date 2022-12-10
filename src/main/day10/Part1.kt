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

class Register(private var value: Int) {

    fun add(other: Int) {
        value += other
    }

    fun signalStrength(cycle: Int): Int {
        return value * cycle
    }

    fun value(): Int {
        return value
    }
}

interface Command {
    fun execute(register: Register)
}

class Noop : Command {
    override fun execute(register: Register) {

    }
}

class Add(val toAdd: Int) : Command {
    override fun execute(register: Register) {
        register.add(toAdd)
    }
}

fun parseInput(input: List<String>): List<Command> {
    return input
        .flatMap { row ->
            when(row.substringBefore(" ")) {
                "addx" -> listOf(Noop(), Add(row.substringAfter(" ").toInt()))
                else -> listOf(Noop())
            }
        }
}