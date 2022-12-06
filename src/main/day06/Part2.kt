package day06

import readInput

fun main() {
    val messagesize = 14

    val testinput = "mjqjpqmgbljsphdztnvjfqwrcgsmlb"
    val input = readInput("day06/input.txt").first()

    val charsToStartOfMessage = input
        .windowed(messagesize)
        .map { it.toSet() }
        .takeWhile { it.size != messagesize }
        .count() + messagesize

    println("Day 6 part 2, num of chars to start of packet $charsToStartOfMessage")
}