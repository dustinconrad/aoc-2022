package day11

import byEmptyLines
import readResourceAsBufferedReader
import java.math.BigInteger
import java.util.function.BinaryOperator
import java.util.function.UnaryOperator

fun main() {
    println("part 1: ${part1(readResourceAsBufferedReader("11_1.txt").readLines())}")
    println("part 2: ${part2(readResourceAsBufferedReader("11_1.txt").readLines())}")
}

fun part1(input: List<String>): Long {
    val monkeys = input.byEmptyLines().map { parseMonkey(it) }
    val middle = MonkeyInTheMiddle(monkeys)
    repeat(20) { middle.round() }

    val sorted = middle.monkeys.map { it.inspectCount.toLong() }.sortedDescending()
    return sorted[0] * sorted[1]
}

fun part2(input: List<String>): Long {
    val monkeys = input.byEmptyLines().map { parseMonkey(it) }
    val middle = MonkeyInTheMiddle(monkeys, part2Worry(monkeys))
    repeat(10_000) { middle.round() }

    val sorted = middle.monkeys.map { it.inspectCount.toLong() }.sortedDescending()
    return sorted[0] * sorted[1]
}

fun part2Worry(monkeys: List<Monkey>): UnaryOperator<BigInteger> {
    val product = monkeys.map { it.tst }.reduce(BigInteger::times)
    return UnaryOperator{ t -> t % product }
}

data class Operation(val l: String, val op: BinaryOperator<BigInteger>, val r: String) {

    fun call(worry: BigInteger): BigInteger {
        val left = if (l == "old") {
            worry
        } else {
            l.toBigInteger()
        }

        val right = if (r == "old") {
            worry
        } else {
            r.toBigInteger()
        }

        return op.apply(left, right)
    }

    companion object {
        fun parse(line: String): Operation {
            val parts = line.trim().split(Regex("\\s+"))
            val l = parts[3]
            val r = parts[5]
            val op: (BigInteger, BigInteger) -> BigInteger = when(parts[4]) {
                "*" -> { a, b -> a.times(b) }
                "+" -> { a, b -> a.plus(b)}
                "-" -> { a, b -> a.minus(b) }
                else -> throw IllegalArgumentException("Unknown op: ${parts[4]}")
            }
            return Operation(l, op, r)
        }
    }
}

fun parseMonkey(monkey: String): Monkey {
    val lines = monkey.lines()
    val id = lines[0].trim().replace(":","", false).split(Regex("\\s+"))[1].toInt()
    val start = lines[1].split(":")[1].split(",").map { it.trim() }.map { it.toBigInteger() }.toMutableList()
    val op = Operation.parse(lines[2])
    val test = lines[3].trim().split(Regex("\\s+")).last().toBigInteger()
    val tr = lines[4].trim().split(Regex("\\s+")).last().toInt()
    val fls = lines[5].trim().split(Regex("\\s+")).last().toInt()

    return Monkey(id, start, op, test, tr, fls)
}

class Monkey(
    val id: Int,
    val items: MutableList<BigInteger>,
    val op: Operation,
    val tst: BigInteger,
    val tBranch: Int,
    val fBranch: Int,
) {

    var inspectCount = 0

    fun turn(worryFn: UnaryOperator<BigInteger>): Map<Int, List<BigInteger>> {
        inspectCount += items.size

        val result = items.map {
            var newWorry = op.call(it)
            newWorry = worryFn.apply(newWorry)
            if (newWorry.mod(tst).compareTo(BigInteger.ZERO) == 0) {
                tBranch to newWorry
            } else {
                fBranch to newWorry
            }
        }.groupBy({p -> p.first}, { p -> p.second })
        items.clear()
        return result
    }

    override fun toString(): String {
        return "Monkey{$items}"
    }
}

class MonkeyInTheMiddle(
    val monkeys: List<Monkey>,
    val worryFn: UnaryOperator<BigInteger> = UnaryOperator { t -> t.divide(BigInteger.valueOf(3L)) }
) {

    fun state(): List<Int> {
        return monkeys.map { it.inspectCount }
    }

    fun round() {
        monkeys.forEach {
            val turnResult = it.turn(worryFn)
            turnResult.forEach { (m, w) ->
                monkeys[m].items.addAll(w)
            }
        }
    }

}