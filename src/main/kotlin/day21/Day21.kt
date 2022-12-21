package day21

import readResourceAsBufferedReader

fun main() {
    println("part 1: ${part1(readResourceAsBufferedReader("21_1.txt").readLines())}")
    //println("part 2: ${part2(readResourceAsBufferedReader("21_1.txt").readLines())}")
}

fun part1(input: List<String>): Long {
    val nodes = input.map { Node.parse(it) }
    val monkeyGraph = MonkeyGraph(nodes)
    val result = monkeyGraph.process()
    return result
}

fun part2(input: List<String>): Long {
    TODO()
}



sealed interface Node {
    fun evaluate(lookup: Map<String, Long>): Long

    fun ancestors(): Collection<String>

    var inDegree: Int

    val name: String

    companion object {

        private val constRegex = Regex("""[a-z]+:\s\d+""")

        fun parse(line: String): Node {
            val constant = constRegex.matchEntire(line)
            return if (constant != null) {
                val (name, rest) = line.split(":")
                ConstantNode(name, rest.trim().toLong())
            } else {
                val (name, rest) = line.split(":")
                val (left, op, right) = rest.trim().split(" ")
                MonkeyNode(name, left, op, right)
            }
        }
    }
}

data class ConstantNode(override val name: String, val const: Long) : Node {

    override var inDegree = 0

    override fun evaluate(lookup: Map<String, Long>): Long {
        return const
    }

    override fun ancestors() = emptySet<String>()
}

data class MonkeyNode(override val name: String, val left: String, val op: String, val right: String) : Node {

    override var inDegree = 2

    override fun evaluate(lookup: Map<String, Long>): Long {
        require(inDegree == 0) { "inDegree not zero" }
        val leftVal = lookup[left]!!
        val rightVal = lookup[right]!!
        return when (op) {
            "+" -> leftVal + rightVal
            "*" -> leftVal * rightVal
            "-" -> leftVal - rightVal
            "/" -> leftVal / rightVal
            else -> throw IllegalArgumentException("Unknown op $op")
        }
    }

    override fun ancestors(): Set<String> {
        return setOf(left, right)
    }

}

class MonkeyGraph(nodes: Collection<Node>) {

    private val _values: MutableMap<String, Long> = mutableMapOf()
    private val graph = nodes.associateBy { it.name }

    fun process(target: String = "root"): Long {
        val reverseGraph = reverseGraph()

        val zeroIn = graph.values.filter { it.inDegree == 0 }

        val q = ArrayDeque<Node>()
        q.addAll(zeroIn)

        while(q.isNotEmpty()) {
            val curr = q.removeFirst()
            _values[curr.name] = curr.evaluate(_values)

            reverseGraph[curr.name]?.forEach {
                it.inDegree--
                if (it.inDegree == 0) {
                    q.add(it)
                }
            }
        }

        return _values[target]!!
    }

    private fun reverseGraph(): MutableMap<String, MutableList<Node>> {
        val reverseGraph = mutableMapOf<String, MutableList<Node>>()
        graph.forEach { (_, v) ->
            v.ancestors().forEach { ancestor ->
                val siblings = reverseGraph.getOrPut(ancestor) { mutableListOf() }
                siblings.add(v)
            }
        }

        return reverseGraph
    }
}

