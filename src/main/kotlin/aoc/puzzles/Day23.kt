package aoc.puzzles

import java.util.*
import kotlin.math.abs
import kotlin.math.min

/**
 * @author Kris | 23/12/2021
 */
private const val MISSING_AMPHIPOD_CHAR = '.'
private const val ROW_SIZE = 4
private const val HALLWAY_SIZE = 11
private val Int.columnToHallway get() = (this + 1) * 2
private typealias Hallway = List<Amphipod>

object Day23 : Puzzle<List<String>, Long>(23) {
    private val ROOM_REGEX = Regex("""\s?\s?#+([A-D])#([A-D])#([A-D])#([A-D])#+""")
    private val EMPTY_HALLWAY = List(HALLWAY_SIZE) { Amphipod(MISSING_AMPHIPOD_CHAR, HallwayPosition(it)) }
    private val UNFOLDED_ROOMS = """
        |  #D#C#B#A#
        |  #D#B#A#C#
    """.trimMargin()

    override fun List<String>.parse(): List<String> = filter { ROOM_REGEX.find(it) != null }

    private fun List<String>.toAmphipodState(): AmphipodState {
        val nodes = mutableListOf<Amphipod>()
        for ((row, line) in this.withIndex()) {
            val chars = ROOM_REGEX.matchEntire(line) ?: error("Corrupt line: $line")
            val room = chars.groupValues.drop(1).toList().flatMap(String::toList).withIndex().map { (col, char) ->
                Amphipod(char, RoomPosition(col, row))
            }
            nodes += room
        }
        return AmphipodState(EMPTY_HALLWAY, Rooms(nodes), 0)
    }

    private fun AmphipodState.computeShortestPath(): Long {
        val queue = PriorityQueue(compareBy(AmphipodState::cost))
        queue.add(this)
        var lowestCost = Long.MAX_VALUE
        val calculatedCosts = mutableMapOf<AmphipodState, Long>()
        while (queue.isNotEmpty()) {
            val state = queue.poll()
            /* Once we reach a stage where the cost already exceeds the lowest cost, there's no need to compute it any further. */
            if (state.cost >= lowestCost) break
            for (amphipod in state) {
                val positions = state.calculateValidMovements(amphipod)
                if (positions.isEmpty()) continue
                for (targetPosition in positions) {
                    val next = state.move(amphipod, targetPosition)
                    if (next.complete) {
                        lowestCost = min(lowestCost, next.cost)
                    } else if (next.cost < calculatedCosts.getOrDefault(next, Long.MAX_VALUE)) {
                        calculatedCosts[next] = next.cost
                        queue.add(next)
                    }
                }
            }
        }
        return lowestCost
    }

    override fun List<String>.solvePartOne(): Long = toAmphipodState().computeShortestPath()

    override fun List<String>.solvePartTwo(): Long {
        require(size == 2) { "Initial state must be two entries." }
        val (a, b) = this
        val unfoldedList = mutableListOf<String>()
        unfoldedList += a
        for (entry in UNFOLDED_ROOMS.lines()) unfoldedList += entry
        unfoldedList += b
        return unfoldedList.toAmphipodState().computeShortestPath()
    }
}

data class AmphipodState(val hallway: Hallway, val rooms: Rooms, val cost: Long) : Iterable<Amphipod> {
    val complete: Boolean get() = hallway.clear && rooms.complete
    private val Hallway.clear get() = all { it.type == MISSING_AMPHIPOD_CHAR }

    fun move(amphipod: Amphipod, destination: AmphipodPosition): AmphipodState {
        return if (!amphipod.position.inHallway) {
            moveToHallway(amphipod, destination)
        } else {
            moveToRoom(amphipod, destination)
        }
    }

    private fun moveToHallway(amphipod: Amphipod, destination: AmphipodPosition): AmphipodState {
        val rooms = rooms.toMutableList()
        val hallway = hallway.toMutableList()
        hallway[destination.hallwayPosition] = Amphipod(amphipod.type, destination)
        rooms[amphipod.position.row * ROW_SIZE + amphipod.position.column] = Amphipod(MISSING_AMPHIPOD_CHAR, amphipod.position)
        val distance = amphipod.position.distance(destination)
        return AmphipodState(hallway, Rooms(rooms), cost + (amphipod.stepCost * distance))
    }

    private fun moveToRoom(amphipod: Amphipod, destination: AmphipodPosition): AmphipodState {
        val rooms = rooms.toMutableList()
        val hallway = hallway.toMutableList()
        hallway[amphipod.position.hallwayPosition] = Amphipod(MISSING_AMPHIPOD_CHAR, amphipod.position)
        rooms[destination.row * ROW_SIZE + destination.column] = Amphipod(amphipod.type, destination)
        val distance = destination.distance(amphipod.position)
        return AmphipodState(hallway, Rooms(rooms), cost + (amphipod.stepCost * distance))
    }

    fun calculateValidMovements(amphipod: Amphipod): List<AmphipodPosition> {
        return if (amphipod.position.inHallway) {
            amphipod.getValidRoomSteps(rooms, hallway)
        } else {
            amphipod.getValidHallwaySteps(rooms, hallway)
        }
    }

    override fun toString(): String {
        val builder = StringBuilder()
        builder.append("#############")
        builder.appendLine()
        builder.append('#')
        for (pod in hallway) {
            builder.append(pod.type)
        }
        builder.append('#')
        builder.appendLine()
        val rowCount = rooms.size / ROW_SIZE
        builder.append("###")
        val firstRow = rooms.getRow(0)
        for (pod in firstRow) {
            builder.append(pod.type).append('#')
        }
        builder.append("##")
        builder.appendLine()

        for (row in 1 until rowCount) {
            builder.append("  #")
            for (pod in rooms.getRow(row)) {
                builder.append(pod.type).append('#')
            }
            builder.appendLine()
        }
        builder.append("  #########")
        builder.appendLine()
        return builder.toString()
    }

    override fun iterator(): Iterator<Amphipod> {
        val nodesInHallway = hallway.filterNot(Amphipod::isEmpty)
        val nodesInRooms = rooms.filterNot(Amphipod::isEmpty)
        return (nodesInHallway + nodesInRooms).listIterator()
    }
}

interface AmphipodPosition {
    val hallwayPosition: Int
    val column: Int
    val row: Int
    val inHallway: Boolean

    fun distance(hallwayAmphipod: AmphipodPosition): Int {
        require(!inHallway)
        require(hallwayAmphipod.inHallway)
        /* Number of steps to go up to reach the hallway. */
        val distanceToHallway = row + 1
        /* The position of the amphipod upon moving north, in hallway, from left to right. */
        val positionInHallway = column.columnToHallway
        /* The distance between the two hallway positions. */
        val hallwayDistanceDifference = abs(hallwayAmphipod.hallwayPosition - positionInHallway)
        return distanceToHallway + hallwayDistanceDifference
    }
}

data class HallwayPosition(override val hallwayPosition: Int) : AmphipodPosition {
    override val column: Int
        get() = error("Not in a room.")
    override val row: Int
        get() = error("Not in a room.")
    override val inHallway: Boolean
        get() = true
}

data class RoomPosition(override val column: Int, override val row: Int) : AmphipodPosition {
    override val hallwayPosition: Int
        get() = error("Not in a hallway.")
    override val inHallway: Boolean
        get() = false
}

data class Amphipod(val type: Char, val position: AmphipodPosition) {
    val isEmpty: Boolean get() = type == MISSING_AMPHIPOD_CHAR
    val stepCost: Long
        get() = when (type) {
            'A' -> 1
            'B' -> 10
            'C' -> 100
            'D' -> 1000
            else -> error("Invalid type: $type")
        }

    private val correctColumn: Int
        get() = when (type) {
            'A' -> 0
            'B' -> 1
            'C' -> 2
            'D' -> 3
            else -> error("Invalid type: $type")
        }

    /**
     * Preliminary movement checks to see if we can move **anywhere** at all.
     * This does not check for collision.
     */
    private fun canMove(rooms: Rooms): Boolean {
        if (position.inHallway) {
            val room = rooms.getColumn(correctColumn)
            /* Can only move to a room if that column only contains missing or correct types. */
            return room.all { it.type == MISSING_AMPHIPOD_CHAR || it.type == type }
        }
        val room = rooms.getColumn(position.column)
        /* Check if anything is blocking our movement above. */
        if (position.row > 0) {
            val range = (position.row - 1) downTo 0
            if (range.any { room[it].type != MISSING_AMPHIPOD_CHAR }) return false
        }
        /* If we aren't in the correct column, we must be able to move. */
        if (position.column != correctColumn) return true
        val row = position.row
        val range = row until room.size
        /* If any node below us doesn't belong in this column, we are able to move despite being in the correct column. */
        return range.any { room[it].type != type }
    }

    fun getValidRoomSteps(rooms: Rooms, hallway: Hallway): List<AmphipodPosition> {
        if (!canMove(rooms)) return emptyList()
        val destinationHallwayPosition = correctColumn.columnToHallway
        if (!hallway.canMove(destinationHallwayPosition, position.hallwayPosition)) return emptyList()
        val column = rooms.getColumn(correctColumn)
        val firstFilledIndex = column.indexOfFirst { !it.isEmpty }
        return listOf(column[if (firstFilledIndex == -1) (column.size - 1) else (firstFilledIndex - 1)].position)
    }

    fun getValidHallwaySteps(rooms: Rooms, hallway: Hallway): List<AmphipodPosition> {
        if (!canMove(rooms)) return emptyList()
        val destinationHallwayPosition = position.column.columnToHallway
        val availableSteps = mutableListOf<AmphipodPosition>()
        for (node in hallway) {
            if (!node.isEmpty) continue
            if (!hallway.canMove(destinationHallwayPosition, node.position.hallwayPosition)) continue
            availableSteps += node.position
        }
        return availableSteps
    }

    private fun Hallway.canMove(from: Int, to: Int): Boolean {
        if (to in INVALID_HALLWAY_SPOTS) return false
        val range = if (from > to) (from downTo to) else (from..to)
        return none { it !== this@Amphipod && !it.isEmpty && it.position.hallwayPosition in range }
    }

    companion object {
        private val INVALID_HALLWAY_SPOTS = 2..8 step 2
    }
}

data class Rooms(private val amphipods: List<Amphipod>) : List<Amphipod> by amphipods {
    init {
        require(size % ROW_SIZE == 0) { "Rooms must contain a multiple of $ROW_SIZE amphipods." }
    }

    val complete: Boolean
        get() {
            val rowCount = size / ROW_SIZE
            val range = 0 until rowCount
            return range.all { getRow(it).isRowComplete() }
        }

    private fun List<Amphipod>.isRowComplete(): Boolean = map(Amphipod::type) == FINISHED_ROW

    fun getRow(row: Int): List<Amphipod> {
        require(row in 0 until size / ROW_SIZE) { "Row out of bounds: $row" }
        val startIndex = row * ROW_SIZE
        return subList(startIndex, startIndex + ROW_SIZE)
    }

    fun getColumn(col: Int): List<Amphipod> {
        require(col in 0 until ROW_SIZE) { "Column out of bounds: $col" }
        return (0 until size step ROW_SIZE).map { get(it + col) }
    }

    private companion object {
        private val FINISHED_ROW = listOf('A', 'B', 'C', 'D')
    }
}
