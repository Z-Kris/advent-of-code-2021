package aoc.day12

import aoc.Puzzle
import java.util.*

/**
 * @author Kris | 12/12/2021
 */
object Day12 : Puzzle<NodeTree, Int>(12) {
    private const val START_LABEL = "start"
    private const val END_LABEL = "end"

    /* The maximum size of the grid, this can be any number, but it will also allocate this number of bits underneath. */
    private const val MAX_GRID_SIZE = 64

    /* Since our grid is a bit set, we need to offset the large cave bits; In this case, we simply split it in half. */
    private const val LARGE_BIT_OFFSET = MAX_GRID_SIZE shr 1

    /* The range of bit ids that indicate it is a small cavern. */
    private val SMALL_BIT_RANGE = 0 until LARGE_BIT_OFFSET
    private const val SUCCESSFUL_PATH = 1
    private const val FAILED_PATH = 0

    @OptIn(ExperimentalStdlibApi::class)
    override fun Sequence<String>.parse(): NodeTree {
        /* Use two maps to track which label matches which bit index, as we convert it to unique bits. */
        val smallBits = mutableMapOf<String, Int>()
        val largeBits = mutableMapOf<String, Int>()
        return buildMap<Node, MutableSet<Node>> {
            this@parse.forEach {
                val (fromLabel, toLabel) = it.split('-')
                val from = Node(fromLabel.getAsBit(smallBits, largeBits))
                val to = Node(toLabel.getAsBit(smallBits, largeBits))
                if (isValid(from, to)) getOrPut(from, ::mutableSetOf).add(to)
                if (isValid(to, from)) getOrPut(to, ::mutableSetOf).add(from)
            }
        }
    }

    private fun String.getAsBit(smallBits: MutableMap<String, Int>, largeBits: MutableMap<String, Int>) = when {
        this == START_LABEL -> Int.MIN_VALUE
        this == END_LABEL -> Int.MAX_VALUE
        this.isSmallLabel -> smallBits.getOrPut(this, smallBits::size)
        else -> largeBits.getOrPut(this) { LARGE_BIT_OFFSET + largeBits.size }
    }

    private inline val String.isSmallLabel get() = all(Char::isLowerCase)
    private inline val NodeTree.startNode get() = keys.single { it.bit == Int.MIN_VALUE }
    private inline val Node.isSmall get() = bit in SMALL_BIT_RANGE
    private fun Grid.smallNodeEnabled(node: Node) = node.isSmall && get(node.bit)

    private fun NodeTree.visit(node: Node, grid: Grid, spareSmallCavern: Boolean): Int {
        if (node.bit == Int.MAX_VALUE) return SUCCESSFUL_PATH
        val connectedNodes = requireNotNull(get(node))
        val nextGrid = if (node.isSmall) grid.cloneWithBit(node.bit) else grid
        val exhaustedSpareCavern = !spareSmallCavern || grid.smallNodeEnabled(node)
        return connectedNodes.sumOf {
            if (exhaustedSpareCavern && grid.smallNodeEnabled(it)) FAILED_PATH
            else visit(it, nextGrid, !exhaustedSpareCavern)
        }
    }

    private fun Grid.cloneWithBit(bit: Int): Grid = (clone() as Grid).apply { set(bit) }
    private fun isValid(from: Node, to: Node) = from.bit != Int.MAX_VALUE && to.bit != Int.MIN_VALUE

    override fun NodeTree.solvePartOne(): Int = visit(startNode, Grid(MAX_GRID_SIZE), false)
    override fun NodeTree.solvePartTwo(): Int = visit(startNode, Grid(MAX_GRID_SIZE), true)
}

private typealias NodeTree = Map<Node, Set<Node>>
private typealias Grid = BitSet

@JvmInline
value class Node(val bit: Int)
