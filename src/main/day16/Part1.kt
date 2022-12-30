package day16

import log
import permutations
import readInput
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException
import java.util.*
import kotlin.collections.ArrayList

fun main() {
    val valves = readInput("day16/testinput.txt").map { Valve.parse(it) }
    val agents = listOf(Agent("You", "AA"))
    var startingPath = Path(agents, 0, valves, actions = emptyList())
    val timeLimit = 30
    val startTime = System.currentTimeMillis()

    val bestPath = solve(startingPath, timeLimit)
    println("Day 16 part 1. Total pressure released: ${bestPath.releasedPressure(timeLimit)}")
    println("Solved in ${System.currentTimeMillis() - startTime} ms")
//    bestPath.actions.forEach { println(it) }
}



fun solve(start: Path, timeLimit: Int): Path {
    val queue = PriorityQueue<Path> { a, b -> (a.score(timeLimit).compareTo(b.score(timeLimit)) * -1) }
    queue.add(start)
    var iterations = 0
    var pathToExplore: Path = start
    while (!queue.peek().allValvesOpen()) {
//        queue.take(2).forEach { println(it.score(timeLimit)) }
//        println()
        pathToExplore = queue.poll()
        queue.addAll(pathToExplore.possiblePaths())
        val currentMax = pathToExplore.releasedPressure(timeLimit)
        queue.removeIf { it.score(timeLimit) < currentMax }
        iterations++
    }
    println("$iterations iterations")
    return pathToExplore
}

data class Agent(val name: String, val position: String)

interface Action {
    fun perform(path: Path): Path
}

data class OpenValveAction(private val valveId: String): Action {
    override fun perform(path: Path): Path {
        return path.openValve(valveId)
    }

    override fun toString(): String {
        return "Open Valve $valveId"
    }
}

data class MoveAction(private val fromValve: String, private val toValve: String): Action {
    override fun perform(path: Path): Path {
        return path.move(fromValve, toValve)
    }

    override fun toString(): String {
        return "Move from $fromValve to $toValve"
    }
}

data class Path(
    val agents: List<Agent>,
    val elapsedTime: Int,
    val valves: List<Valve>,
    val actions: List<String> = ArrayList()
) {

    fun performActions(actions: List<Action>): Path {
        return actions
            .fold(this) { state, action -> action.perform(state) }
            .copy(elapsedTime = elapsedTime + 1)
    }

    fun allValvesOpen(): Boolean {
        return valves
            .filter { it.flowRate > 0 }
            .all { it.opened }
    }

    fun possiblePaths(): List<Path> {
        if (valves.all { it.opened }) return listOf(copy(elapsedTime = elapsedTime + 1))
        val agentActions = agents
            .map { agent ->
                val currentValve = valve(agent.position)
                (if(currentValve.closed()) listOf(OpenValveAction(currentValve.id)) else emptyList<Action>()) +
                currentValve.connections
                    .filter { connection ->
                        actions.size < 2 || !actions.chunked(2).map { it.joinToString(". ") }
                            .takeLast(1)
                            .contains(" from $connection")
                    }
                    .map { destination -> MoveAction(currentValve.id, destination) }
            }
        return if(agentActions.size > 1) {
            val permutations = agentActions.first().permutations(agentActions.last())
                .filter { it.first != it.second }
                .distinct()
            permutations.map { actions -> actions.second.perform(actions.first.perform(this)).copy(elapsedTime = elapsedTime + 1) }
        } else {
            agentActions.first().map { action -> action.perform(this).copy(elapsedTime = elapsedTime + 1) }
        }
    }

    fun valve(id: String): Valve {
        return valves.first { it.id == id }
    }

    fun move(from: String, destination: String): Path {
        val invalidDestination = valves
            .filter { valve -> agents.any { agent -> agent.position == valve.id } }
            .map { it.connections }
            .none { it.contains(destination)}
        if (invalidDestination) {
            throw IllegalArgumentException("Unable to move to destination $destination from valve ${agents.map { it.position }}")
        }
        val agentToMove = agents.first { it.position == from }
        return copy(
            agents = agents.filter { agent -> agent != agentToMove } + agentToMove.copy(position = destination),
            actions = actions + "${agentToMove.name} moved from ${agentToMove.position} to $destination"
        )

    }

    fun openValve(id: String): Path {
        val agentToOpen = agents.firstOrNull { it.position == id } ?: throw IllegalStateException("No agent present at $id")

        val openedValve = valve(id).open(elapsedTime + 1)
        val newValves = valves
            .filter { it.id != openedValve.id }
            .fold(mutableListOf(openedValve)) { acc, valve ->
                acc.add(valve)
                acc
            }
        return copy(
            agents = agents,
            valves = newValves,
            actions = actions + "$agentToOpen open valve ${openedValve.id}"
        )
    }

    fun releasedPressure(timeLimit: Int): Int {
        return valves
            .filter { it.opened }
            .sumOf { it.releasedPressure(timeLimit) }
    }

    fun score(timeLimit: Int): Int {
        val agentPositionScores = agents.map { it.position }
            .map { valve(it) }
            .filter { it.closed() }
            .fold(0) { acc, valve -> acc + valve.flowRate * (timeLimit - (elapsedTime + 1))}
        return releasedPressure(timeLimit) + agentPositionScores +
                valves
                    .asSequence()
                    .filter { it.closed() && !agents.map { agent -> agent.position }.contains(it.id) }
                    .filter { it.flowRate != 0 }
                    .sortedByDescending { it.flowRate }
//                    .mapIndexed { index, valve ->
//                        index + 1 to valve
//                    }
//                    .map { (timeToNextValve, valve) ->
//                        (timeToNextValve * 2) + elapsedTime to valve }
//                    .filter { (time, _) -> time < timeLimit }
//                    .sumOf { (time, valve) -> (timeLimit - time) * valve.flowRate }
                    .sumOf { it.releasedPressure(timeLimit) }
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

    fun releasedPressure(time: Int): Int {
        return flowRate * (time - openedAt)
    }
}