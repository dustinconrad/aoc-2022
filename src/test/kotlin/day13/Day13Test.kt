package day13

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day13Test {

    @Test
    fun testPart1() {
        val input = """
            Sabqponm
            abcryxxl
            accszExk
            acctuvwj
            abdefghi
        """.trimIndent()
            .lines()

        assertEquals(31, part1(input))
    }

    @Test
    fun testPart2() {
        val input = """
            Sabqponm
            abcryxxl
            accszExk
            acctuvwj
            abdefghi
        """.trimIndent()
            .lines()

        assertEquals(29, part2(input))
    }

}