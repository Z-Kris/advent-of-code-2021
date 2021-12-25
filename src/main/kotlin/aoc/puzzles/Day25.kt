package aoc.puzzles

import aoc.Point

/**
 * @author Kris | 25/12/2021
 */
object Day25 : Puzzle<SeaFloor, Int>(25) {
    override fun List<String>.parse(): SeaFloor {
        val width = first().length
        require(all { it.length == width }) { "Varying widths in the seafloor." }
        val allCucumbers = mutableListOf<List<SeaCucumber>>()
        for (line in this) {
            val cucumbers = mutableListOf<SeaCucumber>()
            allCucumbers += cucumbers
            for (char in line) {
                cucumbers += when (char) {
                    '>' -> SeaCucumber(SeaCucumberType.East)
                    'v' -> SeaCucumber(SeaCucumberType.South)
                    '.' -> SeaCucumber(SeaCucumberType.None)
                    else -> error("Invalid character: $char")
                }
            }
        }
        return allCucumbers
    }

    private fun SeaFloor.cycle(): Pair<Boolean, SeaFloor> {
        val nextSeafloor = map(List<SeaCucumber>::toMutableList)
        val eastMovement = nextSeafloor.move(SeaCucumberType.East)
        val southMovement = nextSeafloor.move(SeaCucumberType.South)
        return (eastMovement || southMovement) to nextSeafloor
    }

    private fun List<MutableList<SeaCucumber>>.move(type: SeaCucumberType): Boolean {
        var changed = false
        val visitedPoints = mutableSetOf<Point>()
        forEachIndexed { y, seaCucumbers ->
            seaCucumbers.forEachIndexed loop@{ x, seaCucumber ->
                if (seaCucumber.type != type) return@loop
                val forwardCucumberPos = getPoint(type.pointOffset.merge(Point(x, y)))
                val currentPoint = getPoint(x, y)
                if (forwardCucumberPos in visitedPoints || currentPoint in visitedPoints) return@loop
                val forwardCucumber = get(forwardCucumberPos)
                if (forwardCucumber.type != SeaCucumberType.None) return@loop
                changed = true
                visitedPoints += currentPoint
                visitedPoints += forwardCucumberPos
                val previous = set(forwardCucumberPos, seaCucumber)
                set(currentPoint, previous)
            }
        }

        return changed
    }

    private fun SeaFloor.getPoint(point: Point): Point = getPoint(point.x, point.y)

    private fun SeaFloor.getPoint(x: Int, y: Int): Point {
        val yPos = if (y >= size) 0 else y
        val xPos = if (x >= first().size) 0 else x
        return Point(xPos, yPos)
    }

    private fun SeaFloor.get(point: Point): SeaCucumber = get(point.y)[point.x]

    private fun List<MutableList<SeaCucumber>>.set(point: Point, seaCucumber: SeaCucumber): SeaCucumber {
        val current = get(point)
        this[point.y][point.x] = seaCucumber
        return current
    }

    override fun SeaFloor.solvePartOne(): Int {
        /* Use a triple to determine the current stage - number of changes, whether a movement occurred, and the seafloor. */
        val initialState = Triple(0, true, this)
        val sequence = generateSequence(initialState) { (count, _, seafloor) ->
            val (changed, nextSeafloor) = seafloor.cycle()
            Triple(count.inc(), changed, nextSeafloor)
        }
        val (count, _, _) = sequence.first { (_, changed, _) -> !changed }
        return count
    }

    override fun SeaFloor.solvePartTwo(): Int = Int.MIN_VALUE
}

private typealias SeaFloor = List<List<SeaCucumber>>

data class SeaCucumber(val type: SeaCucumberType)
enum class SeaCucumberType(val pointOffset: Point) {
    East(Point(1, 0)),
    South(Point(0, 1)),
    None(Point(0, 0))
}
