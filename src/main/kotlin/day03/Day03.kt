package day03

import readResourceAsBufferedReader

fun main() {
    println("part 1: ${part1(readResourceAsBufferedReader("3_1.txt").readLines())}")
    println("part 2: ${part2(readResourceAsBufferedReader("3_1.txt").readLines())}")
}

fun part1(input: List<String>): Int {
    return input.map { parsePart1(it) }
        .flatMap { it.overlap }
        .sumOf { it.priority() }
}

fun part2(input: List<String>): Int {
    return input.chunked(3)
        .map { Grouping(it) }
        .flatMap { it.overlap }
        .sumOf { it.priority() }
}

fun Char.priority(): Int {
    return if (this.isLowerCase()) {
        this.code - 'a'.code + 1
    } else {
        27 + this.code - 'A'.code
    }
}

private fun parsePart1(input: String): Grouping {
    return Grouping(listOf(
        input.subSequence(0, input.length / 2).toString(),
        input.subSequence(input.length / 2, input.length).toString()
    ))
}

data class Grouping(val groups: List<String>) {

    private val asSets = groups.map { it.toSet() }

    val overlap = asSets.reduce { l, r -> l.intersect(r) }
}