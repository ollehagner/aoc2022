package day02

enum class Move(val score: Int) {
    ROCK(1) {
        override fun play(move: Move): Result {
            return when(move) {
                ROCK -> Result.DRAW
                PAPER -> Result.LOSS
                SCISSORS -> Result.WIN
            }
        }

    }, PAPER(2) {
        override fun play(move: Move): Result {
            return when(move) {
                ROCK -> Result.WIN
                PAPER -> Result.DRAW
                SCISSORS -> Result.LOSS
            }
        }

    }, SCISSORS(3) {
        override fun play(move: Move): Result {
            return when(move) {
                ROCK -> Result.LOSS
                PAPER -> Result.WIN
                SCISSORS -> Result.DRAW
            }
        }

    };

    abstract fun play(move: Move): Result;
}