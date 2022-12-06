package day06

import readResourceAsBufferedReader

fun main() {
    println("part 1: ${part1(readResourceAsBufferedReader("6_1.txt").readLine())}")
    println("part 2: ${part2(readResourceAsBufferedReader("6_1.txt").readLine())}")
}

fun part1(input: String): Int {
    return packetize(4, input).first()
}

fun part2(input: String): Int {
    return packetize(14, input).first()
}

fun packetize(unique: Int, input: String): Sequence<Int> = sequence {
    var l = 0
    var r = 0

    while (r < input.length) {
        val uniqueChars = mutableSetOf<Char>()

        while (uniqueChars.size < unique && r < input.length) {
            val test = input[r]
            if (!uniqueChars.contains(test)) {
                uniqueChars.add(test)
                r++
            } else {
                while (l < r) {
                    if (input[l] != test) {
                        uniqueChars.remove(input[l])
                        l++
                    } else {
                        l++
                        break
                    }
                }
                r++
            }
        }
        yield(r)
        l = r
    }
}

