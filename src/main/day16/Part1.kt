package day16

import addPermutation
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
    val openers = listOf(ValveOpener("You", "AA"))
    val bestpath = solve(State(openers, valves.filter { it.flowRate > 0 }), distances, 30)
    println("Day 16 part 1. Best path pressure release: ${bestpath.releasedPressure(30)}")
}

fun solve(start: State, distances: Map<Pair<String, String>, Int>, timeLimit: Int): State {

    val states =
        PriorityQueue<State>() { a, b -> a.score().compareTo(b.score()) * -1 }
    states.add(start)
    var currentMax = start
    while (states.isNotEmpty()) {
        val currentState = states.poll()
        val nextMoves = currentState.openers
            .map { opener ->
                distances
                    .filter { (fromAndTo, _) -> fromAndTo.first == opener.position }
                    .filter { (fromAndTo, _) -> currentState.valve(fromAndTo.second).closed() }
                    .map { (fromAndTo, distance) -> OpenValveAction(fromAndTo.first, fromAndTo.second, distance) }
                    .filter { action -> (opener.elapsedTime + action.time + 1) <= timeLimit }
                    .distinct()
            }
            .fold(listOf(emptyList<OpenValveAction>())) { acc, actions -> acc.addPermutation(actions) }
        if (nextMoves.isEmpty()) {
            currentMax =
                if (currentState.releasedPressure(timeLimit) > currentMax.releasedPressure(timeLimit)) currentState else currentMax
        } else {
            states.addAll(nextMoves
                .filter { actions -> actions.map { it.to }.toSet().size == actions.size }
                .map { currentState.openValves(it) })
        }

        states
            .removeIf { it.openedValves() == currentState.openedValves() && it.releasedPressure(timeLimit) < currentState.releasedPressure(timeLimit) }
        states.removeIf { it.potentialMax(timeLimit, distances) < currentState.releasedPressure(timeLimit) }


    }
    return currentMax
}

data class ValveOpener(val name: String, val position: String, val elapsedTime: Int = 0) {

    fun openValve(valveId: String, time: Int): ValveOpener {
        return copy(position = valveId, elapsedTime = elapsedTime + time)
    }
}

fun calculateDistances(valves: List<Valve>) = valves
    .flatMap { valve ->
        valves
            .filter { it != valve && it.flowRate > 0 }
            .map { Pair(valve.id, it.id) to distance(valve.id, it.id, valves) }
    }.toMap()

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