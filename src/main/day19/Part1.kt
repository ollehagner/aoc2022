package day19

import day19.Resource.*
import groupUntil
import readInput
import java.util.*

typealias Materials = Map<Resource, Int>

fun main() {
    val blueprints = parseTestInput()
    val robots = Robots(mapOf(Robot(ORE) to 1, Robot(CLAY) to 1))
    val materials = mapOf(ORE to 2, CLAY to 1)

    val qualityLevel = solve(blueprints.first(), 24)
//        .sumOf { solve(it, 24) }
    println("Day 19 part 1. Quality level: $qualityLevel ")
}

fun solve(blueprint: Blueprint, timelimit: Int): Int {
    println("New blueprint: $blueprint")
    val queue = PriorityQueue<HarvestState> { a, b -> a.elapsedTime.compareTo(b.elapsedTime) }
//    val queue = LinkedList<HarvestState>()
    val startingState = HarvestState(Robots(mapOf(Robot(ORE) to 1)), startingMaterials(), 0)
    queue.add(startingState)
    var max = 0;
    val seen = mutableSetOf<HarvestState>()
    var iterations = 0
    while (queue.isNotEmpty()) {
        val current = queue.poll()!!
        seen.add(current)
//        println(current)
        val possibleBuilds = blueprint.possibleBuilds(current.materials)
        val harvestedMaterials = current.robots.work()
        possibleBuilds
            .map { (robot, materialsLeft) ->
                HarvestState(current.robots.add(robot), materialsLeft + harvestedMaterials, current.elapsedTime + 1)
            }
            .forEach { queue.add(it) }
        queue.add(HarvestState(current.robots, current.materials + harvestedMaterials, current.elapsedTime + 1))

        val possiblemax = queue
            .filter { it.elapsedTime == timelimit }
            .maxOfOrNull { it.geodes() } ?: 0

        max = maxOf(possiblemax, max)

        queue.removeIf { it.elapsedTime == timelimit }
        queue.removeIf { seen.contains(it) }

    }
    return max * blueprint.id
}

data class HarvestState(val robots: Robots, val materials: Materials, val elapsedTime: Int) {

    fun score(): Int {
        return robots.instances
            .map { (robot, quantity) ->
                when(robot.resource) {
                    ORE -> 1 * quantity
                    CLAY -> 10 * quantity
                    OBSIDIAN -> 100 * quantity
                    GEODE -> 100000 * quantity
                }
            }
            .sum() / (elapsedTime + 1)
    }

    fun geodes(): Int {
        return materials.getOrDefault(GEODE, 0)
    }


}

data class Blueprint(val id: Int, private val constructions: List<Construction>) {
    fun possibleBuilds(availableMaterials: Materials): List<Pair<Robot, Materials>> {
        return constructions
            .filter { it.buildable(availableMaterials) }
            .map { it.build(availableMaterials) }
    }

    companion object {
        fun parse(input: List<String>): Blueprint {
            val blueprintId = input.first().substringAfter("Blueprint ").substringBefore(":").toInt()
            val constructions = input
                .drop(1)
                .map { Construction.parse(it) }
            return Blueprint(blueprintId, constructions)
        }
    }
}

data class Construction(val resource: Resource, val requiredMaterials: Materials) {
    fun buildable(availableMaterials: Materials): Boolean {
        return requiredMaterials.all { (resource, requiredQuantity) ->
            availableMaterials.getOrDefault(
                resource,
                0
            ) >= requiredQuantity
        }
    }

    fun build(availableMaterials: Materials): Pair<Robot, Materials> {
        return Robot(resource) to availableMaterials - requiredMaterials
    }

    companion object {
        fun parse(input: String): Construction {
            val resource = input.substringAfter("Each ").substringBefore(" robot").let { valueOf(it.uppercase()) }
            if (input.contains("and")) {
                val firstMaterial = input.substringAfter("costs ").substringBefore(" and")
                    .split(" ")
                    .let { (quantity, resourceName) -> valueOf(resourceName.uppercase()) to quantity.toInt() }
                val secondMaterial = input.substringAfter("and ").substringBefore(".")
                    .split(" ")
                    .let { (quantity, resourceName) -> valueOf(resourceName.uppercase()) to quantity.toInt() }
                return Construction(resource, mapOf(firstMaterial, secondMaterial))
            } else {
                val firstMaterial = input.substringAfter("costs ").substringBefore(".")
                    .split(" ")
                    .let { (quantity, resourceName) -> valueOf(resourceName.uppercase()) to quantity.toInt() }
                return Construction(resource, mapOf(firstMaterial))
            }
        }
    }
}

data class Robots(val instances: Map<Robot, Int>) {
    fun work(): Materials {
        return instances
            .map { (robot, quantity) -> robot.resource to quantity }
            .toMap()
    }

    fun add(robot: Robot): Robots {
        return if(!instances.containsKey(robot)) {
            Robots(instances + (robot to 1))
        } else {
            instances
                .map { (key, quantity) ->
                    if (key == robot) {
                        key to quantity + 1
                    } else {
                        key to quantity
                    }
                }
                .toMap().let { robotMap -> Robots(robotMap) }
        }
    }
}

data class Robot(val resource: Resource) {

}

enum class Resource {
    ORE, CLAY, OBSIDIAN, GEODE;

}

operator fun Materials.minus(other: Materials): Materials {
    return this
        .map { (resource, quantity) -> resource to (quantity - other.getOrDefault(resource, 0)) }
        .toMap()
}

operator fun Materials.plus(other: Materials): Materials {
    return this
        .map { (resource, quantity) -> resource to (quantity + other.getOrDefault(resource, 0)) }
        .toMap()
}

fun parseTestInput(): List<Blueprint> {
    return readInput("day19/testinput.txt")
        .groupUntil { line -> line.isBlank() }
        .map { Blueprint.parse(it.filter { line -> !line.isBlank() }) }
}

fun startingMaterials(): Materials {
    return mapOf(ORE to 0, CLAY to 0, OBSIDIAN to 0, GEODE to 0)
}