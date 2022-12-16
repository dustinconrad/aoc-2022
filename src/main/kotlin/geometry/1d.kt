package geometry

import kotlin.math.max

fun IntRange.fullyContains(other: IntRange): Boolean {
    return this.first <= other.first && this.last >= other.last
}

fun IntRange.disjoint(other: IntRange): Boolean {
    return this.last < other.first || this.first > other.last
}

fun IntRange.overlaps(other: IntRange) = !this.disjoint(other)

fun IntRange.combine(other: IntRange): IntRange {
    require(this.overlaps(other)) { "$this must overlap $other" }
    return kotlin.math.min(this.first, other.first).rangeTo(max(this.last, other.last))
}

fun IntRange.split(on: Int): Pair<IntRange, IntRange> {
    require(this.contains(on)) { "$this must contain $on" }
    return this.first until on to (on + 1 .. this.last)
}