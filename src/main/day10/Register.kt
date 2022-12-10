package day10

class Register(private var value: Int) {

    fun add(other: Int) {
        value += other
    }

    fun signalStrength(cycle: Int): Int {
        return value * cycle
    }

    fun value(): Int {
        return value
    }
}