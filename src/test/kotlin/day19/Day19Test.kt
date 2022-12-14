package day19

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day19Test {

    @Test
    fun testPart1() {
        val input = """
            2,2,2
            1,2,2
            3,2,2
            2,1,2
            2,3,2
            2,2,1
            2,2,3
            2,2,4
            2,2,6
            1,2,5
            3,2,5
            2,1,5
            2,3,5
        """.trimIndent()
            .lines()

        assertEquals(64, part1(input))
    }

    @Test
    fun testPart2() {
        val input = """
            2,2,2
            1,2,2
            3,2,2
            2,1,2
            2,3,2
            2,2,1
            2,2,3
            2,2,4
            2,2,6
            1,2,5
            3,2,5
            2,1,5
            2,3,5
        """.trimIndent()
            .lines()

        assertEquals(58, part2(input))
    }

}