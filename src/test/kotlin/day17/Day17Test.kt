package day17

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day17Test {

    @Test
    fun testPart1() {
        val input = """
            >>><<><>><<<>><>>><<<>>><<<><<<>><>><<>>
        """.trimIndent()
            .lines()

        assertEquals(-3068L, part1(input.first()))
    }

    @Test
    fun testPart2() {
        val input = """
            >>><<><>><<<>><>>><<<>>><<<><<<>><>><<>>
        """.trimIndent()
            .lines()

        assertEquals(-1514285714288, part1(input.first(), 1000000000000L))
    }



}