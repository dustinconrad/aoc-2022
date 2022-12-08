package day08

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day08Test {

    @Test
    fun testPart1() {
        val input = """
            30373
            25512
            65332
            33549
            35390
        """.trimIndent()
            .lines()

        assertEquals(21, part1(input))
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