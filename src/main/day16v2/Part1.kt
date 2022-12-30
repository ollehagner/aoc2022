package day16v2

import day16.Valve
import readInput
import java.util.*

fun main() {
    val valves = readInput("day16/input.txt").map { Valve.parse(it) }
    val distances = valves
        .flatMap { valve ->
            valves
                .filter { it != valve && it.flowRate > 0 }
                .map { Pair(valve.id, it.id) to distance(valve.id, it.id, valves) }
        }.toMap()
    val bestpath = solve(State("AA", valves.filter { it.flowRate > 0 }), distances, 30)
    println("Day 16 part 1. Best path pressure release: ${bestpath.releasedPressure(30)}")
}

fun solve(start: State, distances: Map<Pair<String, String>, Int>, timeLimit: Int): State {
    val states =
        PriorityQueue<State>() { a, b -> a.openedValves().size.compareTo(b.openedValves().size) * -1}

    states.add(start)
    var currentMax = start
    while (states.isNotEmpty()) {
        val currentState = states.poll()

        val nextMoves = currentState.valves
            .filter { it.closed() && it.id != currentState.position }
            .map { destinationValve -> destinationValve to distances[currentState.position to destinationValve.id]!! }
            .filter { currentState.elapsedTime + it.second < timeLimit }
            .map { (valve, distance) -> currentState.openValve(valve.id, distance) }

        if(nextMoves.isEmpty()) {
            currentMax = if(currentState.releasedPressure(timeLimit) > currentMax.releasedPressure(timeLimit)) currentState else currentMax
        } else {
            states.addAll(nextMoves)
        }
        states.removeIf { it.allValvesOpen() || it.duplicate(currentState) }
        states.removeIf { it.elapsedTime > timeLimit }

    }
    return currentMax
//    return done.maxBy { it.releasedPressure(timeLimit) }
}


data class State(val position: String, val valves: List<Valve>, val elapsedTime: Int = 0) {

    fun duplicate(other: State): Boolean {
        return this.position == other.position && this.openedValves() == other.openedValves()
    }

    fun openedValves(): List<String> {
        return this.valves.filter { it.opened }.map { it.id }
    }

    fun releasedPressure(timeLimit: Int): Int {
        return valves
            .filter { it.opened }
            .sumOf { it.releasedPressure(timeLimit) }
    }

    fun currentRelease(): Int {
        return valves.filter { it.opened }
            .sumOf { it.flowRate }
    }

    fun allValvesOpen(): Boolean {
        return valves.filter { it.flowRate > 0 }.all { it.opened }
    }

    fun openValve(id: String, distance: Int): State {
        val newElapsedTime = elapsedTime + distance + 1
        val openedValve = valves.first { it.id == id }.open(newElapsedTime)
        val newValves = valves
            .filter { it.id != openedValve.id }
            .fold(mutableListOf(openedValve)) { acc, valve ->
                acc.add(valve)
                acc
            }
        return copy(
            position = id,
            valves = newValves,
            elapsedTime = newElapsedTime
        )
    }
}


fun distance(from: String, to: String, valves: List<Valve>): Int {
    if (from == to) return 0
    val valvesWithDistance = PriorityQueue<Pair<String, Int>>() { a, b -> a.second.compareTo(b.second) }
    valvesWithDistance.add(from to 0)
    val valveMap = valves.associateBy { it.id }
    val visited = mutableSetOf<String>()
    while (true) {
        val toExpand = valvesWithDistance.poll()
        if(toExpand.first == to) return toExpand.second

        valveMap[toExpand.first]!!
            .connections
            .filter { !visited.contains(it) }
            .forEach { valvesWithDistance.add(it to toExpand.second + 1) }
    }

}

fun toValve(id: String, valves: List<Valve>): Valve {
    return valves.first { it.id == id }
}