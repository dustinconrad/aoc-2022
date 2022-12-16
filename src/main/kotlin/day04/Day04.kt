package day04

import geometry.disjoint
import geometry.fullyContains
import geometry.overlaps
import readResourceAsBufferedReader

fun main() {
    println("part 1: ${part1(readResourceAsBufferedReader("4_1.txt").readLines())}")
    println("part 2: ${part2(readResourceAsBufferedReader("4_1.txt").readLines())}")
}

fun part1(input: List<String>): Int {
    return input.map { parse(it) }
        .count { it.fullyContains }
}

fun part2(input: List<String>): Int {
    return input.map { parse(it) }
        .count { it.overlaps }
}

private fun parse(line: String): SectionAssignment {
    val (l, r) = line.split(",")
    val (lf, ll) = l.split("-").map { it.toInt() }
    val (rf, rl) = r.split("-").map { it.toInt() }

    return SectionAssignment(IntRange(lf, ll), IntRange(rf, rl))
}

data class SectionAssignment(val l: IntRange, val r: IntRange) {

    val fullyContains = l.fullyContains(r) || r.fullyContains(l)

    val overlaps = l.overlaps(r)

}