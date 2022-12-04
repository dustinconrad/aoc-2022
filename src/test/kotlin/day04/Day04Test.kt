package day04

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day04Test {

    @Test
    fun testPart1() {
        val input = """
            2-4,6-8
            2-3,4-5
            5-7,7-9
            2-8,3-7
            6-6,4-6
            2-6,4-8
        """.trimIndent()
            .lines()

        assertEquals(2, part1(input))
    }

    @Test
    fun testPart2() {
        val input = """
            2-4,6-8
            2-3,4-5
            5-7,7-9
            2-8,3-7
            6-6,4-6
            2-6,4-8
        """.trimIndent()
            .lines()

        assertEquals(4, part2(input))
    }

}