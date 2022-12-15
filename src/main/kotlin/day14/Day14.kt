package day14

import plane.Coord
import plane.pathTo
import plane.plus
import plane.x
import plane.y
import readResourceAsBufferedReader
import java.util.function.Predicate

fun main() {
    println("part 1: ${part1(readResourceAsBufferedReader("14_1.txt").readLines())}")
    println("part 2: ${part2(readResourceAsBufferedReader("14_1.txt").readLines())}")
}

fun part1(input: List<String>): Int {
    val paths = input.map { RockPath.parse(it) }
    val scan = Scan(paths)
    val result = scan.start()
    return result
}

fun part2(input: List<String>): Int {
    val paths = input.map { RockPath.parse(it) }
    val scan = Scan(paths)
    val result = scan.start2()
    return result
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

    fun start2(): Int {
        while(simulateSand2(0 to 500)) {
//            println(this)
//            println()
        }
        return coords.count { it.value == Fill.Sand }
    }

    private fun at(c: Coord): Fill {
        return coords.getOrDefault(c, Fill.Air)
    }

    private fun at2(c: Coord): Fill {
        return when {
            coords.containsKey(c) -> coords[c]!!
            c.y() == bottomRight.y() + 2 -> Fill.Rock
            else -> Fill.Air
        }
    }

    private tailrec fun sandStep(sandSrc: Coord, lookup: (Coord) -> Fill, done: Predicate<Coord>): Boolean {
        if (done.test(sandSrc)) {
            return false
        }
        val next = listOf(1 to 0, 1 to -1, 1 to 1)
            .map { sandSrc + it }
            .firstOrNull { lookup(it) == Fill.Air }

        return if (next == null) {
            (lookup(sandSrc) == Fill.Air).also { coords[sandSrc] = Fill.Sand }
        } else {
            sandStep(next, lookup, done)
        }
    }

    private fun simulateSand(sandSrc: Coord): Boolean {
        return sandStep(sandSrc, this::at) {
            !(topLeft.x()..bottomRight.x()).contains(it.x())
        }
    }

    private fun simulateSand2(sandSrc: Coord): Boolean {
        return sandStep(sandSrc, this::at2) { false }
    }

    override fun toString(): String {
        val minX = coords.keys.minBy { it.x() }.x()
        val minY = minOf(0, coords.keys.minBy { it.y() }.y())

        val maxX = coords.keys.maxBy { it.x() }.x()
        val maxY = coords.keys.maxBy { it.y() }.y()

        return (minY..maxY).joinToString("\n") { y ->
            (minX..maxX).joinToString("") { at2(y to it).toString() }
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