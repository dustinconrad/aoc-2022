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