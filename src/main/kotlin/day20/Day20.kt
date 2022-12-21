package day20

import readResourceAsBufferedReader

fun main() {
    println("part 1: ${part1(readResourceAsBufferedReader("20_1.txt").readLines())}")
    //println("part 2: ${part2(readResourceAsBufferedReader("20_1.txt").readLines())}")
}

fun part1(input: List<String>): Int {
    val nums = input.map { it.toInt() }
    val encryptedFile = EncryptedFile(nums)
    val coords = encryptedFile.coordinates()
    return coords.sum()
}

fun part2(input: List<String>): Int {
    TODO()
}

class EncryptedFile(private val contents: List<Int>) {

    fun mix(steps: Int = contents.size): List<Int> {
        var initial = contents
        for (i in 0 until steps) {
            initial = step(i, initial)
        }
        return initial
    }

    fun step(step: Int, state: List<Int>): List<Int> {
        val selected = contents[step]
        val index = state.indexOf(selected)

        var newIndex = indexAfterOffset(index, selected)
        if (newIndex < index) {
            newIndex++
        }

        val result = state.toMutableList()
        if (index != newIndex) {
            result.removeAt(index)
            result.add(newIndex, selected)
        }

        return result
    }

    private fun indexAfterOffset(start: Int, offset: Int): Int {
        var normalizedOffset = offset % contents.size
        if (normalizedOffset < 0) {
            normalizedOffset = contents.size + normalizedOffset - 1
        }
        var newIndex = start + normalizedOffset
        if (newIndex >= contents.size) {
            newIndex = newIndex % contents.size
        }
        return newIndex
    }

    fun coordinates(): List<Int> {
        val file = mix()
        val zeroIndex = file.indexOf(0)
        return listOf(1000, 2000, 3000).map { indexAfterOffset(zeroIndex, it) }
            .map { file[it] }
    }

}

