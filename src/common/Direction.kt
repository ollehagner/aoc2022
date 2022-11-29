package common

enum class Direction {
    UP {
        override fun from(from: Point, steps: Int): Point {
            return Point(from.x, from.y + steps)
        }
    }, DOWN {
        override fun from(from: Point, steps: Int): Point {
            return Point(from.x, from.y - steps)
        }
    }, RIGHT {
        override fun from(from: Point, steps: Int): Point {
            return Point(from.x + steps, from.y)
        }
    }, LEFT {
        override fun from(from: Point, steps: Int): Point {
            return Point(from.x - steps, from.y)
        }
    };

    abstract fun from(from: Point, steps: Int = 1): Point;
}
