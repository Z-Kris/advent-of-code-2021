package aoc.day20

import aoc.Point
import aoc.Puzzle
import aoc.toInt
import java.util.*

/**
 * @author Kris | 20/12/2021
 */
object Day20 : Puzzle<TrenchMap, Int>(20) {
    private const val LIT_BIT = '#'
    private const val PADDING = 1
    private val NEIGHBOUR_RANGE = -1..1
    private val NEIGHBOUR_POINTS = NEIGHBOUR_RANGE.map { x -> NEIGHBOUR_RANGE.map { y -> Point(x, y) } }.flatten()

    override fun Sequence<String>.parse(): TrenchMap = toList().let { list ->
        list.drop(2).toTrenchMap(list.first().toImageEnhancement())
    }

    private fun String.toImageEnhancement(): ImageEnhancement = ImageEnhancement(length).apply {
        forEachIndexed { index, c -> if (c == LIT_BIT) set(index) }
    }

    private fun List<String>.toTrenchMap(imageEnhancement: BitSet): TrenchMap {
        require(all { it.length == size }) { "Grid has uneven sides." }
        val grid = Grid(size * size)
        forEachIndexed { x, line ->
            line.forEachIndexed { y, char ->
                if (char == LIT_BIT) grid[x, y, size] = true
            }
        }
        return TrenchMap(imageEnhancement, size, grid)
    }

    private fun TrenchMap.enhance(litBorder: Boolean): TrenchMap {
        val nextSize = size + PADDING * 2
        val nextGrid = BitSet(nextSize)
        for (x in 0 until nextSize) {
            for (y in 0 until nextSize) {
                nextGrid[x, y, nextSize] = imageEnhancement.get(getPixelValue(Point(x - PADDING, y - PADDING), litBorder))
            }
        }
        return TrenchMap(imageEnhancement, nextSize, nextGrid)
    }

    private operator fun Grid.get(x: Int, y: Int, size: Int, litBorder: Boolean): Boolean =
        if (x < 0 || x >= size || y < 0 || y >= size) litBorder else this[x, y, size]

    private fun TrenchMap.getPixelValue(center: Point, litBorder: Boolean): Int = NEIGHBOUR_POINTS.fold(0) { acc, offset ->
        acc shl 1 or grid.pointToBit(center.merge(offset), size, litBorder)
    }

    private operator fun Grid.get(x: Int, y: Int, size: Int) = get(x * size + y)
    private operator fun Grid.set(x: Int, y: Int, size: Int, value: Boolean) = set(x * size + y, value)
    private fun Grid.pointToBit(point: Point, size: Int, invert: Boolean): Int = get(point.x, point.y, size, invert).toInt

    private fun TrenchMap.getLitPixelCount(enhancements: Int): Int {
        val sequence = generateSequence(false to this) { (invert, map) ->
            !invert to map.enhance(map.litBorder && invert)
        }
        val (_, map) = sequence.drop(enhancements).first()
        return map.grid.cardinality()
    }

    override fun TrenchMap.solvePartOne(): Int = getLitPixelCount(2)
    override fun TrenchMap.solvePartTwo(): Int = getLitPixelCount(50)
}

private typealias ImageEnhancement = BitSet
private typealias Grid = BitSet

data class TrenchMap(val imageEnhancement: ImageEnhancement, val size: Int, val grid: BitSet) {
    val litBorder get() = imageEnhancement.get(0)
}
