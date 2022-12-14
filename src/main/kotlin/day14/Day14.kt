package day14

import plane.Coord
import plane.down
import plane.pathTo
import plane.plus
import plane.x
import plane.y
import readResourceAsBufferedReader

fun main() {
    println("part 1: ${part1(readResourceAsBufferedReader("14_1.txt").readLines())}")
    //println("part 2: ${part2(readResourceAsBufferedReader("14_1.txt").readLines())}")
}

fun part1(input: List<String>): Int {
    val paths = input.map { RockPath.parse(it) }
    val scan = Scan(paths)
    val result = scan.start()
    println(scan)
    return result
}

fun part2(input: List<String>): Int {
    TODO()
}

enum class Fill(val c: Char) {
    Air('.'),
    Rock('#'),
    Sand('o');

    override fun toString(): String {
        return c.toString()
    }}

class Scan(paths: List<RockPath>) {

    private val topLeft: Coord
    private val bottomRight: Coord

    private val coords: MutableMap<Coord, Fill>

    init {
        val rocks = mutableSetOf<Coord>()
        paths.forEach { rocks.addAll(it.path) }

        val minX = rocks.minBy { it.x() }.x()
        val minY = minOf(0, rocks.minBy { it.y() }.y())

        val maxX = rocks.maxBy { it.x() }.x()
        val maxY = rocks.maxBy { it.y() }.y()

        topLeft = minY to minX
        bottomRight = maxY to maxX

        coords = rocks.associateWith { Fill.Rock }.toMutableMap()
    }

    fun start(): Int {
        while(simulateSand(0 to 500)) {
//            println(this)
//            println()
        }
        return coords.count { it.value == Fill.Sand }
    }

    private fun at(c: Coord): Fill {
        return coords.getOrDefault(c, Fill.Air)
    }

    private fun simulateSand(sandSrc: Coord): Boolean {
        val lastAir = sandSrc.down().takeWhile { it.y() <= bottomRight.y() && at(it) == Fill.Air }.lastOrNull()
        if (lastAir == null || lastAir.y() >= bottomRight.y()) {
            return false
        }
        // try down and left
        val dl = lastAir + (1 to -1)
        if (dl.x() < topLeft.x()) {
            return false
        } else if (at(dl) == Fill.Air) {
            return simulateSand(dl)
        }

        // try down and right
        val dr = lastAir + (1 to 1)
        if (dr.x() > bottomRight.x()) {
            return false
        } else if (at(dr) == Fill.Air) {
            return simulateSand(lastAir + (1 to 1))
        }

        // settled
        coords[lastAir] = Fill.Sand
        return true
    }

    override fun toString(): String {
        return (topLeft.y()..bottomRight.y()).joinToString("\n") { y ->
            (topLeft.x()..bottomRight.x()).joinToString("") { at(y to it).toString() }
        }
    }


}

data class RockPath(val pts: List<Coord>) {

    val path: Set<Coord>
        get() {
            val result = mutableSetOf<Coord>()
            for (i in 0 until pts.size - 1) {
                val start = pts[i]
                val end = pts[i + 1]
                result.addAll(start.pathTo(end))
            }
            return result
        }

    companion object {
        fun parse(line: String): RockPath {
            return RockPath(line.split("->")
                .map { it.trim() }
                .map {
                    val (x, y) = it.split(",").map { it.toInt() }
                    y to x
                })
        }
    }
}