package day15

import geometry.Coord
import geometry.combine
import geometry.fullyContains
import geometry.mdist
import geometry.overlaps
import geometry.x
import geometry.y
import readResourceAsBufferedReader
import kotlin.math.absoluteValue

fun main() {
    println("part 1: ${part1(readResourceAsBufferedReader("15_1.txt").readLines())}")
    println("part 2: ${part2(readResourceAsBufferedReader("15_1.txt").readLines())}")
}

fun part1(input: List<String>, y: Int = 2000000): Int {
    val sensors = input.map { Sensor.parse(it) }
    val overlaps = overlapsOnY(sensors, y)

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

fun overlapsOnY(sensors: List<Sensor>, y: Int): Set<IntRange> {
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

    return overlaps
}

fun part2(input: List<String>, tl: Coord = 0 to 0, br: Coord = 4000000 to 4000000): Long {
    val sensors = input.map { Sensor.parse(it) }

    val xRange = (tl.x()..br.x())
    for (y in tl.y()..br.y()) {
        val coord = emptyOnY(sensors, y, xRange)
        if (coord != null) {
            return coord.x().toLong() * 4000000 + coord.y()
        }
    }
    throw IllegalArgumentException()
}

fun emptyOnY(sensors: List<Sensor>, y: Int, xRange: IntRange): Coord? {
    val overlapsOnY = overlapsOnY(sensors, y)

    val spots = overlapsOnY.filter { it.fullyContains(xRange) }

    return if (spots.isEmpty()) {
        val sorted = overlapsOnY.sortedBy { it.first }
        val (l, r) = sorted
        if (r.first - l.last != 2) {
            throw IllegalArgumentException()
        } else {
            y to (r.first - 1)
        }
    } else {
        null
    }
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