package day02

import readResourceAsBufferedReader

fun main() {
    println("part 1: ${part1(readResourceAsBufferedReader("2_1.txt").readLines())}")
    println("part 2: ${part2(readResourceAsBufferedReader("2_1.txt").readLines())}")
}

fun part1(input: List<String>): Int {
    return input.map { parseRoundPart1(it) }
        .sumOf { it.score }
}


fun part2(input: List<String>): Int {
    return input.map { parseRoundPart2(it) }
        .sumOf { it.score }
}


enum class Throw(val score: Int) {
    Rock(1),
    Paper(2),
    Scissors(3),
}

enum class Outcome(val score: Int) {
    Win(6),
    Draw(3),
    Lose(0),
}

val throwMap = mapOf(
    "A" to Throw.Rock,
    "B" to Throw.Paper,
    "C" to Throw.Scissors,
    "X" to Throw.Rock,
    "Y" to Throw.Paper,
    "Z" to Throw.Scissors
)

val outcomeMap = mapOf(
    "X" to Outcome.Lose,
    "Y" to Outcome.Draw,
    "Z" to Outcome.Win
)

private fun parseRoundPart1(line: String): Round {
    val (l, r) = line.split(" ");
    return Round(throwMap[l]!!, throwMap[r]!!)
}

private fun parseRoundPart2(line: String): Round {
    val (l, r) = line.split(" ");
    return Round(throwMap[l]!!, outcomeMap[r]!!)
}

data class Round(val opp: Throw, private val _me: Throw?, private val _outcome: Outcome?) {

    constructor(opp: Throw, right: Throw): this(opp, right, null)
    constructor(opp: Throw, outcome: Outcome): this(opp, null, outcome)

    val outcome: Outcome = when {
        _outcome != null -> _outcome
        _me == opp -> Outcome.Draw
        _me == Throw.Rock && opp == Throw.Scissors -> Outcome.Win
        _me == Throw.Paper && opp == Throw.Rock -> Outcome.Win
        _me == Throw.Scissors && opp == Throw.Paper -> Outcome.Win
        else -> Outcome.Lose
    }

    val me: Throw = when {
        _me != null -> _me
        _outcome == Outcome.Draw -> opp
        _outcome == Outcome.Win && opp == Throw.Scissors -> Throw.Rock
        _outcome == Outcome.Lose && opp == Throw.Paper -> Throw.Rock
        _outcome == Outcome.Win && opp == Throw.Rock -> Throw.Paper
        _outcome == Outcome.Lose && opp == Throw.Scissors -> Throw.Paper
        else -> Throw.Scissors
    }

    val score = outcome.score + me.score
}