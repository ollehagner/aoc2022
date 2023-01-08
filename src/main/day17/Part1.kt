package day17

import LoopingIterator
import common.Direction
import common.Direction.*
import common.Grid
import common.Point
import readInput

fun main() {
    val result = solve(2022, "day17/testinput.txt")
    println("Day 17 part 1. Highest point: ${result}")
}

fun solve(iterations: Int, jetsFile: String): Int {
    val rocks = Rock.rockSequence()
    val jets = Jet.parse(jetsFile)
    var grid = Grid(Floor.start().points, "-")

    rocks
        .take(iterations)
        .forEach { rock ->
            val state = dropRock(startPositionRelativeToFloor(rock, grid.max().y), grid, jets)
            state.rock.parts.forEach { grid.set(it, "#") }
        }
    return grid.max().y
}

fun dropRock(rock: Rock, grid: Grid<String>, directions: LoopingIterator<Jet>): State {
    var movingRock = rock
    while(true) {
        val nextJet = directions.next()
        movingRock = movingRock.move(nextJet.direction, grid)
        if(movingRock.move(DOWN, grid).parts.any { grid.hasValue(it) }) {
            return State(nextJet, movingRock, Floor(grid))
        }
        movingRock = movingRock.move(DOWN, grid)
    }
}

fun startPositionRelativeToFloor(rock: Rock, maxY: Int): Rock {
    return Rock(rock.id, rock.parts.map { (x, y) -> Point(x + 2, y + maxY + 4) })
}

data class State(val jet: Jet, val rock: Rock, val floor: Floor)

data class Jet(val id: Int, val direction: Direction) {
    companion object {
        fun parse(jetsFile: String): LoopingIterator<Jet> {
            return LoopingIterator(readInput(jetsFile).first().mapIndexed { index, value -> if(value == '<') Jet(index, LEFT) else Jet(index, RIGHT)})
        }
    }
}
