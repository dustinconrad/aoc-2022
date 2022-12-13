package day13

import byEmptyLines
import readResourceAsBufferedReader

fun main() {
    println("part 1: ${part1(readResourceAsBufferedReader("13_1.txt").readLines())}")
    println("part 2: ${part2(readResourceAsBufferedReader("13_1.txt").readLines())}")
}

fun part1(input: List<String>): Int {
    val pairs = input.byEmptyLines()
        .map { val (l,r) = it.split("\n")
            PacketPair(parsePacket(l), parsePacket(r))
        }


    val inOrder = pairs.mapIndexed { index, packetPair -> index + 1 to packetPair.inOrder() }
        .filter { it.second }

    return inOrder.sumOf { it.first }
}

fun part2(input: List<String>): Int {
    val packets = input.byEmptyLines()
        .flatMap { val (l,r) = it.split("\n")
            listOf(parsePacket(l), parsePacket(r))
        }.toMutableList()

    val p1 = PacketList(listOf(PacketInt(2)))
    val p2 = PacketList(listOf(PacketInt(6)))
    packets.add(p1)
    packets.add(p2)

    packets.sort()

    val i1 = packets.indexOf(p1)
    val i2 = packets.indexOf(p2)

    return (i1 + 1) * (i2 + 1)
}

sealed class PacketData: Comparable<PacketData> {
}

object PacketListStart: PacketData() {
    override fun compareTo(other: PacketData): Int {
        TODO("Not yet implemented")
    }
}

data class PacketList(val data: List<PacketData>): PacketData() {
    override fun compareTo(other: PacketData): Int {
        val result = when (other){
            is PacketInt -> this.compareTo(PacketList(listOf(other)))
            is PacketList -> {
                for (i in 0 until minOf(data.size, other.data.size)) {
                    val l = data[i]
                    val r = other.data[i]
                    val c = l.compareTo(r)
                    if (c != 0) {
                        //println("$this ? $other -> $c")
                        return c
                    }
                }
                data.size.compareTo(other.data.size)
            }
            else -> throw IllegalArgumentException()
        }
        //println("$this ? $other -> $result")
        return result
    }

}

data class PacketInt(val data: Int): PacketData() {
    override fun compareTo(other: PacketData): Int {
        val result =  when (other) {
            is PacketInt -> data.compareTo(other.data)
            is PacketList -> PacketList(listOf(this)).compareTo(other)
            else -> throw IllegalArgumentException()
        }
        //println("$this ? $other -> $result")
        return result
    }
}

fun parsePacket(line: String): PacketList {
    val stack = ArrayDeque<PacketData>()

    val chars = StringBuilder()

    fun processChars() {
        if (chars.isNotEmpty()) {
            val n = chars.toString().toInt()
            chars.clear()
            stack.add(PacketInt(n))
        }
    }

    line.forEach {
        when(it) {
            '[' -> {
                stack.add(PacketListStart)
                processChars()
            }
            ']' -> {
                processChars()
                val parts = mutableListOf<PacketData>()
                while (stack.last() != PacketListStart) {
                    parts.add(stack.removeLast())
                }
                stack.removeLast()
                stack.add(PacketList(parts.asReversed().toList()))
            }
            ',' -> processChars()
            else -> {
                chars.append(it)
            }
        }
    }

    return stack.first() as PacketList
}

data class PacketPair(val left: PacketList, val right: PacketList) {

    fun inOrder(): Boolean {
        val result = left <= right
//        println("$left <= $right: $result")
//        println()
        return result
    }

}
