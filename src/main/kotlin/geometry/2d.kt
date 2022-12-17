package geometry

import kotlin.math.absoluteValue
import kotlin.math.pow
import kotlin.math.sqrt

typealias Coord = Pair<Int, Int>

typealias Lcoord = Pair<Long, Long>

fun Coord.distance(): Double {
    return sqrt(first.toDouble().pow(2) + second.toDouble().pow(2))
}

operator fun Coord.plus(other: Coord): Coord {
    return this.first + other.first to this.second + other.second
}

fun Lcoord.cplus(other: Coord): Lcoord {
    return this.first + other.first to this.second + other.second
}

fun Lcoord.lplus(other: Lcoord): Lcoord {
    return this.first + other.first to this.second + other.second
}

fun Coord.step(vector: Coord) = generateSequence(this) {
    it + vector
}

fun Coord.x() = second
fun Coord.y() = first

fun Lcoord.x() = second
fun Lcoord.y() = first

fun Coord.right() = this.step(0 to 1)

fun Coord.left() = this.step(0 to -1)

fun Coord.down() = this.step(1 to 0)

fun Coord.up() = this.step(-1 to 0)

fun Coord.mdist(other: Coord): Int {
    return (this.y() - other.y()).absoluteValue + (this.x() - other.x()).absoluteValue
}

fun Coord.pathTo(end: Coord): Set<Coord> {
    return if (this.y() == end.y()) {
        val y = this.y()
        (minOf(this.x(), end.x())..maxOf(this.x(), end.x()))
            .map { y to it }
            .toSet()
    } else {
        val x = this.x()
        (minOf(this.y(), end.y())..maxOf(this.y(), end.y()))
            .map { it to x }
            .toSet()
    }
}