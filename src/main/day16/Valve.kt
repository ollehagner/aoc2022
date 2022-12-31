package day16

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