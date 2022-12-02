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

        override fun moveForResult(result: Result): Move {
            return when(result) {
                Result.LOSS -> PAPER;
                Result.WIN -> SCISSORS
                Result.DRAW -> ROCK
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

        override fun moveForResult(result: Result): Move {
            return when(result) {
                Result.LOSS -> SCISSORS;
                Result.WIN -> ROCK
                Result.DRAW -> PAPER
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

        override fun moveForResult(result: Result): Move {
            return when(result) {
                Result.LOSS -> ROCK;
                Result.WIN -> PAPER
                Result.DRAW -> SCISSORS
            }
        }
    };

    abstract fun play(move: Move): Result;
    abstract fun moveForResult(result: Result): Move;
}