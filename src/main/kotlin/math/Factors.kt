package math

data class ExtendedEuclidianResult(
    val gcd: Long,
    val s: Long,
    val t: Long
)

fun extendedEuclidean(a: Long, b: Long): ExtendedEuclidianResult {
    var ri = b
    var riSubOne = a
    var si = 0L
    var siSubOne = 1L
    var ti = 1L
    var tiSubOne = 0L

    while (ri != 0L) {
        val quotient = riSubOne / ri
        val riPlusOne = riSubOne - quotient * ri
        val siPlusOne = siSubOne - quotient * si
        val tiPlusOne = tiSubOne - quotient * ti

        riSubOne = ri
        siSubOne = si
        tiSubOne = ti

        ri = riPlusOne;
        si = siPlusOne;
        ti = tiPlusOne;
    }

    return ExtendedEuclidianResult(
        riSubOne,
        siSubOne,
        tiSubOne
    )
}

fun lcm(vararg numbers: Long): Long {
    val (a, b) = numbers
    val gcd = extendedEuclidean(a, b).gcd
    val result = (a * b) / gcd;
    return numbers.drop(2).fold(result) { acc, n -> lcm(acc, n) }
}