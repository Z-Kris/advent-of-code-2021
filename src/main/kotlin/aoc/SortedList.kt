package aoc

/**
 * @author Kris | 07/12/2021
 */
class SortedList<T> private constructor(private val list: List<T>) : List<T> by list {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as SortedList<*>
        if (list != other.list) return false
        return true
    }

    override fun hashCode(): Int = list.hashCode()
    override fun toString(): String = "SortedList(list=$list)"

    companion object {
        fun <T> List<T>.toSortedList(): SortedList<T> where T : Comparable<T> = SortedList(sorted())
    }
}
