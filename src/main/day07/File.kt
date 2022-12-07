package day07

import java.lang.IllegalArgumentException

class File(val name: String, val value: Int) : Node<Int> {

    override fun value(): Int {
        return value
    }

    override fun print(depth: Int) {
        repeat(depth) { print("\t") }
        println("- $name (file, size=$value)")
    }

    override fun addChild(node: Node<Int>) {
        throw IllegalArgumentException("Files can't have children")
    }

    override fun toString(): String {
        return "File(name='$name')"
    }
}