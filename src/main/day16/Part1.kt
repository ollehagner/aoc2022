package day16

import addPermutation
import readInput
import java.lang.IllegalArgumentException
import java.util.*

fun main() {
    val valves = readInput("day16/input.txt").map { Valve.parse(it) }
    val distances = valves
        .flatMap { valve ->
            valves
                .filter { it != valve && it.flowRate > 0 }
                .map { Pair(valve.id, it.id) to distance(valve.id, it.id, valves) }
        }.toMap()
    val openers = listOf(ValveOpener("You", "AA"))
    val bestpath = solve(State(openers, valves.filter { it.flowRate > 0 }), distances, 30)
    println("Day 16 part 1. Best path pressure release: ${bestpath.releasedPressure(30)}")
}

fun calculateDistances(valves: List<Valve>) = valves
    .flatMap { valve ->
        valves
            .filter { it != valve && it.flowRate > 0 }
            .map { Pair(valve.id, it.id) to distance(valve.id, it.id, valves) }
    }.toMap()

fun solve(start: State, distances: Map<Pair<String, String>, Int>, timeLimit: Int): State {

    val states =
        PriorityQueue<State>() { a, b -> a.openedValves().size.compareTo(b.openedValves().size) * -1 }
    states.add(start)
    var currentMax = start
    while (states.isNotEmpty()) {
        val currentState = states.poll()

        val nextMoves = currentState.openers
            .map { opener ->
                distances
                    .filter { (fromAndTo, _) -> fromAndTo.first == opener.position  }
                    .filter { (fromAndTo, _) -> currentState.valve(fromAndTo.second).closed() }
                    .map { (fromAndTo, distance) -> OpenValveAction(fromAndTo.first, fromAndTo.second, distance) }
                    .filter { action -> opener.elapsedTime + action.time <= timeLimit }
            }
            .fold(listOf(emptyList<OpenValveAction>())) { acc, actions -> acc.addPermutation(actions) }

        if (nextMoves.isEmpty()) {
            currentMax =
                if (currentState.releasedPressure(timeLimit) > currentMax.releasedPressure(timeLimit)) currentState else currentMax
        } else {
            states.addAll(nextMoves.map { currentState.openValves(it) })
        }
      states.removeIf { it.duplicate(currentState) }

    }
    return currentMax
}

data class ValveOpener(val name: String, val position: String, val elapsedTime: Int = 0) {

    fun openValve(valveId: String, time: Int): ValveOpener {
        return copy(position = valveId, elapsedTime = elapsedTime + time)
    }
}

data class State(val openers: List<ValveOpener>, val valves: List<Valve>) {

    fun positions(): List<String> {
        return openers.map { it.position }
    }

    fun duplicate(other: State): Boolean {
        return this.openedValves() == other.openedValves() && openers.map { it.position }.containsAll(other.openers.map { it.position })
    }

    fun openedValves(): List<String> {
        return this.valves.filter { it.opened }.map { it.id }
    }

    fun openerAtValve(id: String): ValveOpener {
        return openers.firstOrNull { it.position == id } ?: throw IllegalArgumentException("No opener at valve $id")
    }

    fun valve(id: String): Valve {
        return valves.firstOrNull { it.id == id } ?: throw IllegalArgumentException("No valve at valve $id")
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

    fun openValves(actions: List<OpenValveAction>): State {
        return actions.fold(this) { acc, action ->
            acc.openValve(action)
        }
    }

    private fun openValve(action: OpenValveAction): State {
        val opener = openerAtValve(action.from)
        val newOpener = opener.openValve(action.to, action.time + 1)
        val openedValve = valves.first { it.id == action.to }.open(newOpener.elapsedTime)
        val newValves = valves
            .filter { it.id != openedValve.id }
            .fold(mutableListOf(openedValve)) { acc, valve ->
                acc.add(valve)
                acc
            }
        return copy(
            openers = openers.filter { it != opener } + newOpener,
            valves = newValves
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
        if (toExpand.first == to) return toExpand.second

        valveMap[toExpand.first]!!
            .connections
            .filter { !visited.contains(it) }
            .forEach { valvesWithDistance.add(it to toExpand.second + 1) }
    }

}

data class OpenValveAction(val from: String, val to: String, val time: Int)