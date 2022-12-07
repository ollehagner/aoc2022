package day07

class Directory(private val name: String) : Node<Int> {

    private val children = mutableListOf<Node<Int>>()
    override fun value(): Int {
        return children
            .sumOf { it.value() }
    }

    override fun print(depth: Int) {
        repeat(depth) { print("\t") }
        println("- $name (dir, size=${value()})")
        children
            .forEach { it.print(depth + 1) }
    }

    override fun addChild(node: Node<Int>) {
        children.add(node)
    }

    fun subDirs(): List<Directory> {
        return children
            .filterIsInstance<Directory>()
    }

    override fun toString(): String {
        return "Directory(name='$name')"
    }
}