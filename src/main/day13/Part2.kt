package day13

import dequeOf
import readInput

private const val DIVIDER_1 = "[[2]]"

private const val DIVIDER_2 = "[[6]]"

fun main() {
    val packets = readInput("day13/input.txt")
        .filter { !it.isBlank() }
    val packetsWithDividers = mutableListOf(DIVIDER_1, DIVIDER_2).apply { addAll(packets) }
    packetsWithDividers
        .sortWith { a, b -> packetSort(a, b) }

    val decoderKey = packetsWithDividers
        .mapIndexed { index, packet -> Pair(index, packet) }
        .filter { listOf(DIVIDER_1, DIVIDER_2).contains(it.second) }
        .map { it.first + 1}
        .reduce(Int::times)

    println("Day 13 part 2 decoder key is $decoderKey")

}

fun packetSort(a: String, b: String): Int {
    return if(validateOrder(dequeOf(a), dequeOf(b))) -1 else 1
}