package day18

import geometry.Coord3d
import geometry.containedBy
import geometry.neighbors
import geometry.parseCoord3d
import geometry.x
import geometry.y
import geometry.z
import readResourceAsBufferedReader

fun main() {
    println("part 1: ${part1(readResourceAsBufferedReader("18_1.txt").readLines())}")
    println("part 2: ${part2(readResourceAsBufferedReader("18_1.txt").readLines())}")
}

fun part1(input: List<String>): Int {
    val coords = input.map { parseCoord3d(it) }
    val droplets = LavaDroplets(coords)
    return droplets.totalSurfaceArea
}

fun part2(input: List<String>): Int {
    val coords = input.map { parseCoord3d(it) }
    val droplets = LavaDroplets(coords)
    return droplets.outerSurfaceArea()
}

class LavaDroplets(droplets: Collection<Coord3d>) {

    private val droplets = droplets.toSet()

    private val min: Coord3d
    private val max: Coord3d

    init {
        var minX = Int.MAX_VALUE
        var maxX = Int.MIN_VALUE
        var minY = Int.MAX_VALUE
        var maxY = Int.MIN_VALUE
        var minZ = Int.MAX_VALUE
        var maxZ = Int.MIN_VALUE
        droplets.forEach {
            minX = minOf(minX, it.x())
            maxX = maxOf(maxX, it.x())
            minY = minOf(minY, it.y())
            maxY = maxOf(maxY, it.y())
            minZ = minOf(minZ, it.z())
            maxZ = maxOf(maxZ, it.z())
        }
        min = Triple(minX, minY, minZ)
        max = Triple(maxX, maxY, maxZ)
    }

    private fun surfaceArea(droplets: Collection<Coord3d>, validate: (Coord3d) -> Boolean): Int {
        val surfaceAreas = droplets.associateWith { 6 }.toMutableMap()
        surfaceAreas.keys.forEach {
            val neighbors = neighbors(it, validate)
            neighbors.forEach { n ->
                surfaceAreas[n] = surfaceAreas[n]!! - 1
            }
        }
        return surfaceAreas.values.sum()
    }

    private fun neighbors(droplet: Coord3d, validate: (Coord3d) -> Boolean): Set<Coord3d> {
        return droplet.neighbors()
            .filter { validate(it) }
            .toSet()
    }

    val totalSurfaceArea = surfaceArea(droplets) { droplets.contains(it) }

    fun outerSurfaceArea(): Int {
        val internalIslands = internalIslands()
        val internalSurfaceAreas =
            internalIslands.associateWith { island -> surfaceArea(island) { !droplets.contains(it) } }
        val internalSurfaceArea = internalSurfaceAreas.values.sum()

        return totalSurfaceArea - internalSurfaceArea
    }

    fun internalIslands(): Set<Set<Coord3d>> {
        val unvisited = mutableSetOf<Coord3d>()
        for (x in min.x()..max.x()) {
            for (y in min.y()..max.y()) {
                for (z in min.z()..max.z()) {
                    val candidate = Triple(x, y, z)
                    if (!droplets.contains(candidate)) {
                        unvisited.add(candidate)
                    }
                }
            }
        }

        val externalIslands = mutableSetOf<Set<Coord3d>>()
        val internalIslands = mutableSetOf<Set<Coord3d>>()

        while(unvisited.isNotEmpty()) {
            var isInternal = true
            val currIsland = mutableSetOf<Coord3d>()
            val q = ArrayDeque<Coord3d>()
            val start = unvisited.first()
            q.add(start)

            while(q.isNotEmpty()) {
                val next = q.removeFirst()
                if (!unvisited.remove(next)) {
                    continue
                }
                currIsland.add(next)

                val neighbors = next.neighbors()
                if (neighbors.any { !it.containedBy(min, max)}) {
                    isInternal = false
                }
                val unvisitedNeighbors = neighbors.filter { unvisited.contains(it) }
                q.addAll(unvisitedNeighbors)
            }

            if (isInternal) {
                internalIslands.add(currIsland)
            } else {
                externalIslands.add(currIsland)
            }
        }

        return internalIslands
    }

}



