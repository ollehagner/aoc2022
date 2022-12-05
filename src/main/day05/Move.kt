package day05

class Move(instruction: String) {

    private val pattern = Regex("""move (\d+) from (\d+) to (\d+)""")

    val from: Int
    val to: Int
    val quantity: Int
    init {
        val (quantityValue, fromValue, toValue) = pattern.find(instruction)!!.destructured
        quantity = quantityValue.toInt()
        from = fromValue.toInt()
        to = toValue.toInt()
    }

    override fun toString(): String {
        return "Move(from=$from, to=$to, quantity=$quantity)"
    }


}