import java.io.File
import java.math.BigInteger
import java.security.MessageDigest
import java.util.*
import java.util.function.Predicate

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String): List<String> = File("src/main", "$name").readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5(): String = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray())).toString(16)


inline fun<R> List<R>.groupUntil(predicate: Predicate<R>): List<List<R>> {
    return fold(
        LinkedList<List<R>>()
    ) {
            acc, value ->
        if(acc.isEmpty() || predicate.test(value)) {
            acc.addLast(listOf(value))
        } else {
            val currentList = acc.removeLast()
            acc.addLast(listOf(currentList, listOf( value)).flatten())
        }
        acc
    }
}

fun IntRange.fullyOverlaps(other: IntRange): Boolean {
    return this.first <= other.first && this.last >= other.last
}

fun IntRange.overlap(other: IntRange): Boolean {
    return this.first <= other.last && other.first <= this.last
}