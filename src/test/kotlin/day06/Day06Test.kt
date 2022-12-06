package day06

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import kotlin.test.assertEquals

class Day06Test {

    @ParameterizedTest(name = "Verify part1({0}) is {1}")
    @MethodSource("part1")
    fun testPart1(input: String, expected: Int) {
        assertEquals(expected, part1(input))
    }

    @ParameterizedTest(name = "Verify part2({0}) is {1}")
    @MethodSource("part2")
    fun testPart2(input: String, expected: Int) {
        assertEquals(expected, part2(input))
    }

    companion object {
        @JvmStatic
        fun part1(): List<Arguments> {
            return listOf(
                Arguments.of("mjqjpqmgbljsphdztnvjfqwrcgsmlb", 7),
                Arguments.of("bvwbjplbgvbhsrlpgdmjqwftvncz", 5),
                Arguments.of("nppdvjthqldpwncqszvftbrmjlhg", 6),
                Arguments.of("nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg", 10),
                Arguments.of("zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw", 11)
            )
        }

        @JvmStatic
        fun part2(): List<Arguments> {
            return listOf(
                Arguments.of("mjqjpqmgbljsphdztnvjfqwrcgsmlb", 19),
                Arguments.of("bvwbjplbgvbhsrlpgdmjqwftvncz", 23),
                Arguments.of("nppdvjthqldpwncqszvftbrmjlhg", 23),
                Arguments.of("nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg", 29),
                Arguments.of("zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw", 26)
            )
        }
    }

}