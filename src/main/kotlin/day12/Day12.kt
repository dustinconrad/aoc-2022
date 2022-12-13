package day12

import plane.Coord
import plane.plus
import plane.x
import plane.y
import readResourceAsBufferedReader
import java.util.function.Predicate

fun main() {
    println("part 1: ${part1(readResourceAsBufferedReader("12_1.txt").readLines())}")
    println("part 2: ${part2(readResourceAsBufferedReader("12_1.txt").readLines())}")
}

fun part1(input: List<String>): Int {
    val start = input.find('S')
    val end = Predicate<Char> { 'E' == it }

    return bfs(input, start, end) { curr, neighbor -> neighbor.code - curr.code <= 1}
}

fun part2(input: List<String>): Int {
    val start = input.find('E')
    val end = Predicate<Char> { 'a' == it || 'S' == it }

    return bfs(input, start, end) { curr, neighbor -> curr.code - neighbor.code <= 1}
}

typealias Hills = List<String>
fun Hills.elevation(c: Coord): Char {
    return when(val char = this[c.y()][c.x()]) {
        'S' -> 'a'
        'E' -> 'z'
        else -> char
    }
}

fun Array<IntArray>.at(c: Coord): Int = this[c.y()][c.x()]

fun Hills.neighbors(c: Coord): List<Coord> {
    return listOf(
        1 to 0, -1 to 0, 0 to 1, 0 to -1
    ).map(c::plus)
        .filter { this.indices.contains(it.y()) && this[c.y()].indices.contains(it.x()) }
}

fun Hills.find(c: Char): Coord {
    return this.mapIndexed { index, s -> index to s.indexOf(c) }
        .first { it.x() != -1 }
}

fun bfs(hills: Hills, start: Coord, end: Predicate<Char>, nFn: (Char, Char) -> Boolean): Int {
    val shortestPaths = Array(hills.size) { IntArray(hills[0].length) }

    shortestPaths[start.y()][start.x()] = 0
    val seen = mutableSetOf<Coord>()

    val q = ArrayDeque<Coord>()
    q.add(start)

    while(q.isNotEmpty()) {
        val curr = q.removeFirst()
        if (end.test(hills[curr.y()][curr.x()])) {
            return shortestPaths.at(curr)
        }
        if (!seen.add(curr)) {
            continue
        }

        val currShortest = shortestPaths.at(curr)

        val neighbors = hills.neighbors(curr)
            .filterNot { seen.contains(it) }
            .filter { nFn.invoke(hills.elevation(curr), hills.elevation(it)) }

        neighbors.forEach {
            shortestPaths[it.y()][it.x()] = currShortest + 1
        }

        q.addAll(neighbors)
    }

    throw IllegalArgumentException("No end")
}