package day15

import plane.Coord
import plane.mdist
import plane.x
import plane.y
import readResourceAsBufferedReader
import kotlin.math.absoluteValue

fun main() {
    println("part 1: ${part1(readResourceAsBufferedReader("15_1.txt").readLines())}")
    //println("part 2: ${part2(readResourceAsBufferedReader("15_1.txt").readLines())}")
}

fun part1(input: List<String>, y: Int = 2000000): Int {
    val sensors = input.map { Sensor.parse(it) }
    val overlaps = sensors.flatMap { it.yOverlap(y) }
        .toSet()

    val beacons = sensors.map { it.closestBeacon }
        .filter { it.y() == y }
        .toSet()

    val noBeacons = overlaps - beacons

    return noBeacons.size
}

fun part2(input: List<String>): Int {
    TODO()
}

data class Sensor(val pos: Coord, val closestBeacon: Coord) {

    val manhattanDistance = pos.mdist(closestBeacon)

    fun yOverlap(y: Int): Set<Coord> {
        val yDist = (pos.y() - y).absoluteValue
        return if (yDist > manhattanDistance) {
            emptySet()
        } else {
            val xOffset = (manhattanDistance - yDist).absoluteValue
            val left = pos.x() - xOffset
            val right = pos.x() + xOffset
            (left .. right).map { y to it }
                .toSet()
        }
    }

    companion object {
        val xy: Regex = Regex("""x=(-?\d+), y=(-?\d+)""")

        fun parse(line: String): Sensor {
            val (sensor, beacon) = xy.findAll(line).toList()
                .map {
                    val (x, y) = it.destructured
                    y.toInt() to x.toInt()
                }

            return Sensor(sensor, beacon)
        }
    }

}