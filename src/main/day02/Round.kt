package day02

class Round(val left: Move, val right: Move) {
    fun score(): Pair<Int, Int> {
        val leftScore = points(left.play(right)) + left.score
        val rightScore = points(right.play(left)) + right.score
        return Pair(leftScore, rightScore)
    }

    private fun points(result: Result): Int {
        return when(result) {
            Result.WIN -> 6
            Result.DRAW -> 3
            Result.LOSS -> 0
        }
    }

    override fun toString(): String {
        return "Round(left=$left, right=$right)"
    }


}