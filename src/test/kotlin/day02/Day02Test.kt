package day02

import kotlin.test.Test
import kotlin.test.assertEquals

class Day02Test {

    @Test
    fun testPart1() {
        val input = """
            A Y
            B X
            C Z
        """.trimIndent()
            .lines()

        assertEquals(15, part1(input))
    }

    @Test
    fun testPart2() {
        val input = """
            A Y
            B X
            C Z
        """.trimIndent()
            .lines()

        assertEquals(12, part2(input))
    }

}