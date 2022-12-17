package day17

import geometry.Coord
import geometry.Lcoord
import geometry.cplus
import geometry.lplus
import geometry.x
import geometry.y
import readResourceAsBufferedReader
import kotlin.math.min
import math.lcm

fun main() {
    println("part 1: ${part1(readResourceAsBufferedReader("17_1.txt").readLine())}")
    //println("part 2: ${part2(readResourceAsBufferedReader("17_1.txt").readLines())}")
}

fun part1(input: String, blocks: Long = 2022): Long {
    val tetris = Tetris(input)
    tetris.simulate(blocks)
    return tetris.tallest()
}

fun createJetStream(line: String) = sequence<Coord> {
    val raw = line.map { if (it == '>') 0 to 1 else 0 to -1 }

    while (true) {
        yieldAll(raw)
    }
}

fun part2(input: List<String>): Long {
    TODO()
}

sealed interface Shape {
    val parts: Set<Lcoord>
}

object HorizontalLine : Shape {
    override val parts = (0..3L).map { 0L to it }.toSet()
}

object Plus : Shape {
    override val parts: Set<Lcoord>
        get() {
            val line = (0..2L).map { -1L to it }.toMutableSet()
            line.add(-2L to 1)
            line.add(0L to 1)
            return line
        }
}

object Elbow: Shape {
    override val parts: Set<Lcoord>
        get() {
            val line = (0..2L).map { 0L to it }.toMutableSet()
            (-2 .. -1L).map { it to 2L }.forEach { line.add(it) }
            return line
        }
}

object VerticalLine: Shape {
    override val parts = (-3..0L).map { it to 0L}.toSet()
}

object Block: Shape {
    override val parts: Set<Lcoord>
        get() {
            val line = (0 .. 1L).map { -1L to it }.toMutableSet()
            (0 .. 1L).map { 0L to it }.forEach { line.add(it) }
            return line
        }
}
fun shapeSequence(): Sequence<Shape> = sequence {
    while(true) {
        yield(HorizontalLine)
        yield(Plus)
        yield(Elbow)
        yield(VerticalLine)
        yield(Block)
    }
}


fun Set<Lcoord>.move(dir: Coord): Set<Lcoord> {
    return this.map { it.cplus(dir) }.toSet()
}

fun Set<Lcoord>.lMove(dir: Lcoord): Set<Lcoord> {
    return this.map { it.lplus(dir) }.toSet()
}

class Tetris(jets: String) {
    private val jetsLength = jets.length
    private val jetsIter = createJetStream(jets).iterator()
    private val shapeIter = shapeSequence().iterator()

    private val occupied = (0..7L).map { 0L to it }.toMutableSet()
    private var minY = 0L

    val lcm: Long = lcm(jetsLength.toLong(), 5L)

    fun tallest(): Long = minY

    fun simulate(blockCount: Long) {
        fullSimulation(blockCount)
    }

    private fun fullSimulation(blockCount: Long) {
        for (i in 0 until blockCount) {
            simulateNextShape()
        }
    }

    private fun simulateNextShape() {
        val shape = shapeIter.next()
        val start = (minY - 4) to 2L
        val s = shape.parts.lMove(start)
        fall(s)
    }

    private tailrec fun fall(shape: Set<Lcoord>) {
        val jet = jetsIter.next()
        val jetMove = shape.move(jet)
        val toMoveDown =  if (jetMove.all { it.x() in 0..6 } && jetMove.none { occupied.contains(it) }) {
            jetMove
        } else {
            shape
        }

        val movedDown = toMoveDown.move(1 to 0)

        if (movedDown.any { occupied.contains(it) }) {
            minY = min(minY, toMoveDown.minOf { it.y() })
            occupied.addAll(toMoveDown)
        } else {
            fall(movedDown)
        }
    }

    override fun toString(): String {
        return (minY..0).joinToString("\n") { y ->
            (0..6L).joinToString("") { if (occupied.contains(y to it)) "#" else "." }
        }
    }
}



