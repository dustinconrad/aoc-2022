package day05

import byEmptyLines
import readResourceAsBufferedReader

fun main() {
    println("part 1: ${part1(readResourceAsBufferedReader("5_1.txt").readLines())}")
    println("part 2: ${part2(readResourceAsBufferedReader("5_1.txt").readLines())}")
}

fun part1(input: List<String>): String {
    return solve(input) { stack, instr -> stack.execute1(instr) }
}

fun part2(input: List<String>): String {
    return solve(input) { stack, instr -> stack.execute2(instr) }
}

private fun solve(input: List<String>, stackFn: (SupplyStacks, CraneInstruction) -> Unit): String {
    val (stack, instructions) = parseInput(input)

    instructions.forEach { stackFn.invoke(stack, it) }

    return stack.tops()
}

fun parseInput(input: List<String>): Pair<SupplyStacks, List<CraneInstruction>> {
    val (stacks, instructions) = input.byEmptyLines()

    val stack = parseSupplyStack(stacks.lines())
    val instrs = parseInstructions(instructions.lines())

    return stack to instrs
}

fun parseSupplyStack(input: List<String>): SupplyStacks {
    val base = input.last()
        .chunked(4)

    val stacks = List(base.size) { StringBuilder() }

    for (i in (input.size - 2) downTo 0) {
        val row = input[i]
        val blocks = row.chunked(4)

        blocks.forEachIndexed { idx, block ->
            if (block.isNotBlank()) {
                stacks[idx].append(block[1])
            }
        }
    }

    return SupplyStacks(stacks)
}

fun parseInstructions(input: List<String>): List<CraneInstruction> {
    return input.map { parseInstruction(it) }
}

fun parseInstruction(line: String): CraneInstruction {
    val parts = line.split(" ")
    return CraneInstruction(
        parts[1].toInt(),
        parts[3].toInt(),
        parts[5].toInt()
    )
}

class SupplyStacks(private val stacks: List<StringBuilder>) {

    fun execute1(instr: CraneInstruction) {
        execute(instr) { it.reversed() }
    }

    fun execute2(instr: CraneInstruction) {
        execute(instr) { it }
    }

    private fun execute(instr: CraneInstruction, moveFn: (String) -> String) {
        val src = stacks[instr.src - 1]
        val move = src.substring(src.length - instr.amt)
        src.setLength(src.length - instr.amt)

        val dst = stacks[instr.dst - 1]
        dst.append(moveFn.invoke(move))
    }

    fun tops(): String {
        return stacks.joinToString("") { it.last().toString() }
    }

}

data class CraneInstruction(val amt: Int, val src: Int, val dst: Int)