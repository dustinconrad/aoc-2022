package plane

import kotlin.math.pow
import kotlin.math.sqrt

typealias Coord = Pair<Int, Int>

fun Coord.distance(): Double {
    return sqrt(first.toDouble().pow(2) + second.toDouble().pow(2))
}

operator fun Coord.plus(other: Coord): Coord {
    return this.first + other.first to this.second + other.second
}

fun Coord.step(vector: Coord) = generateSequence(this) {
    it + vector
}

fun Coord.x() = second
fun Coord.y() = first

fun Coord.right() = this.step(0 to 1)

fun Coord.left() = this.step(0 to -1)

fun Coord.down() = this.step(1 to 0)

fun Coord.up() = this.step(-1 to 0)

