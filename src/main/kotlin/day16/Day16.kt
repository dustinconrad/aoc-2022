package day16

import geometry.Coord
import readResourceAsBufferedReader
import kotlin.math.min

fun main() {
    println("part 1: ${part1(readResourceAsBufferedReader("16_1.txt").readLines())}")
    //println("part 2: ${part2(readResourceAsBufferedReader("15_1.txt").readLines())}")
}

data class Node(val name: String, val rate: Int, val neighbors: Set<String>) {

    companion object {

        private val valve: Regex = Regex("""Valve ([A-Z]{2}) has flow rate=(\d+); tunnels? leads? to valves? ([A-Z\s,]+)""")

        fun parse(line: String): Node {
            val m = valve.matchEntire(line) ?: throw IllegalArgumentException(line)
            val (name, rate, neighbors) = m.destructured
            return Node(
                name,
                rate.toInt(),
                neighbors.split(",").map { it.trim() }.toSet()
            )
        }

    }
}

fun part1(input: List<String>): Int {
    val nodes = input.map { Node.parse(it) }
    val tunnels = Tunnels(nodes)
    val result = tunnels.dfs("AA")
    return result
}
fun part2(input: List<String>, tl: Coord = 0 to 0, br: Coord = 4000000 to 4000000): Long {
    TODO()
}

sealed interface Action {

    fun apply(state: SearchState): SearchState

}

data class OpenValve(val node: String): Action {
    override fun apply(state: SearchState): SearchState {
        return state.copy(
            opened = state.opened + node
        )
    }
}

data class Move(val from: String, val to: String): Action {
    override fun apply(state: SearchState): SearchState {
        return state.copy(
            
        )
    }
}

data class SearchState(
    val currNodes: List<String>,
    val opened: Set<String>,
    val minute: Int
)

class Tunnels(nodes: Collection<Node>) {

    private val graph = mutableMapOf<String, Node>()
    private val cache = mutableMapOf<SearchState, Int>()

    init {
        nodes.forEach { addNode(it) }
    }

    private fun addNode(node: Node) {
        graph[node.name] = node
    }

    fun dfs(currNodeName: String, minute: Int = 30): Int {
        val initial = SearchState(
            listOf(currNodeName),
            emptySet(),
            minute
        )
        return 0
    }

//    fun dfs(state: SearchState): Int {
//        if (cache.containsKey(state)) {
//            return cache[state]!!
//        }
//        if (state.minute <= 0) {
//            return 0
//        }
//        // options are to spend a minute opening the current valve, then visit the children
//        // or skip opening this valve
//
//        var maxSoFar = 0
//        val currNode = graph[currNodeName]!!
//        // open current valve (if unopened)
//        if (!opened.containsKey(currNodeName) && currNode.rate > 0) {
//            opened[currNodeName] = minutes
//            val openCurrentValve = currNode.rate * (--minutes)
//            // and visit children
//            var maxChild = 0
//            for (child in currNode.neighbors) {
//                // travel
//                minutes--
//                val result = dfs(child)
//                // backtrack
//                minutes++
//                maxChild = maxOf(maxChild, result)
//            }
//            maxSoFar = openCurrentValve + maxChild
//            // backtrack and don't open current valve
//            minutes++
//            opened.remove(currNodeName)
//        }
//
//        var maxChild = 0
//        for (child in currNode.neighbors) {
//            // travel
//            minutes--
//            val result = dfs(child)
//            // backtrack
//            minutes++
//            maxChild = maxOf(maxChild, result)
//        }
//        // compute max
//        val max = maxOf(maxSoFar, maxChild)
//        cache[Triple(currNodeName, opened.keys, minutes)] = max
//
//        return max
//    }

}