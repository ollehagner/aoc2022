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

inline fun <T> Sequence<T>.takeUntil(predicate: (T) -> Boolean): List<T> {
    val list = ArrayList<T>()
    for (item in this) {
        list.add(item)
        if (predicate(item))
            break
    }
    return list
}
fun IntRange.fullyOverlaps(other: IntRange): Boolean {
    return this.first <= other.first && this.last >= other.last
}

fun IntRange.overlap(other: IntRange): Boolean {
    return this.first <= other.last && other.first <= this.last
}

inline fun <R> Iterable<R>.log(): List<R> {
    return map { value ->
        println(value)
        value
    }
}

inline fun <T> dequeOf(vararg elements: T): Deque<T> = if (elements.isNotEmpty()) LinkedList(elements.asList()) else LinkedList()

fun infiniteInts(startValue: Int): Sequence<Int> = sequence {
    var value = startValue
    while (true) {
        yield(value++)
    }
}

fun <T> infiniteSequence(value: T): Sequence<T> = sequence {
    while(true) {
        yield(value)
    }
}

fun <T> infiniteSequence(value: List<T>): Sequence<T> = sequence {
    val loopingIterator = LoopingIterator(value)
    while(true) {
        yield(loopingIterator.next())
    }
}

inline fun <T> List<T>.permutations(other: List<T>): List<List<T>> {
    return flatMap { first ->
        other.map { second ->
            listOf(first, second)
        }
    }
}

inline fun <T> List<List<T>>.addPermutation(other: List<T>): List<List<T>> {
    return flatMap { first ->
        other.map { second ->
            first + second
        }
    }
}

class LoopingIterator<T>(private val items: List<T>) : Iterator<T> {

    var iterator = items.iterator()

    override fun hasNext(): Boolean {
        return true
    }

    override fun next(): T {
        if(!iterator.hasNext()) {
            iterator = items.iterator()
        }
        return iterator.next()
    }

}