package day09

import readResourceAsBufferedReader
import kotlin.math.absoluteValue
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

typealias Coord = Pair<Int, Int>

sealed class Dir(val magnitude: Int) {

    abstract fun move(input: Coord): Coord

    abstract fun steps(): List<Dir>

    class Right(magnitude: Int) : Dir(magnitude) {
        override fun move(input: Coord): Coord {
            return input.first to input.second + magnitude
        }

        override fun steps(): List<Dir> {
            return List(magnitude) { Right(1) }
        }
    }

    class Left(magnitude: Int) : Dir(magnitude) {
        override fun move(input: Coord): Coord {
            return input.first to input.second - magnitude
        }

        override fun steps(): List<Dir> {
            return List(magnitude) { Left(1) }
        }
    }

    class Up(magnitude: Int) : Dir(magnitude) {
        override fun move(input: Coord): Coord {
            return input.first - magnitude to input.second
        }

        override fun steps(): List<Dir> {
            return List(magnitude) { Up(1) }
        }

    }

    class Down(magnitude: Int) : Dir(magnitude) {
        override fun move(input: Coord): Coord {
            return input.first + magnitude to input.second
        }

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

        return when {
            deltaY.absoluteValue == 1 && deltaX.absoluteValue == 1 -> tail
            deltaY.absoluteValue >= 1 && deltaX.absoluteValue >= 1 -> tail.first + deltaY.sign to tail.second + deltaX.sign
            deltaY.absoluteValue > 1 -> tail.first + deltaY.sign to tail.second
            deltaX.absoluteValue > 1 -> tail.first to tail.second + deltaX.sign
            else -> tail
        }
    }

}

