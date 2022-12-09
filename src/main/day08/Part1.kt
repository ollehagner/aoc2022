package day08

import common.Point
import readInput
typealias TreeRow = List<Tree>
typealias Column = List<Int>

fun main() {
//    val input = readInput("day08/testinput.txt")
    val input = readInput("day08/input.txt")
    val inputRows = inputAsRows(input)
    val inputRowsReversed = inputRows.map { it.reversed() }
    val inputColumns = inputAsColumns(inputRows)
    val inputColumnsReversed = inputColumns.map { it.reversed() }

    val treesInSight = listOf(inputRows, inputRowsReversed, inputColumns, inputColumnsReversed).flatten()
        .map { trees -> inSight(trees)  }
        .flatten()
        .toSet()
        .count();
    println("Day 08 part 1, trees in sight: $treesInSight")
}

fun inSight(trees: TreeRow): Set<Tree> {
    return trees
        .drop(1)
        .fold(listOf(trees.first())) { acc, tree ->
            if(tree.height > acc.maxOf { it.height }) acc + tree else acc
        }
        .toSet()
}

fun inputAsRows(input: List<String>): List<TreeRow> {
    return input
        .mapIndexed { y, row ->
            row.withIndex()
                .map { indexedValue ->
                    Tree(indexedValue.value.digitToInt(), Point(indexedValue.index, y))
                }
        }
}

fun inputAsColumns(input: List<TreeRow>): List<TreeRow> {
    return (0 until input.first().size).map { x ->
        input.mapIndexed { y, trees ->
            trees[x]
        }
    }
}

data class Tree(val height: Int, val position: Point)