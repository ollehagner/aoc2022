package day16

import log
import readInput
import java.lang.IllegalArgumentException
import java.util.PriorityQueue

fun main() {
    val valves = readInput("day16/input.txt").map { Valve.parse(it) }
    val start = valves.first { it.id == "AA" }.open(0)

    var startingPath = Path(start, 0, valves, actions = listOf("Moved to AA"))
    val startTime = System.currentTimeMillis()
    val bestPath = solve(startingPath)
    println("Day 16 part 1. Total pressure released: ${bestPath.releasedPressure()}")
    println("Solved in ${System.currentTimeMillis() - startTime} ms")
}

fun solve(start: Path): Path {
    val queue = PriorityQueue<Path> { a, b -> (a.score().compareTo(b.score()) * -1) }
    queue.add(start)

    while (queue.peek().elapsedTime < 30) {
        val pathToExplore = queue.poll()
        queue.addAll(pathToExplore.possiblePaths())
        val currentMax = pathToExplore.releasedPressure()
        queue.removeIf { it.score() < currentMax }
    }
    return queue.poll()
}


data class Path(
    val currentPosition: Valve,
    val elapsedTime: Int,
    val valves: List<Valve>,
    val actions: List<String> = ArrayList()
) {

    fun move(destination: String): Path {
        if (!currentPosition.connections.contains(destination)) {
            throw IllegalArgumentException("Unable to move to destination $destination from valve ${currentPosition.id}")
        }
        return copy(
            currentPosition = valve(destination),
            elapsedTime = elapsedTime + 1,
            actions = actions + "Move to $destination"
        )

    }

    fun possiblePaths(): List<Path> {
        if (valves.all { it.opened }) return listOf(copy(elapsedTime = elapsedTime + 1))
        val openValve = if (currentPosition.closed()) listOf(openValve()) else emptyList()
        val moves =
            currentPosition
                .connections
                .filter {
                    actions.size < 2 || !actions[actions.size - 2].contains(it)
                }
                .map { move(it) }
//            .sortedBy { !it.currentPosition.opened }
        return openValve + moves
    }

    private fun openValve(): Path {
        val openedValve = currentPosition.open(elapsedTime + 1)
        val newValves = valves
            .filter { it.id != currentPosition.id }
            .fold(mutableListOf(openedValve)) { acc, valve ->
                acc.add(valve)
                acc
            }
        return copy(
            currentPosition = openedValve,
            elapsedTime = elapsedTime + 1,
            valves = newValves,
            actions = actions + "Opened valve ${openedValve.id}"
        )
    }

    private fun valve(id: String): Valve {
        return valves.first { it.id == id }
    }

    fun releasedPressure(): Int {
        return valves
            .filter { it.opened }
            .sumOf { it.releasedPressure() }
    }

    fun score(): Int {
        val currentPositionScore = if (currentPosition.closed()) currentPosition.flowRate * (29 - elapsedTime) else 0
        return releasedPressure() + currentPositionScore +
                valves
                    .asSequence()
                    .filter { it.closed() && it != currentPosition }
                    .sortedByDescending { it.flowRate }
                    .mapIndexed { index, valve ->
                        index + 1 to valve
                    }
                    .map { (timeToNextValve, valve) -> (timeToNextValve * 2) + elapsedTime to valve }
                    .filter { (time, _) -> time < 30 }
                    .sumOf { (time, valve) -> (30 - time) * valve.flowRate }
    }

    override fun toString(): String {
        return "(currentPosition=${currentPosition.id}, elapsedTime=$elapsedTime}, score=${score()}, last action=${actions.lastOrNull()})"
    }


}

data class Valve(
    val id: String,
    val flowRate: Int,
    val connections: List<String>,
    val opened: Boolean = false,
    val openedAt: Int = -1
) {
    companion object {
        fun parse(input: String): Valve {
            val id = input.substringAfter("Valve ").substringBefore(" has")
            val flowRate = input.substringAfter("flow rate=").substringBefore(";")
            val connections = if (input.contains("valves"))
                input.substringAfter("valves").split(",").map { it.trim() }
            else
                input.substringAfter("valve").split(",").map { it.trim() }
            return Valve(id, flowRate.toInt(), connections)
        }
    }

    fun closed(): Boolean {
        return !opened
    }

    fun open(time: Int): Valve {
        return this.copy(opened = true, openedAt = time)
    }

    fun releasedPressure(): Int {
        return flowRate * (30 - openedAt)
    }
}