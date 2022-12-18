package day17

import geometry.Coord
import geometry.Lcoord
import geometry.cplus
import geometry.lplus
import geometry.x
import geometry.y
import geometry.plus
import readResourceAsBufferedReader
import kotlin.math.absoluteValue
import kotlin.math.min

fun main() {
    println("part 1: ${part1(readResourceAsBufferedReader("17_1.txt").readLine())}")
    println("part 2: ${part2(readResourceAsBufferedReader("17_1.txt").readLine())}")
}

fun part1(input: String, blocks: Long = 2022): Long {
    val jetStream = parseJetStream(input)
    val tetris = Tetris(jetStream)
    return tetris.simulate(blocks)
}

fun part2(input: String, blocks: Long = 1000000000000): Long {
    val jetStream = parseJetStream(input)
    val tetris = Tetris(jetStream)
    return tetris.simulate(blocks)
}

fun parseJetStream(line: String): List<Coord> {
    return line.map { if (it == '>') 0 to 1 else 0 to -1 }
}

private fun createMoves(jetStream: List<Coord>): List<Coord> {
    return jetStream.flatMap { listOf(it, 1 to 0) }
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

class TetrisStateGraph {
    private val graph = mutableMapOf<TetrisState, MutableList<TetrisState>>()
    private var lastState: TetrisState? = null
    private val shapes: ArrayDeque<Pair<Shape, Coord>> = ArrayDeque()


    fun add(shape: Pair<Shape, Coord>) {
        shapes.add(shape)
        if (shapes.size > 6) {
            shapes.removeFirst()
        }
        val state = TetrisState(shapes.toList())
        add(state)
    }

    fun add(state: TetrisState) {
        if(lastState != null) {
            val neighbors = graph.getOrPut(lastState!!) { mutableListOf() }
            neighbors.add(state)
        }
        lastState = state
    }

    fun cycle(): Int {
        return cycle(lastState!!, emptySet())
    }

    private fun cycle(curr: TetrisState, visited: Set<TetrisState>): Int {
        return when {
            visited.contains(curr) -> {
                visited.size
            }
            graph[curr].isNullOrEmpty() -> 0
            else -> {
                val neighbors = graph[curr]
                neighbors?.maxOfOrNull { cycle(it, visited + curr) } ?: 0
            }
        }
    }

}

data class TetrisState(
    val shapes: List<Pair<Shape,Coord>>
)

class Tetris(jetStream: List<Coord>) {
    private val moves = createMoves(jetStream)
    private val movesIter = moves.cycle().iterator()
    private val shapeIter = shapeSequence().iterator()
    private val graph = TetrisStateGraph()

    private val occupied = (0..7L).map { 0L to it }.toMutableSet()
    private var minY = 0L

    fun tallest(): Long = minY

    fun simulate(blockCount: Long): Long {
        return fullSimulation(blockCount)
    }

    private fun fullSimulation(blockCount: Long): Long {
        val history = mutableListOf<Long>()
        var cycleLength: Int = 0
        for (i in 0 until blockCount) {
            val result = simulateNextShape()
            history.add(tallest())
            graph.add(result)
            cycleLength = graph.cycle()
            if (cycleLength != 0) {
                break
            }
//            println("$i: $result")
//            val s = this.toString()
//            val print = s.substring(0, min(s.length, 112))
//            println(print)
//            println()
        }
        val endIdx = history.size - 1
        val startIdx = endIdx - cycleLength
        val cycleStartHeight = history[startIdx].absoluteValue
        val cycleEndHeight = history[endIdx].absoluteValue
        val cycleHeight = cycleEndHeight - cycleStartHeight
        val cyclesInBlockCount = (blockCount - startIdx) / cycleLength
        val height = cycleHeight * cyclesInBlockCount + cycleStartHeight
        val simulatedBlocks = startIdx + cyclesInBlockCount * cycleLength

        val remainingBlocks = (blockCount - simulatedBlocks).toInt()
        val remainingBlocksHeight = history[startIdx + remainingBlocks - 1].absoluteValue - cycleStartHeight

        return height + remainingBlocksHeight
    }

    private fun simulateNextShape(): Pair<Shape, Coord> {
        val shape = shapeIter.next()
        val start = (minY - 4) to 2L
        val s = shape.parts.lMove(start)
        val relativePos = fall(s)
        return shape to relativePos
    }

    private fun fall(shape: Set<Lcoord>): Coord {
        val jet = movesIter.next()
        val isDown = jet == 1 to 0
        val jetMove = shape.move(jet)

        val wouldConflict = jetMove.any { it.x() < 0 || it.x() > 6 || occupied.contains(it) }

        return when {
            wouldConflict && isDown -> {
                minY = min(minY, shape.minOf { it.y() })
                occupied.addAll(shape)
                return 0 to 0
            }
            wouldConflict -> fall(shape)
            else -> jet + fall(jetMove)
        }
    }

    override fun toString(): String {
        return (minY..0).joinToString("\n") { y ->
            (0..6L).joinToString("") { if (occupied.contains(y to it)) "#" else "." }
        }
    }
}



