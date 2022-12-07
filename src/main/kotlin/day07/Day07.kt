package day07

import readResourceAsBufferedReader
import java.util.function.Predicate

fun main() {
    println("part 1: ${part1(readResourceAsBufferedReader("7_1.txt").readLines())}")
    println("part 2: ${part2(readResourceAsBufferedReader("7_1.txt").readLines())}")
}

fun part1(input: List<String>): Int {
    val device = Device()
    val instructions = parseCommands(input)
    instructions.forEach { device.execute(it) }

    val found = device.dfs { it is Directory &&  it.size() <= 100000 }

    return found.sumOf { it.size() }
}

fun part2(input: List<String>): Int {
    val device = Device()
    val instructions = parseCommands(input)
    instructions.forEach { device.execute(it) }

    val usedSpace = device.usedSpace()
    val freeSpace = 70000000 - usedSpace

    val target = 30000000 - freeSpace

    val found = device.dfs { it is Directory &&  it.size() >= target }

    return found.sortedBy { it.size() }[0].size()
}

fun parseCommands(lines: List<String>): List<Command> {
    val commands = mutableListOf<Command>()
    var input = lines[0]
    var output = mutableListOf<String>()
    for (i in 1 until lines.size) {
        val curr = lines[i]
        if (curr.startsWith("$")) {
            var arg = ""
            val parts = input.split(" ")
            if (parts.size == 3) {
                arg = parts[2]
            }
            commands.add(Command(parts[1], arg, output))
            input = curr
            output = mutableListOf()
        } else {
            output.add(curr)
        }
    }
    if (output.isNotEmpty()) {
        var arg = ""
        val parts = input.split(" ")
        if (parts.size == 3) {
            arg = parts[2]
        }
        commands.add(Command(parts[1], arg, output))
    }
    return commands
}

class Device {
    private val root = Directory(null, "/")
    private var currContext: FileOrDirectory = root

    fun execute(command: Command) {
        when(command.cmd) {
            "cd" -> {
                currContext = currContext.cd(command.arg)
            }
            "ls" -> currContext.ls(command.output)
        }
    }

    fun dfs(pred: Predicate<FileOrDirectory>): List<FileOrDirectory> {
        return root.dfs(pred)
    }

    fun usedSpace(): Int = root.size()

}

data class Command(val cmd: String, val arg: String, val output: List<String>)

sealed class FileOrDirectory(val parent: FileOrDirectory?, val name: String) {

    abstract fun size(): Int

    abstract fun cd(input: String): FileOrDirectory

    abstract fun ls(output: List<String>)

    abstract fun dfs(pred: Predicate<FileOrDirectory>): List<FileOrDirectory>

}

class File(parent: FileOrDirectory?, name: String, val size: Int): FileOrDirectory(parent, name) {

    override fun size(): Int {
        return size
    }

    override fun cd(input: String): FileOrDirectory {
        throw IllegalArgumentException("Not a directory")
    }

    override fun ls(output: List<String>) {
        throw IllegalArgumentException("Not a directory")
    }

    override fun dfs(pred: Predicate<FileOrDirectory>): List<FileOrDirectory> {
        return if (pred.test(this)) {
            listOf(this)
        } else {
            emptyList()
        }
    }

    override fun toString(): String {
        return "File(name=$name,size=$size)"
    }


}

class Directory(parent: FileOrDirectory?, name: String): FileOrDirectory(parent, name) {

    private val files = mutableMapOf<String, FileOrDirectory>()

    private val _size: Int by lazy {
        files.values.sumOf { it.size() }
    }

    override fun size(): Int {
        return _size
    }

    override fun cd(input: String): FileOrDirectory {
        return when(input) {
            ".." -> parent ?: throw IllegalStateException("No parent")
            "/" -> {
                var p: FileOrDirectory = this
                while(p.parent != null) {
                    p = p.parent!!
                }
                p
            }
            else -> files[input]!!
        }
    }

    override fun ls(output: List<String>) {
        output.forEach {
            val (l, r) = it.split(" ")
            if (l == "dir") {
                files[r] = Directory(this, r)
            } else {
                files[r] = File(this, r, l.toInt())
            }
        }
    }

    override fun dfs(pred: Predicate<FileOrDirectory>): List<FileOrDirectory> {
        var result = mutableListOf<FileOrDirectory>()
        if (pred.test(this)) {
            result.add(this)
        }
        files.values.forEach { result.addAll(it.dfs(pred)) }
        return result
    }

    override fun toString(): String {
        return "Directory(name=$name,size=${size()})"
    }
}
