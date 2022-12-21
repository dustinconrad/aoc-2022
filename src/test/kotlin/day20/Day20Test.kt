package day20

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day20Test {

    @Test
    fun testPart1_sample() {
        val input = """
            1
            2
            -3
            3
            -2
            0
            4
        """.trimIndent()
            .lines()
            .map { it.toInt() }

        val file = EncryptedFile(input)

        val one = file.mix(1)
        assertEquals(listOf(2, 1, -3, 3, -2, 0, 4), one)

        val two = file.mix(2)
        assertEquals(listOf(1, -3, 2, 3, -2, 0, 4), two)

        val three = file.mix(3)
        assertEquals(listOf(1, 2, 3, -2, -3, 0, 4), three)

        val four = file.mix(4)
        assertEquals(listOf(1, 2, -2, -3, 0, 3, 4), four)

        val five = file.mix(5)
        assertEquals(listOf(1, 2, -3, 0, 3, 4, -2), five)

        val six = file.mix(6)
        assertEquals(listOf(1, 2, -3, 0, 3, 4, -2), six)

        val seven = file.mix(7)
        assertEquals(listOf(1, 2, -3, 4, 0, 3, -2), seven)
    }

    @Test
    fun testPart1_more() {
        val input = """
            0
            -2
            5
            6
            7
            8
            9
        """.trimIndent()
            .lines()
            .map { it.toInt() }

        val file = EncryptedFile(input)

        val two = file.mix(2)
        assertEquals(listOf(0, 5, 6, 7, 8, -2, 9), two)
    }

    @Test
    fun testPart1() {
        val input = """
            1
            2
            -3
            3
            -2
            0
            4
        """.trimIndent()
            .lines()

        assertEquals(3, part1(input))
    }


}