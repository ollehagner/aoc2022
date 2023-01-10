package day17

import common.Grid

fun main() {
    val totalIterations = 1000000000000
    val jetsfile = "day17/input.txt"
    val (firstOccurrence, secondOccurrence) = countPeriodicity(jetsfile)
    val period = secondOccurrence - firstOccurrence
    val heightAtFirstOccurrence = solve(firstOccurrence, jetsfile)
    val periodHeight = solve(secondOccurrence, jetsfile) - heightAtFirstOccurrence
    val totalLoops = (totalIterations - firstOccurrence) / period
    val remainder = (totalIterations - firstOccurrence) % period
    val firstAndReminderHeight = solve((firstOccurrence + remainder).toInt(), jetsfile)
    val totalHeight = firstAndReminderHeight + (periodHeight * totalLoops)
    println("Day 17 part 2. Highest point: $totalHeight")
}

fun countPeriodicity(jetsFile: String): Pair<Int, Int> {
    val rocks = Rock.rockIterator()
    val jets =Jet.parse(jetsFile)
    var grid = Grid(Floor.start().points, "-")
    val seen = mutableMapOf<State, Int>()
    var iterations = 0

    while(true) {
        iterations++
        val state = dropRock(startPositionRelativeToFloor(rocks.next(), grid.max().y), grid, jets)
        state.rock.parts.forEach { grid.set(it, "#") }
        val normalizedState = state.copy(floor = state.floor.normalized())
        if(seen.containsKey(normalizedState)) {
            return seen[normalizedState]!! to iterations
        }
        seen[normalizedState] = iterations
    }
}

