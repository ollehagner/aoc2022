package day02

import readInput
import java.lang.IllegalArgumentException

fun main() {
    val testinput = readInput("day02/testinput.txt")
    val input = readInput("day02/input.txt")
    val totalscore = input
        .map { it.split(" ") }
        .map { Round(fromSymbol(it.first()), fromSymbol(it.last())) }
        .map { round -> round.score() }
        .map { it.second }
        .sum()
    println("Day 2 part 1. Total score $totalscore")
}

fun fromSymbol(symbol: String): Move {
    return when(symbol) {
        "A" -> Move.ROCK
        "X" -> Move.ROCK
        "B" -> Move.PAPER
        "Y" -> Move.PAPER
        "C" -> Move.SCISSORS
        "Z" -> Move.SCISSORS
        else -> { throw IllegalArgumentException("Unknown symbol $symbol")}
    }
}

