package day17

import org.junit.jupiter.api.Test
import readResourceAsBufferedReader
import kotlin.test.assertEquals

class Day17Test {

    @Test
    fun testPart1() {
        val input = """
            >>><<><>><<<>><>>><<<>>><<<><<<>><>><<>>
        """.trimIndent()
            .lines()

        assertEquals(3068L, part1(input.first(), 2022))
    }

    @Test
    fun testPart1Input() {
        val input = readResourceAsBufferedReader("17_1.txt").readLine()

        assertEquals(3102, part1(input, 2022L))
    }

    @Test
    fun testPart2() {
        val input = """
            >>><<><>><<<>><>>><<<>>><<<><<<>><>><<>>
        """.trimIndent()
            .lines()

        assertEquals(1514285714288, part2(input.first(), 1000000000000L))
    }



}