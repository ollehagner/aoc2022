package day05

import java.util.*
import kotlin.collections.HashMap

private const val CRATE_DATA_LENGTH = 4

private const val CRATE_LABEL_POSITION = 1

class Stacks(input: List<String>) {
    private val contents: Map<Int, Deque<Crate>>

    init {
        contents = HashMap()
        val stackdata = input
            .takeWhile { it.isNotEmpty() }
            .dropLast(1)
            .reversed()

        stackdata.forEach { line ->
            line.chunked(CRATE_DATA_LENGTH)
                .forEachIndexed { index, value ->
                    if(value.isNotBlank()) {
                        val stackNumber = index + 1
                        val stack = contents.getOrDefault(stackNumber, LinkedList())
                        stack.push(Crate(value[CRATE_LABEL_POSITION]))
                        contents.put(stackNumber, stack)
                    }
                }
        }
    }

    fun executeSerialMove(move: Move) {
        repeat(move.quantity) {
            val crate = contents[move.from]!!.pop()
            val toStack = contents[move.to]!!
            toStack.push(crate)
        }
    }

    fun executeSimultaneousMove(move: Move) {
        val intermediateStack = LinkedList<Crate>()
        repeat(move.quantity) {
            val crate = contents[move.from]!!.pop()
            intermediateStack.push(crate)
        }
        val toStack = contents[move.to]!!
        intermediateStack.forEach { toStack.push(it) }
    }

    fun topOfEachStack(): String {
        return contents.values
            .filter { it.isNotEmpty() }
            .map { it.peek()!!.label }
            .joinToString("")
    }

}