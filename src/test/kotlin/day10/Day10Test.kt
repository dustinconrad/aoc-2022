package day10

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day10Test {

    @Test
    fun testPart1() {
        val input = """
            R 4
            U 4
            L 3
            D 1
            R 4
            D 1
            L 5
            R 2
        """.trimIndent()
            .lines()

        assertEquals(13, part1(input))
    }

    @Test
    fun testPart2() {
        val input = """
            R 4
            U 4
            L 3
            D 1
            R 4
            D 1
            L 5
            R 2
        """.trimIndent()
            .lines()

        assertEquals(1, part2(input))
    }

    @Test
    fun testPart2part2() {
        val input = """
            R 5
            U 8
            L 8
            D 3
            R 17
            D 10
            L 25
            U 20
        """.trimIndent()
            .lines()

        assertEquals(36, part2(input))
    }

}