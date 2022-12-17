package day17

import geometry.Coord
import geometry.Lcoord
import geometry.cplus
import geometry.lplus
import geometry.x
import geometry.y
import readResourceAsBufferedReader
import kotlin.math.min

fun main() {
    println("part 1: ${part1(readResourceAsBufferedReader("17_1.txt").readLine())}")
    //println("part 2: ${part2(readResourceAsBufferedReader("17_1.txt").readLines())}")
}

fun part1(input: String, blocks: Long = 2022): Long {
    val jetStream = parseJetStream(input)
    val tetris = Tetris(jetStream)
    tetris.simulate(blocks)
    return tetris.tallest()
}

fun parseJetStream(line: String): List<Coord> {
    return line.map { if (it == '>') 0 to 1 else 0 to -1 }
}

private fun createMoves(jetStream: List<Coord>): List<Coord> {
    return jetStream.flatMap { listOf(it, 1 to 0) }
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

fun List<Coord>.cycle() = sequence {
    while(true) {
        yieldAll(this@cycle)
    }
}

class Tetris(jetStream: List<Coord>) {
    private val moves = createMoves(jetStream)
    private val movesIter = moves.cycle().iterator()
    private val shapeIter = shapeSequence().iterator()

    private val occupied = (0..7L).map { 0L to it }.toMutableSet()
    private var minY = 0L

    fun tallest(): Long = minY

    fun simulate(blockCount: Long) {
        fullSimulation(blockCount)
    }

    private fun fullSimulation(blockCount: Long) {
        for (i in 0 until blockCount) {
            simulateNextShape()
//            println()
//            println(this)
        }
    }

    private fun simulateNextShape() {
        val shape = shapeIter.next()
        val start = (minY - 4) to 2L
        val s = shape.parts.lMove(start)
        fall(s)
    }

    private tailrec fun fall(shape: Set<Lcoord>) {
        val jet = movesIter.next()
        val isDown = jet == 1 to 0
        val jetMove = shape.move(jet)

        val wouldConflict = jetMove.any { it.x() < 0 || it.x() > 6 || occupied.contains(it) }

        when {
            wouldConflict && isDown -> {
                minY = min(minY, shape.minOf { it.y() })
                occupied.addAll(shape)
            }
            wouldConflict -> fall(shape)
            else -> fall(jetMove)
        }
    }

    override fun toString(): String {
        return (minY..0).joinToString("\n") { y ->
            (0..6L).joinToString("") { if (occupied.contains(y to it)) "#" else "." }
        }
    }
}



