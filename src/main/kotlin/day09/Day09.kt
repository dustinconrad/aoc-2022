package day09

import plane.Coord
import plane.distance
import plane.plus
import readResourceAsBufferedReader
import kotlin.math.sign

fun main() {
    println("part 1: ${part1(readResourceAsBufferedReader("9_1.txt").readLines())}")
    println("part 2: ${part2(readResourceAsBufferedReader("9_1.txt").readLines())}")
}

fun part1(input: List<String>): Int {
    val instructions = input.map { Dir.parse(it) }
    val rp = RopeBridge()

    instructions.forEach { rp.move(it) }

    return rp.tailSeen.size
}

fun part2(input: List<String>): Int {
    val instructions = input.map { Dir.parse(it) }
    val rp = RopeBridge(10)

    instructions.forEach { rp.move(it) }

    return rp.tailSeen.size
}

sealed class Dir(private val vector: Coord) {

    fun move(input: Coord): Coord {
        return input + vector
    }

    abstract fun steps(): List<Dir>

    class Right(private val magnitude: Int) : Dir(0 to magnitude) {
        override fun steps(): List<Dir> {
            return List(magnitude) { Right(1) }
        }
    }

    class Left(private val magnitude: Int) : Dir(0 to -magnitude) {

        override fun steps(): List<Dir> {
            return List(magnitude) { Left(1) }
        }
    }

    class Up(private val magnitude: Int) : Dir(-magnitude to 0) {

        override fun steps(): List<Dir> {
            return List(magnitude) { Up(1) }
        }

    }

    class Down(private val magnitude: Int) : Dir(magnitude to 0) {
        override fun steps(): List<Dir> {
            return List(magnitude) { Down(1) }
        }
    }

    companion object {
        fun parse(line: String): Dir {
            val (d, m) = line.split(" ")
            return when(d) {
                "R" -> Right(m.toInt())
                "L" -> Left(m.toInt())
                "U" -> Up(m.toInt())
                "D" -> Down(m.toInt())
                else -> throw IllegalArgumentException("Invalid line: $line")
            }
        }
    }

}

class RopeBridge(
    size: Int
) {

    private val _tailSeen: MutableSet<Coord> = mutableSetOf(0 to 0)
    private val knots = Array(size) { 0 to 0 }

    val tailSeen: Set<Coord> = _tailSeen

    constructor(): this(2)

    fun move(dir: Dir) {
        for (step in dir.steps()) {
            var newHead = step.move(knots[0])
            knots[0] = newHead
            for (i in 1 until knots.size) {
                val currTail = knots[i]
                val newTail = follow(newHead, currTail)
                if (newTail == currTail) {
                    break
                }
                knots[i] = newTail
                newHead = newTail
            }
            _tailSeen.add(knots.last())
        }
    }

    private fun follow(head: Coord, tail: Coord): Coord {
        val deltaY = head.first - tail.first
        val deltaX = head.second - tail.second
        val dist = (deltaY to deltaX).distance()
        val move = deltaY.sign to deltaX.sign

        return if (dist > 1.5) {
            tail + move
        } else {
            tail
        }
    }

}

