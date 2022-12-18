package day16

import combinations
import day17.Shape
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

data class OpenValve(val node: Node): Action {
    override fun apply(state: SearchState): SearchState {
        val result = state.copy(
            opened = state.opened + node.name
        )
        result.score += node.rate * state.minute
        return result
    }
}

data class Move(val from: Node, val to: String): Action {
    override fun apply(state: SearchState): SearchState {
        return state.copy(
            currNodes = state.currNodes.map { if (it == from.name) to else it}.sorted()
        )
    }
}

data class SearchState(
    val currNodes: List<String>,
    val opened: Set<String>,
    var minute: Int
) {
    var score = 0
}

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
        return dfs(initial)
    }

    private fun dfs(state: SearchState): Int {
        if (cache.containsKey(state)) {
            return cache[state]!!
        }
        if (state.minute <= 0) {
            return 0
        }
        val actions = state.currNodes.map { actions(state, it) }
        val actionCombinations = combinations(actions)
        state.minute--
        val nextStates = actionCombinations
            .map { actCombo -> actCombo.foldRight(state) { act, acc -> act.apply(acc)} }

        val max = nextStates.maxOfOrNull { it.score + dfs(it) } ?: 0
        state.minute++
        cache[state] = max
        return max
    }

    private fun actions(currState: SearchState, currNodeName: String): List<Action> {
        val possibleActions = mutableListOf<Action>()
        val currNode = graph[currNodeName]!!
        if (!currState.opened.contains(currNodeName) && currNode.rate > 0) {
            possibleActions.add(OpenValve(currNode))
        }
        currNode.neighbors.forEach {
            possibleActions.add(Move(currNode, it))
        }
        return possibleActions
    }

}