package day07

interface Node<T> {
    fun value(): Int
    fun addChild(node: Node<T>)
    fun print(depth: Int = 0)
}