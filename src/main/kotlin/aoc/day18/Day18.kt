package aoc.day18

import aoc.Puzzle
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
    override fun Sequence<String>.parse(): List<Node> = toList().map { StringReader(it).parse() }

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

    private fun <T : Node> Node.firstOrNull(depth: Int = 0, function: Node.(depth: Int) -> T?): T? {
        val result = function(depth)
        if (result != null) return result
        if (this !is PairNode) return null
        return left.firstOrNull(depth.inc(), function) ?: right.firstOrNull(depth.inc(), function)
    }

    private fun Node.findNextExplodingNode() = firstOrNull { depth -> if (this is PairNode && depth == THRESHOLD_DEPTH) this else null }
    private fun Node.findNextSplittingNode() = firstOrNull { if (this is NumberNode && value >= SPLIT_THRESHOLD) this else null }

    private fun Node.nextExplosion(): Boolean {
        val explodingNode = findNextExplodingNode() ?: return false
        explodingNode.explode(this)
        return true
    }

    private fun Node.nextSplit(): Boolean {
        val splittingNode = findNextSplittingNode() ?: return false
        splittingNode.split(this)
        return true
    }

    private fun Node.reduced(): Node = if (nextExplosion() || nextSplit()) reduced() else this

    private fun Node.findParent(startingNode: Node): PairNode? {
        if (startingNode !is PairNode) return null
        if (this == startingNode.left || this == startingNode.right) return startingNode
        return findParent(startingNode.left) ?: findParent(startingNode.right)
    }

    private fun NumberNode.split(startingNode: Node) {
        val split = PairNode(
            NumberNode(floor(value / 2.0).toInt()),
            NumberNode(ceil(value / 2.0).toInt())
        )
        requireNotNull(findParent(startingNode)).replaceNode(this, split)
    }

    private fun PairNode.explode(startingNode: Node) {
        explode(startingNode, PairNode::left) { edgeNumber(PairNode::right) }
        explode(startingNode, PairNode::right) { edgeNumber(PairNode::left) }
        requireNotNull(findParent(startingNode)).replaceNode(this, NumberNode(0))
    }

    private inline fun PairNode.explode(
        startingNode: Node,
        noinline sideNode: PairNode.() -> Node,
        edgeNumber: Node.() -> NumberNode
    ) {
        val parent = firstParent(startingNode, sideNode) ?: return
        val side = sideNode()
        require(side is NumberNode)
        parent.sideNode().edgeNumber().value += side.value
    }

    private tailrec fun Node.edgeNumber(next: (PairNode).() -> Node): NumberNode {
        if (this is NumberNode) return this
        require(this is PairNode)
        return next().edgeNumber(next)
    }

    private tailrec fun Node.firstParent(startingNode: Node, side: PairNode.() -> Node): PairNode? {
        val parent = findParent(startingNode) ?: return null
        return if (parent.side() != this) parent else parent.firstParent(startingNode, side)
    }

    private fun mergeAndReduce(left: Node, right: Node) = left.plus(right).reduced()
    private fun List<Node>.deepCopy() = map(Node::copy)

    override fun List<Node>.solvePartOne(): Int = deepCopy().reduce(::mergeAndReduce).magnitude

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

class PairNode(var left: Node, var right: Node) : Node() {
    override val magnitude: Int get() = left.magnitude * 3 + right.magnitude * 2
    override fun copy(): Node = PairNode(left.copy(), right.copy())
    override fun toString() = "[$left, $right]"

    fun replaceNode(nodeToFind: Node, replacement: Node) =
        if (left === nodeToFind) this::left.set(replacement) else this::right.set(replacement)
}
