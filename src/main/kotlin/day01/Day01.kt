package day01

import byEmptyLines
import readResourceAsBufferedReader
import java.util.PriorityQueue

fun main() {
    println("part 1: ${part1(readResourceAsBufferedReader("1_1.txt").readLines())}")
    println("part 2: ${part2(readResourceAsBufferedReader("1_1.txt").readLines())}")
}

fun parseElf(elf: String): Elf {
    return Elf(elf.lines().map { it.toInt() })
}

fun part1(input: List<String>): Int {
    return input.byEmptyLines()
        .map { parseElf(it) }
        .topN(1, Comparator.comparing { it.calories })
        .sumOf { it.calories }
}

fun part2(input: List<String>): Int {
    return input.byEmptyLines()
        .map { parseElf(it) }
        .topN(3, Comparator.comparing { it.calories })
        .sumOf { it.calories }
}

fun <T> Collection<T>.topN(n: Int, comp: Comparator<T>): Collection<T> {
    check(n > 0) { "$n must be greater than 0" }
    val minHeap = PriorityQueue(comp)

    for (item in this) {
        if (minHeap.size < n) {
            minHeap.add(item)
        } else {
            val min = minHeap.peek()!!
            if (comp.compare(item, min) > 0) {
                minHeap.remove()
                minHeap.offer(item)
            }
        }
    }

    return minHeap
}

data class Elf(
    val food: List<Int>
) {
    val calories = food.sum()
}