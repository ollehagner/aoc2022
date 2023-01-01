package day16

import java.lang.IllegalArgumentException
import java.lang.IllegalStateException

data class State(val openers: List<ValveOpener>, val valves: List<Valve>) {

    fun potentialMax(timeLimit: Int, distances: Map<Pair<String, String>, Int>): Int {
        var remainingTime = (timeLimit * openers.size) - openers.sumOf { it.elapsedTime }
        return releasedPressure(timeLimit) + valves
            .filter { it.closed() }
            .sortedByDescending { it.flowRate }
            .map { it to (openers.minOfOrNull { opener -> distances[opener.position to it.id]!! } ?: throw IllegalStateException(
                "Invalid distance map"
            )) }
            .takeWhile { (_, distance) ->
                remainingTime -= distance
                remainingTime > 0
            }
            .sumOf { (valve, distance) ->
                valve.open(openers.first().elapsedTime + distance).releasedPressure(timeLimit)
            }
    }

    fun openedValves(): Set<String> {
        return this.valves.filter { it.opened }.map { it.id }.toSet()
    }

    fun openerAtValve(id: String): ValveOpener {
        return openers.firstOrNull { it.position == id } ?: throw IllegalArgumentException("No opener at valve $id")
    }

    fun valve(id: String): Valve {
        return valves.firstOrNull { it.id == id } ?: throw IllegalArgumentException("No valve at valve $id")
    }

    fun score(): Int {
        return currentRelease() / openers.sumOf { it.elapsedTime }
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

    fun openValves(actions: List<OpenValveAction>): State {
        return actions.toSet()
            .fold(this) { acc, action ->
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