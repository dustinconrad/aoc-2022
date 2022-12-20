package day16

import getCartesianProduct
import readResourceAsBufferedReader

fun main() {
    println("part 1: ${part1(readResourceAsBufferedReader("16_1.txt").readLines())}")
    println("part 2: ${part2(readResourceAsBufferedReader("16_1.txt").readLines())}")
}

data class Node(val name: String, val rate: Int, val neighbors: Map<String, Int>) {

    companion object {

        private val valve: Regex = Regex("""Valve ([A-Z]{2}) has flow rate=(\d+); tunnels? leads? to valves? ([A-Z\s,]+)""")

        fun parse(line: String): Node {
            val m = valve.matchEntire(line) ?: throw IllegalArgumentException(line)
            val (name, rate, neighbors) = m.destructured
            return Node(
                name,
                rate.toInt(),
                neighbors.split(",").map { it.trim() }.associateWith { 1 }
            )
        }

    }
}

fun part1(input: List<String>): Int {
    var nodes = input.map { Node.parse(it) }
    val tunnels = Tunnels(nodes)
    val result = tunnels.dfs("AA")
    return result
}
fun part2(input: List<String>): Int {
    val nodes = input.map { Node.parse(it) }
    val tunnels = Tunnels(nodes)
    val result = tunnels.dfs2(listOf("AA", "AA"))
    return result
}

sealed interface Action {

    fun apply(state: SearchState): Pair<SearchState, Int>

    fun undo(state: SearchState): SearchState

    fun cost(): Int

}

data class OpenValve(val node: Node): Action {
    override fun apply(state: SearchState): Pair<SearchState, Int> {
        return if (!state.opened.contains(node.name)) {
            state.opened.add(node.name)
            state to node.rate * state.minute
        } else {
            state to 0
        }
    }

    override fun undo(state: SearchState): SearchState {
        return if (state.opened.contains(node.name)) {
            state.opened.remove(node.name)
            state
        } else {
            state
        }
    }

    override fun cost(): Int {
        return 1
    }
}

data class Move(val from: Node, val to: String, val dist: Int): Action {
    override fun apply(state: SearchState): Pair<SearchState, Int> {
        val nextNodes = state.currNodes.toMutableList()
        for (i in nextNodes.indices) {
            val node = nextNodes[i]
            if (node == from.name) {
                nextNodes[i] = to
                break
            }
        }
        nextNodes.sort()
        return state.copy(
            currNodes = nextNodes
        ) to 0
    }

    override fun undo(state: SearchState): SearchState {
        val prevNodes = state.currNodes.toMutableList()
        for (i in prevNodes.indices) {
            val node = prevNodes[i]
            if (node == to) {
                prevNodes[i] = from.name
                break
            }
        }
        prevNodes.sort()
        return state.copy(
            currNodes = prevNodes
        )
    }

    override fun cost(): Int {
        return dist
    }
}

data class SearchState(
    val currNodes: MutableList<String>,
    val opened: MutableSet<String>,
    var minute: Int
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
            mutableListOf(currNodeName),
            mutableSetOf(),
            minute
        )
        return dfs(initial)
    }

    fun dfs2(currNodeName: List<String>, minute: Int = 26): Int {
        val initial = SearchState(
            currNodeName.toMutableList(),
            mutableSetOf(),
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
        val actionCombinations = actions.getCartesianProduct()
        state.minute--

        var max = Integer.MIN_VALUE
        actionCombinations.forEach { actions ->
            val (nextState, score) = actions.foldRight(state to 0) { act, (st, sc) ->
                val (newState, newScore) = act.apply(st)
                newState to newScore + sc
            }

            val nextScore = dfs(nextState) + score
            max = maxOf(max, nextScore)

            actions.forEach {
                it.undo(nextState)
            }
        }
        state.minute++
        cache[state] = max
        return max
    }

    private fun actions(currState: SearchState, currNodeName: String): List<Action> {
        val possibleActions = mutableListOf<Action>()
        val currNode = graph[currNodeName] ?: throw IllegalArgumentException("Unknown node name: $currNodeName")
        if (!currState.opened.contains(currNodeName) && currNode.rate > 0) {
            possibleActions.add(OpenValve(currNode))
        }
        currNode.neighbors.forEach {
            possibleActions.add(Move(currNode, it.key, it.value))
        }
        return possibleActions
    }

}


fun shortenAll(nodes: Collection<Node>): List<Node> {
    val nodeLookup = nodes.associateBy { it.name }.toMutableMap()
    val newNodes = mutableListOf<Node>()

    for (node in nodes) {
        if (node.name != "AA" && node.rate == 0) {
            continue
        }
        val newNode = shorten(nodeLookup, node)
        nodeLookup[newNode.name] = node
        newNodes.add(newNode)
    }

    return newNodes
}

fun shorten(nodeLookup: Map<String, Node>, node: Node): Node {
    val newNeighbors = mutableMapOf<String, Int>()
    val visited = mutableSetOf<String>()
    visited.add(node.name)
    val q = ArrayDeque<Pair<String, Int>>()
    q.addAll(node.neighbors.toList())
    while(q.isNotEmpty()) {
        val (candidate, dist) = q.removeFirst()
        if (visited.contains(candidate)) {
            continue
        } else {
            visited.add(candidate)
        }

        val candidateNode = nodeLookup[candidate]!!
        if (candidateNode.rate > 0) {
            newNeighbors[candidate] = dist
        } else {
            q.addAll(candidateNode.neighbors.map { (k, v) -> k to v + dist })
        }
    }

    return node.copy(
        neighbors = newNeighbors
    )
}

