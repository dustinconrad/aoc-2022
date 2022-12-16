package day15

import geometry.Coord
import geometry.combine
import geometry.mdist
import geometry.overlaps
import geometry.x
import geometry.y
import readResourceAsBufferedReader
import kotlin.math.absoluteValue

fun main() {
    println("part 1: ${part1(readResourceAsBufferedReader("15_1.txt").readLines())}")
    //println("part 2: ${part2(readResourceAsBufferedReader("15_1.txt").readLines())}")
}

fun part1(input: List<String>, y: Int = 2000000): Int {
    val sensors = input.map { Sensor.parse(it) }
    val overlaps = sensors.foldRight(mutableSetOf<IntRange>()) { sensor, acc ->
        val sensorOverlap = sensor.yOverlap(y)
        val overlaps = acc.filter { it.overlaps(sensorOverlap) }.toSet()

        if (overlaps.isEmpty()) {
            acc.add(sensorOverlap)
            acc
        } else {
            acc.removeAll(overlaps)
            val combined = overlaps.fold(sensorOverlap) { l, r -> l.combine(r) }
            acc.add(combined)
            acc
        }
    }

    var overlapCount = overlaps.sumOf { it.count() }

    val beacons = sensors.map { it.closestBeacon }
        .filter { it.y() == y }
        .toSet()

    beacons.forEach { beacon ->
        if (overlaps.any { it.contains(beacon.x()) })  {
            overlapCount--
        }
    }

    return overlapCount
}

fun part2(input: List<String>): Int {
    TODO()
}

data class Sensor(val pos: Coord, val closestBeacon: Coord) {

    val manhattanDistance = pos.mdist(closestBeacon)

    fun yOverlap(y: Int): IntRange {
        val yDist = (pos.y() - y).absoluteValue
        return if (yDist > manhattanDistance) {
            IntRange.EMPTY
        } else {
            val xOffset = (manhattanDistance - yDist).absoluteValue
            val left = pos.x() - xOffset
            val right = pos.x() + xOffset
            return (left .. right)
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