@file:Suppress("NOTHING_TO_INLINE")

package aoc.puzzles

/**
 * @author Kris | 12/12/2021
 */
object Day12 : Puzzle<CaveTree, Int>(12) {
    private const val STARTING_GRID = 0
    private const val START_LABEL = "start"
    private const val END_LABEL = "end"

    /* The maximum number of unique elements in the grid, excluding start and end. */
    private const val MAX_GRID_SIZE = Int.SIZE_BITS

    /* Since our grid is a bit set, we need to offset the large cave bits; In this case, we simply split it in half. */
    private const val LARGE_BIT_OFFSET = MAX_GRID_SIZE shr 1

    /* The range of bit ids that indicate it is a small cavern. */
    private val SMALL_BIT_RANGE = 0 until LARGE_BIT_OFFSET
    private const val SUCCESSFUL_PATH = 1
    private const val FAILED_PATH = 0

    override fun List<String>.parse(): CaveTree {
        /* Use two maps to track which label matches which bit index, as we convert it to unique bits. */
        val smallBits = mutableMapOf<String, Int>()
        val largeBits = mutableMapOf<String, Int>()
        return buildMap<Cave, MutableSet<Cave>> {
            this@parse.forEach {
                val (fromLabel, toLabel) = it.split('-')
                val from = Cave(fromLabel.getAsBit(smallBits, largeBits))
                val to = Cave(toLabel.getAsBit(smallBits, largeBits))
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

    private inline operator fun PassageGrid.get(bitId: Int): Boolean = this shr bitId and 0x1 != 0
    private inline val String.isSmallLabel get() = all(Char::isLowerCase)
    private inline val CaveTree.startCave get() = keys.single { it.bit == Int.MIN_VALUE }
    private inline val Cave.isSmall get() = bit in SMALL_BIT_RANGE
    private inline fun PassageGrid.smallCaveEnabled(node: Cave) = node.isSmall && get(node.bit)

    private fun CaveTree.visit(node: Cave, grid: PassageGrid, spareSmallCavern: Boolean): Int {
        if (node.bit == Int.MAX_VALUE) return SUCCESSFUL_PATH
        val connectedCaves = requireNotNull(get(node))
        val nextGrid = if (node.isSmall) grid.enable(node.bit) else grid
        val exhaustedSpareCavern = !spareSmallCavern || grid.smallCaveEnabled(node)
        return connectedCaves.sumOf {
            if (exhaustedSpareCavern && grid.smallCaveEnabled(it)) FAILED_PATH
            else visit(it, nextGrid, !exhaustedSpareCavern)
        }
    }

    private inline fun PassageGrid.enable(bit: Int): PassageGrid = this or (1 shl bit)
    private inline fun isValid(from: Cave, to: Cave) = from.bit != Int.MAX_VALUE && to.bit != Int.MIN_VALUE

    override fun CaveTree.solvePartOne(): Int = visit(startCave, STARTING_GRID, false)
    override fun CaveTree.solvePartTwo(): Int = visit(startCave, STARTING_GRID, true)
}

private typealias CaveTree = Map<Cave, Set<Cave>>
private typealias PassageGrid = Int

@JvmInline
value class Cave(val bit: Int)
