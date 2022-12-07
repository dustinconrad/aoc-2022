package day07

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day07Test {

    @Test
    fun testPart1() {
        val input = """
                [D]    
            [N] [C]    
            [Z] [M] [P]
             1   2   3 
            
            move 1 from 2 to 1
            move 3 from 1 to 3
            move 2 from 2 to 1
            move 1 from 1 to 2
        """.trimIndent()
            .lines()

        assertEquals(1, part1(input))
    }

    @Test
    fun testPart2() {
        val input = """
                [D]    
            [N] [C]    
            [Z] [M] [P]
             1   2   3 
            
            move 1 from 2 to 1
            move 3 from 1 to 3
            move 2 from 2 to 1
            move 1 from 1 to 2
        """.trimIndent()
            .lines()

        assertEquals(1, part2(input))
    }

}