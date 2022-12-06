package day06

import readInput

fun main() {
    val packetsize = 4

    val testinput = "nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg"
    val input = readInput("day06/input.txt").first()

    val charsToStartOfPacket = input
        .windowed(packetsize)
        .map { it.toSet() }
        .takeWhile { it.size != packetsize }
        .count() + packetsize

    println("Day 6 part 1, num of chars to start of packet $charsToStartOfPacket")
}