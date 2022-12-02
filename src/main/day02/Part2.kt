package day02

import readInput;
import java.lang.IllegalArgumentException

fun main() {
    val testinput = readInput("day02/testinput.txt")
    val input = readInput("day02/input.txt")


    val movesForResult = buildMap<Pair<Move, Result>, Move>() {
        put(Pair(Move.ROCK, Result.WIN), Move.PAPER)
        put(Pair(Move.ROCK, Result.LOSS), Move.SCISSORS)
        put(Pair(Move.ROCK, Result.DRAW), Move.ROCK)
        put(Pair(Move.PAPER, Result.WIN), Move.SCISSORS)
        put(Pair(Move.PAPER, Result.LOSS), Move.ROCK)
        put(Pair(Move.PAPER, Result.DRAW), Move.PAPER)
        put(Pair(Move.SCISSORS, Result.WIN), Move.ROCK)
        put(Pair(Move.SCISSORS, Result.LOSS), Move.PAPER)
        put(Pair(Move.SCISSORS, Result.DRAW), Move.SCISSORS)
    }

    val totalscore = input
        .map { it.split(" ") }
        .map { Pair(toMove(it.first()), toExpectedResult(it.last())) }
        .map { Round(it.first, movesForResult[it]!!) }
        .map { round -> round.score() }
        .map { it.second }
        .sum()

    println("Day 2 part 2. Total score $totalscore")
}

fun toMove(value: String): Move {
    return when(value) {
        "A" -> Move.ROCK
        "B" -> Move.PAPER
        "C" -> Move.SCISSORS
        else -> { throw IllegalArgumentException("Unknown move $value")
        }
    }
}

fun toExpectedResult(value: String): Result {
    return when(value) {
        "X" -> Result.LOSS
        "Y" -> Result.DRAW
        "Z" -> Result.WIN
        else-> { throw IllegalArgumentException("Unknown result $value")
        }
    }
}