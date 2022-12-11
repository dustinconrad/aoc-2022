package day11

import byEmptyLines
import readResourceAsBufferedReader
import java.util.function.BinaryOperator

fun main() {
    println("part 1: ${part1(readResourceAsBufferedReader("11_1.txt").readLines())}")
    //println("part 2: \n${part2(readResourceAsBufferedReader("10_1.txt").readLines())}")
}

fun part1(input: List<String>): Int {
    val monkeys = input.byEmptyLines().map { parseMonkey(it) }
    val middle = MonkeyInTheMiddle(monkeys)
    repeat(20) { middle.round() }

    val sorted = middle.monkeys.map { it.inspectCount }.sortedDescending()
    println(sorted)
    return sorted[0] * sorted[1]
}

fun part2(input: List<String>): String {
    TODO()
}

data class Operation(val l: String, val op: BinaryOperator<Int>, val r: String) {

    fun call(worry: Int): Int {
        val left = if (l == "old") {
            worry
        } else {
            l.toInt()
        }

        val right = if (r == "old") {
            worry
        } else {
            r.toInt()
        }

        return op.apply(left, right)
    }

    companion object {
        fun parse(line: String): Operation {
            val parts = line.trim().split(Regex("\\s+"))
            val l = parts[3]
            val r = parts[5]
            val op: (Int, Int) -> Int = when(parts[4]) {
                "*" -> { a, b -> a * b }
                "+" -> { a, b -> a + b }
                "-" -> { a, b -> a - b }
                else -> throw IllegalArgumentException("Unknown op: ${parts[4]}")
            }
            return Operation(l, op, r)
        }
    }
}

fun parseMonkey(monkey: String): Monkey {
    val lines = monkey.lines()
    val id = lines[0].trim().replace(":","", false).split(Regex("\\s+"))[1].toInt()
    val start = lines[1].split(":")[1].split(",").map { it.trim() }.map { it.toInt() }.toMutableList()
    val op = Operation.parse(lines[2])
    val test = lines[3].trim().split(Regex("\\s+")).last().toInt()
    val tr = lines[4].trim().split(Regex("\\s+")).last().toInt()
    val fls = lines[5].trim().split(Regex("\\s+")).last().toInt()

    return Monkey(id, start, op, test, tr, fls)
}

class Monkey(
    val id: Int,
    val items: MutableList<Int>,
    val op: Operation,
    val tst: Int,
    val tBranch: Int,
    val fBranch: Int
) {

    var inspectCount = 0

    fun turn(): Map<Int, List<Int>> {
        inspectCount += items.size

        val result = items.map {
            var newWorry = op.call(it) / 3
            if (newWorry % tst == 0) {
                tBranch to newWorry
            } else {
                fBranch to newWorry
            }
        }.groupBy({p -> p.first}, { p -> p.second })
        items.clear()
        return result;
    }

    override fun toString(): String {
        return "Monkey{$items}"
    }
}

class MonkeyInTheMiddle(val monkeys: List<Monkey>) {

    fun round() {
        monkeys.forEach {
            val turnResult = it.turn()
            turnResult.forEach { (m, w) ->
                monkeys[m].items.addAll(w)
            }
        }
    }

}