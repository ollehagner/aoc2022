package day07

import java.util.*

class Filesystem {

    val TOTAL_DISK_SPACE = 70000000

    val dirStack = LinkedList<Directory>()
    val root = Directory("root", null)

    init {
        dirStack.add(root)
    }

    fun availableDiskspace(): Int {
        return TOTAL_DISK_SPACE - root.value()
    }

    fun allDirectories(): List<Directory> {
        var toProcess = mutableListOf<Directory>()
        var result = mutableListOf<Directory>()
        toProcess.addAll(root.subDirs())
        do {
            val inProgressDir = toProcess.removeFirst()
            result.add(inProgressDir)
            toProcess.addAll(inProgressDir.subDirs())
        } while(toProcess.isNotEmpty())
        return result
    }

    fun handleCommand(command: String, output: List<String>) {
        when {
            command == "$ cd .." -> {
                if(dirStack.size > 1) {
                    dirStack.pop()
                }
            }
            command.matches("\\$ cd .*".toRegex()) -> {
                val currentDir = dirStack.peek()
                val newDir = Directory(command.substringAfter("cd "), currentDir)
                currentDir.addChild(newDir)
                dirStack.push(newDir)
            }

            command.startsWith("$ ls") -> {
                val currentDir = dirStack.peek()!!
                output
                    .filter { !it.startsWith("dir") }
                    .forEach { fileInfo ->
                        fileInfo.split(" ").let { parts ->
                            currentDir.addChild(File(parts[1], parts[0].toInt()))
                        }
                    }
            }
        }
    }

    fun print() {
        dirStack.last().print(0)
    }

}