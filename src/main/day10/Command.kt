package day10

interface Command {
    fun execute(register: Register)
}

class Noop : Command {
    override fun execute(register: Register) {

    }
}

class Add(val toAdd: Int) : Command {
    override fun execute(register: Register) {
        register.add(toAdd)
    }
}

fun parseInput(input: List<String>): List<Command> {
    return input
        .flatMap { row ->
            when(row.substringBefore(" ")) {
                "addx" -> listOf(Noop(), Add(row.substringAfter(" ").toInt()))
                else -> listOf(Noop())
            }
        }
}