package day09

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day09Test {

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
            30373
            25512
            65332
            33549
            35390
        """.trimIndent()
            .lines()

        assertEquals(8, part2(input))
    }

}