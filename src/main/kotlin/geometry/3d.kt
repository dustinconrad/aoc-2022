package geometry

typealias Coord3d = Triple<Int, Int, Int>

fun Coord3d.x() = this.first

fun Coord3d.y() = this.second

fun Coord3d.z() = this.third

operator fun Coord3d.plus(other: Coord3d): Coord3d {
    return Triple(this.x() + other.x(), this.y() + other.y(), this.z() + other.z())
}

fun Coord3d.neighbors(): List<Coord3d> {
    return listOf(
        Triple(1, 0 , 0),
        Triple(0, 1, 0),
        Triple(0, 0, 1),
        Triple(-1, 0 , 0),
        Triple(0, -1, 0),
        Triple(0, 0, -1),
    ).map { this + it }
}

fun parseCoord3d(line: String): Coord3d {
    val (x, y, z) = line.split(",").map { it.trim() }.map { it.toInt() }
    return Triple(x, y, z)
}

fun Coord3d.containedBy(mins: Coord3d, maxes: Coord3d): Boolean {
    return (mins.x()..maxes.x()).contains(this.x()) &&
            (mins.y()..maxes.y()).contains(this.y()) &&
            (mins.z()..maxes.z()).contains(this.z())
}