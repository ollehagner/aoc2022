package day16

import readInput

fun main() {
    val valves = readInput("day16/testinput.txt").map { Valve.parse(it) }
    val agents = listOf(Agent("You", "AA"), Agent("Elephant", "AA"))
    var startingPath = Path(agents, 0, valves, actions = emptyList())
    val timeLimit = 26
    val startTime = System.currentTimeMillis()

//    startingPath
//        .performActions(listOf(MoveAction("AA", "II"),MoveAction("AA", "DD")))
//        .performActions(listOf(MoveAction("II", "JJ"),OpenValveAction("DD")))
//        .performActions(listOf(OpenValveAction("JJ"),MoveAction("DD", "EE")))
//        .performActions(listOf(MoveAction("JJ", "II"), MoveAction("EE","FF")))
//        .performActions(listOf(MoveAction("II", "AA"),MoveAction("FF", "GG")))
//        .performActions(listOf(MoveAction("AA", "BB"),MoveAction("GG", "HH")))
//        .performActions(listOf(OpenValveAction("BB"),OpenValveAction("HH")))
//        .performActions(listOf(MoveAction("BB", "CC"),MoveAction("HH", "GG")))
//        .performActions(listOf(OpenValveAction( "CC"),MoveAction("GG", "FF")))
//        .performActions(listOf(MoveAction("FF", "EE")))
//        .performActions(listOf(OpenValveAction( "EE")))
//        .possiblePaths().forEach { println("${it.score(timeLimit)} : ${it}") }

    val bestPath = solve(startingPath, timeLimit)
    println("Day 16 part 2. Total pressure released: ${bestPath.releasedPressure(timeLimit)}")
    println("Solved in ${System.currentTimeMillis() - startTime} ms")
    bestPath.actions.chunked(2).forEachIndexed() { index, action ->
        println("${index + 1}. ${action.joinToString("\n")}")
        println()
    }

}