package day03

import java.lang.IllegalArgumentException

class Rucksack {

    val firstCompartmentContents: String
    val secondCompartmentContents: String
//    private val priorities: Map<Char, Int>

    constructor(contents: String) {
        if(contents.length % 2 != 0) {
            throw IllegalArgumentException("Rucksack content length must be even")
        }

        val compartmentContents = splitInCompartments(contents)
        firstCompartmentContents = compartmentContents.first
        secondCompartmentContents = compartmentContents.second

//        priorities = generatePriorities()
    }

//    private fun generatePriorities(): Map<Char, Int> {
//        return ('a'..'z').union('A'..'Z')
//            .mapIndexed{ index, value -> value to index + 1}
//            .toMap()
//    }

    private fun splitInCompartments(contents: String): Pair<String, String> {
        val middleIndex = contents.length / 2
        return Pair(contents.substring(0, middleIndex), contents.substring(middleIndex))
    }

    fun itemsInBothCompartments(): Set<Char> {
        val firstCompartmentItemSet = firstCompartmentContents.toSet()
        val secondCompartmentItemSet = secondCompartmentContents.toSet()
        return firstCompartmentItemSet.intersect(secondCompartmentItemSet)
    }

//    fun priority(item: Char): Int {
//        return priorities.getOrDefault(item, 0)
//    }

    companion object {
        private val priorities2 = ('a'..'z').union('A'..'Z')
            .mapIndexed{ index, value -> value to index + 1}
            .toMap()

        fun priority(item: Char): Int {
            return priorities2.getOrDefault(item, 0)
        }
    }
}