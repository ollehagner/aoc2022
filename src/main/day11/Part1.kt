package day11

import infiniteInts
import readInput
import java.lang.IllegalArgumentException
import java.util.*
import kotlin.math.floor

fun main() {
    val monkeys = parseInput(readInput("day11/testinput.txt"))
    infiniteInts(1)
        .take(20)
        .forEach { round ->
            monkeys.forEach { monkey ->
                monkey.inspectAndThrow(monkeys) { value -> value / 3 }
            }
        }

    val monkeyBusinessValue = monkeys
        .map { it.inspections }
        .sortedByDescending { it }
        .take(2)
        .reduce(Long::times)
    println("Day 11 part 1. Total monkey business: $monkeyBusinessValue")
}


fun worryFunction(instructions: String): (Long) -> Long {
    val operand = instructions.split(" ").last().trim()
    return when {
        instructions.contains("old * old") -> { value -> value * value }
        instructions.contains("+") -> { value -> operand.toLong() + value }
        instructions.contains("*") -> { value -> value * operand.toLong() }
        else -> throw IllegalArgumentException("Unknown operator in $instructions")
    }
}

fun monkeyRouter(divisor: Long, routeIfTrue: Int, routeIfFalse: Int): (Long) -> Int {
    return { value -> if (value % divisor == 0L) routeIfTrue else routeIfFalse }
}

fun parseInput(input: List<String>): List<Monkey> {
    return input
        .map { it.trim() }
        .windowed(6,7)
        .map { monkeyData ->
            Monkey(monkeyData)
        }
}

class Monkey(input: List<String>) {
    val items: Queue<Long>
    val worryIncrease: (Long) -> Long
    val monkeyRouter: (Long) -> Int
    val modDivisor = input[3].substringAfterLast(" ").toLong()
    var inspections: Long = 0
    init {
        items = LinkedList(input[1].substringAfter(":")
            .split(",")
            .map { it.trim().toLong() })

        worryIncrease = worryFunction(input[2])
        monkeyRouter = monkeyRouter(
            modDivisor,
            input[4].substringAfterLast(" ").toInt(),
            input[5].substringAfterLast(" ").toInt()
        )
    }

    fun inspectAndThrow(monkeys: List<Monkey>, worryLevelDecreaseFunction: (Long) -> Long) {
        this.items.map { item ->
            val worryLevel = worryIncrease.invoke(item)
            inspections++
            val loweredWorryItem = floor((worryLevelDecreaseFunction.invoke(worryLevel)).toDouble()).toLong()
            monkeys[monkeyRouter.invoke(loweredWorryItem)].throwItem(loweredWorryItem)
        }
        this.items.clear()
    }

    fun throwItem(item: Long) {
        items.add(item)
    }

    override fun toString(): String {
        return "Monkey (items=$items). Inspections: $inspections"
    }


}