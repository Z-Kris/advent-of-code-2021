package aoc.puzzles

import aoc.runIfNotNull
import java.io.Reader
import java.io.StringReader
import kotlin.math.ceil
import kotlin.math.floor

/**
 * @author Kris | 18/12/2021
 */
object Day18 : Puzzle<List<Node>, Int>(18) {
    private const val THRESHOLD_DEPTH = 4
    private const val SPLIT_THRESHOLD = 10
    private const val OPENING_CHAR = '['
    private const val SEPARATOR = ','
    private const val CLOSING_CHAR = ']'
    override fun List<String>.parse(): List<Node> = map { StringReader(it).parse() }

    private fun Reader.parse(): Node {
        val char = requireNotNull(nextChar)
        if (char != OPENING_CHAR) return NumberNode(char.digitToInt())
        val left = parse()
        require(nextChar == SEPARATOR)
        val right = parse()
        require(nextChar == CLOSING_CHAR)
        return PairNode(left, right)
    }

    private val Reader.nextChar: Char? get() = read().let { if (it == -1) null else it.toChar() }

    private fun Node.findNextExplodingNode(depth: Int = 0): PairNode? = when (this) {
        is PairNode -> if (depth == THRESHOLD_DEPTH) this else firstNotNullOfOrNull { it.findNextExplodingNode(depth.inc()) }
        else -> null
    }

    private fun Node.findNextSplittingNode(): NumberNode? = when (this) {
        is NumberNode -> if (value >= SPLIT_THRESHOLD) this else null
        is PairNode -> firstNotNullOfOrNull { it.findNextSplittingNode() }
    }

    private fun Node.nextExplosion(): Boolean = runIfNotNull(findNextExplodingNode()) { it.explode(this) }
    private fun Node.nextSplit(): Boolean = runIfNotNull(findNextSplittingNode()) { it.split(this) }

    private tailrec fun Node.reduced(): Node = if (nextExplosion() || nextSplit()) reduced() else this

    private fun Node.findParent(startingNode: Node): PairNode? {
        if (startingNode !is PairNode) return null
        return if (this in startingNode) startingNode else startingNode.firstNotNullOfOrNull { findParent(it) }
    }

    private fun NumberNode.split(startingNode: Node) =
        requireNotNull(findParent(startingNode)).replaceNode(this, splitToPairNode())

    private fun NumberNode.splitToPairNode() = PairNode(
        NumberNode(floor(value / 2.0).toInt()),
        NumberNode(ceil(value / 2.0).toInt())
    )

    private fun PairNode.explode(startingNode: Node) {
        firstParentWithout(startingNode, PairNode::left)?.left?.findNumberNode(PairNode::right)?.add(left as NumberNode)
        firstParentWithout(startingNode, PairNode::right)?.right?.findNumberNode(PairNode::left)?.add(right as NumberNode)
        requireNotNull(findParent(startingNode)).replaceNode(this, NumberNode(0))
    }

    private fun NumberNode.add(numberNode: NumberNode) {
        value += numberNode.value
    }

    private tailrec fun Node.findNumberNode(next: (PairNode).() -> Node): NumberNode {
        if (this is NumberNode) return this
        require(this is PairNode)
        return next().findNumberNode(next)
    }

    private tailrec fun Node.firstParentWithout(startingNode: Node, side: PairNode.() -> Node): PairNode? {
        val parent = findParent(startingNode) ?: return null
        return if (parent.side() != this) parent else parent.firstParentWithout(startingNode, side)
    }

    private fun mergeAndReduce(left: Node, right: Node) = left.plus(right).reduced()
    private fun List<Node>.deepCopy() = map(Node::copy)

    override fun List<Node>.solvePartOne(): Int = deepCopy().reduce(Day18::mergeAndReduce).magnitude

    override fun List<Node>.solvePartTwo(): Int = uniquePermutations()
        .map { (left, right) -> mergeAndReduce(left, right) }
        .maxOf(Node::magnitude)

    private fun List<Node>.uniquePermutations() = buildMap {
        for (left in this@uniquePermutations) {
            for (right in this@uniquePermutations) {
                if (left === right) continue
                put(left.copy(), right.copy())
                put(right.copy(), left.copy())
            }
        }
    }

    private operator fun Node.plus(other: Node) = PairNode(this, other)
}

sealed class Node {
    abstract val magnitude: Int
    abstract fun copy(): Node
}

class NumberNode(var value: Int) : Node() {
    override val magnitude: Int get() = value
    override fun copy(): Node = NumberNode(value)
    override fun toString() = value.toString()
}

class PairNode(var left: Node, var right: Node) : Node(), Iterable<Node> {
    override val magnitude: Int get() = left.magnitude * 3 + right.magnitude * 2
    override fun copy(): Node = PairNode(left.copy(), right.copy())
    override fun toString() = "[$left, $right]"

    fun replaceNode(nodeToFind: Node, replacement: Node) =
        if (left === nodeToFind) this::left.set(replacement) else this::right.set(replacement)

    override fun iterator(): Iterator<Node> = sequenceOf(left, right).iterator()
}
