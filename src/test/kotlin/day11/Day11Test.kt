package day11

import byEmptyLines
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day11Test {

    @Test
    fun testPart1() {
        val input = "Monkey 0:\n" +
                "  Starting items: 79, 98\n" +
                "  Operation: new = old * 19\n" +
                "  Test: divisible by 23\n" +
                "    If true: throw to monkey 2\n" +
                "    If false: throw to monkey 3\n" +
                "\n" +
                "Monkey 1:\n" +
                "  Starting items: 54, 65, 75, 74\n" +
                "  Operation: new = old + 6\n" +
                "  Test: divisible by 19\n" +
                "    If true: throw to monkey 2\n" +
                "    If false: throw to monkey 0\n" +
                "\n" +
                "Monkey 2:\n" +
                "  Starting items: 79, 60, 97\n" +
                "  Operation: new = old * old\n" +
                "  Test: divisible by 13\n" +
                "    If true: throw to monkey 1\n" +
                "    If false: throw to monkey 3\n" +
                "\n" +
                "Monkey 3:\n" +
                "  Starting items: 74\n" +
                "  Operation: new = old + 3\n" +
                "  Test: divisible by 17\n" +
                "    If true: throw to monkey 0\n" +
                "    If false: throw to monkey 1"

        val monkeys = input.lines().byEmptyLines().map { parseMonkey(it) }
        val middle = MonkeyInTheMiddle(monkeys)

        middle.round()


        println()
    }

}