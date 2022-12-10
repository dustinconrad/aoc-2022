package day10

import readResourceAsBufferedReader

fun main() {
    println("part 1: ${part1(readResourceAsBufferedReader("10_1.txt").readLines())}")
    println("part 2: \n${part2(readResourceAsBufferedReader("10_1.txt").readLines())}")
}

fun part1(input: List<String>): Int {
    val instrs = input.map { Instruction.parse(it) }
    val signalStrength = signalStrength(instrs)

    return listOf(20, 60, 100, 140, 180, 220)
        .map { signalStrength[it - 1] }
        .sum()
}

fun part2(input: List<String>): String {
    val instrs = input.map { Instruction.parse(it) }
    val spritePos = runResults(instrs)

    return spritePos.mapIndexed { cycle, pos ->
        val cycleX = cycle % 40
        if ((pos - 1..pos + 1).contains(cycleX)) {
            "#"
        } else {
            "."
        }
    }.joinToString("")
        .chunked(40)
        .joinToString("\n")
}

sealed class Instruction(val cycles: Int) {
    abstract fun complete(cpu: Cpu)

    companion object {
        fun parse(line: String): Instruction {
            return if (line == "noop") {
                NoOp
            } else {
                val (_, v) = line.split(" ")
                AddX(v.toInt())
            }
        }
    }
}

data class AddX(val value: Int): Instruction(2) {
    override fun complete(cpu: Cpu) {
        cpu.register += value
    }

}

object NoOp : Instruction(1) {
    override fun complete(cpu: Cpu) {
        //do nothing
    }
}

fun runResults(instrs: List<Instruction>): List<Int> {
    val cpu = Cpu()
    return instrs.flatMap { cpu.step(it) }
}

fun signalStrength(instrs: List<Instruction>): List<Int> {
    val cpu = Cpu()
    return instrs.flatMap { cpu.step(it) }
        .mapIndexed { idx, v -> (idx + 1) * v }
}

class Cpu {

    var register = 1

    fun step(instr: Instruction): List<Int> {
        val currX = register
        instr.complete(this)
        return MutableList(instr.cycles) { currX }
    }

}