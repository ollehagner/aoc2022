package day13

import dequeOf
import readInput
import java.util.*

fun main() {
    val packets = parseInput(readInput("day13/input.txt"))
    val indiceSum = packets
        .mapIndexed { index, packet -> Pair(index + 1, validateOrder(dequeOf(packet.first), dequeOf(packet.second))) }
        .filter { it.second }
        .sumOf { it.first }

    println("Day 13 part 1. Sum of indices: $indiceSum")

}

fun validateOrder(left: Deque<String>, right: Deque<String>): Boolean {
//    left.zip(right)
//        .forEach { println(it.first.padEnd(20) + it.second) }
    val topLeft = left.pop().orEmpty()
    val topRight = right.pop().orEmpty()
    if(topLeft.isEmpty() && topRight.isEmpty()) {
        return validateOrder(LinkedList(left), LinkedList(right))
    }

    if(topLeft.isEmpty()) {
        return true
    } else if(topRight.isEmpty()) {
        return false
    }

    if(topLeft.isList() && topRight.isList()) {
        if(topLeft.isEmpty() && topRight.isNotEmpty()) {
            return true
        } else if(topLeft.isNotEmpty() && topRight.isEmpty()) {
            return false
        }
        val leftNextAndRemainder = nextValueAndRemainder(topLeft)
        val rightNextAndRemainder = nextValueAndRemainder(topRight)
        left.apply {
            push(leftNextAndRemainder.second)
            push(leftNextAndRemainder.first)
        }
        right.apply {
            push(rightNextAndRemainder.second)
            push(rightNextAndRemainder.first)
        }
        return validateOrder(LinkedList(left), LinkedList(right))
    }

    if(!topLeft.isList() && !topRight.isList()) {
        if(topLeft.toInt() == topRight.toInt()) {
            return validateOrder(LinkedList(left), LinkedList(right))
        } else {
            return topLeft.toInt() < topRight.toInt()
        }
    }

    if(!topLeft.isList() && topRight.isList()) {
        left.push(topLeft.toList())
        right.push(topRight)
        return validateOrder(LinkedList(left), LinkedList(right))
    } else if(topLeft.isList() && !topRight.isList()) {
        left.push(topLeft)
        right.push(topRight.toList())
        return validateOrder(LinkedList(left), LinkedList(right))
    }
    return true
}


fun parseInput(input: List<String>): List<Pair<String, String>> {
    return input.windowed(2, 3)
        .map { Pair(it.first(), it.last()) }
}

fun String.isList(): Boolean {
    return startsWith("[") && endsWith("]")
}

fun String.toList(): String {
    return "[$this]"
}

fun String.isEmpty(): Boolean {
    return equals("[]")
}

fun nextValueAndRemainder(value: String): Pair<String, String> {
    return value.drop(1).dropLast(1)
        .fold(Pair("", "")) { acc, value ->
            if(acc.second.isNotEmpty()) {
                Pair(acc.first, acc.second + value)
            } else if(acc.first.isNotEmpty() && (acc.first.last() == ']' || value == ',') && isBalanced(acc.first)) {
                Pair(acc.first, acc.second + value)
            } else {
                Pair(acc.first + value, acc.second)
            }
        }.let { (first, second) -> Pair(first, second.drop(1).toList()) }
}

fun isBalanced(value: String): Boolean {
    return value.count { it == '[' } == value.count { it == ']' }
}
