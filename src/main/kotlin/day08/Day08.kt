package day08

import readResourceAsBufferedReader

fun main() {
    println("part 1: ${part1(readResourceAsBufferedReader("8_1.txt").readLines())}")
    println("part 2: ${part2(readResourceAsBufferedReader("8_1.txt").readLines())}")
}

fun part1(input: List<String>): Int {
    val grid = parseGrid(input)
    return visible(grid).size
}

fun part2(input: List<String>): Int {
    val grid = parseGrid(input)
    val scores = scenicScore(grid)
    return scores.maxOfOrNull { it.max() } ?: throw IllegalStateException()
}

fun parseGrid(input: List<String>): Grid {
    return input.map { line -> line.toCharArray().toList().map { it.code - '0'.code } }
}

fun visible(grid: Grid): Set<Pair<Int, Int>> {
    val answer = mutableSetOf<Pair<Int,Int>>()

    for (y in grid.indices) {

        // left to right
        val row = grid[y]
        var tallestSoFar = -1
        for (x in row.indices) {
            if (row[x] > tallestSoFar) {
                answer.add(y to x)
                tallestSoFar = row[x]
            }
        }

        // right to left
        tallestSoFar = -1
        for (x in row.indices.reversed()) {
            if (row[x] > tallestSoFar) {
                answer.add(y to x)
                tallestSoFar = row[x]
            }
        }
    }

    for (x in 0 until grid[0].size) {

        // top to bottom
        var tallestSoFar = -1
        for (y in grid.indices) {
            if (grid[y][x] > tallestSoFar) {
                answer.add(y to x)
                tallestSoFar = grid[y][x]
            }
        }
        // bottom to top
        tallestSoFar = -1
        for (y in grid.indices.reversed()) {
            if (grid[y][x] > tallestSoFar) {
                answer.add(y to x)
                tallestSoFar = grid[y][x]
            }
        }
    }

    return answer
}

typealias Grid = List<List<Int>>

fun scenicScore(grid: Grid): Array<IntArray> {

    fun right(y: Int, x: Int): Int {
        val curr = grid[y][x]
        var answer = 0
        var i = x
        while(i < grid[y].size - 1) {
            answer++
            if (grid[y][++i] >= curr) {
                break
            }
        }
        return answer
    }

    fun left(y: Int, x: Int): Int {
        val curr = grid[y][x]
        var answer = 0
        var i = x
        while(i > 0) {
            answer++
            if (grid[y][--i] >= curr) {
                break
            }
        }
        return answer
    }

    fun down(y: Int, x: Int): Int {
        val curr = grid[y][x]
        var answer = 0
        var i = y
        while(i < grid.size - 1) {
            answer++
            if (grid[++i][x] >= curr) {
                break
            }
        }
        return answer
    }

    fun up(y: Int, x: Int): Int {
        val curr = grid[y][x]
        var answer = 0
        var i = y
        while(i > 0) {
            answer++
            if (grid[--i][x] >= curr) {
                break
            }
        }
        return answer
    }

    val scores = Array(grid.size) { IntArray(grid[0].size) }

    for (y in 1 until grid.size - 1) {
        val row = grid[y]
        for (x in 1 until row.size - 1) {
            val up = up(y, x)
            val left = left(y, x)
            val right = right(y, x)
            val down = down(y, x)

            scores[y][x] = right * left * down * up
        }
    }
    return scores
}