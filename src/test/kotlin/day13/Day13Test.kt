package day13

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day13Test {

    @Test
    fun testPart1() {
        val input = """
            [1,1,3,1,1]
            [1,1,5,1,1]

            [[1],[2,3,4]]
            [[1],4]

            [9]
            [[8,7,6]]

            [[4,4],4,4]
            [[4,4],4,4,4]

            [7,7,7,7]
            [7,7,7]

            []
            [3]

            [[[]]]
            [[]]

            [1,[2,[3,[4,[5,6,7]]]],8,9]
            [1,[2,[3,[4,[5,6,0]]]],8,9]
        """.trimIndent()
            .lines()

        assertEquals(13, part1(input))
    }


    @Test
    fun testPart1_2() {
        val input = """
            [[10],[],[6,[],[]],[],[2,0,5]]
            [[6,1,[8,6,9]]]
        """.trimIndent()
            .lines()

        assertEquals(0, part1(input))
    }


    @Test
    fun testPart2() {
        val input = """
            [1,1,3,1,1]
            [1,1,5,1,1]

            [[1],[2,3,4]]
            [[1],4]

            [9]
            [[8,7,6]]

            [[4,4],4,4]
            [[4,4],4,4,4]

            [7,7,7,7]
            [7,7,7]

            []
            [3]

            [[[]]]
            [[]]

            [1,[2,[3,[4,[5,6,7]]]],8,9]
            [1,[2,[3,[4,[5,6,0]]]],8,9]
        """.trimIndent()
            .lines()

        assertEquals(140, part2(input))
    }

}