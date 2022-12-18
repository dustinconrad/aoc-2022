import java.io.BufferedReader
import java.math.BigInteger
import java.security.MessageDigest

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

fun readResourceAsBufferedReader(resourceName: String): BufferedReader {
    val classloader = Thread.currentThread().contextClassLoader
    val stream = classloader.getResourceAsStream(resourceName)

    return stream.bufferedReader()
}

fun List<String>.byEmptyLines(): List<String> {
    val result = mutableListOf<String>()
    val curr = mutableListOf<String>()
    for (line in this) {
        if (line.isBlank()) {
            result.add(curr.joinToString("\n"))
            curr.clear()
        } else {
            curr.add(line)
        }
    }
    if (curr.isNotEmpty()) {
        result.add(curr.joinToString("\n"))
    }
    return result
}

fun <T> combinations(itemLists: List<List<T>>): Set<List<T>> =
    when {
        itemLists.isEmpty() -> emptySet()
        itemLists.size == 1 -> itemLists[0].map { listOf(it) }.toSet()
        else -> {
            val base: List<T> = itemLists[0]

            combinations(itemLists.subList(1, itemLists.size))
                .flatMap { ls -> base.map { mutableListOf(it) + ls } }
                .toSet()
        }
    }