package aoc.day19

import aoc.*

/**
 * @author Kris | 19/12/2021
 */
object Day19 : Puzzle<List<Scan>, Int>(19) {
    private val VALID_COORDINATE_REGEX = Regex("""(-?\d+),(-?\d+),(-?\d+)""")
    private const val BEACON_THRESHOLD = 12

    override fun Sequence<String>.parse(): List<Scan> = chunkedBy(String::isEmpty).map { groupOfLines ->
        val beacons = groupOfLines.mapNotNull { line ->
            val match = VALID_COORDINATE_REGEX.matchEntire(line) ?: return@mapNotNull null
            val (x, y, z) = match.groupValues.drop(1).map(String::toInt)
            Vec3i(x, y, z)
        }
        Scan(beacons)
    }

    private fun List<Scan>.getReport(): ScanReport {
        /* The scan result we are matching against. */
        var base = first()
        /* The remaining scans that we need to match up. */
        val remainingScans = drop(1).toMutableList()
        val positions = mutableListOf<Vec3i>()
        while (remainingScans.isNotEmpty()) {
            /* Match as many as we can in one go, as the bigger the identified set gets, the longer the computations take. */
            val matches = remainingScans.mapNotNull { scan ->
                val result = base.identifyScanner(scan) ?: return@mapNotNull null
                scan to result
            }
            matches.forEach { (scan, result) ->
                val (position, beacons) = result
                remainingScans -= scan
                positions += position
                base = Scan(base + beacons)
            }
        }
        return ScanReport(positions, base.toSet())
    }

    override fun List<Scan>.solvePartOne(): Int = getReport().beacons.size

    override fun List<Scan>.solvePartTwo(): Int = with(getReport()) {
        scannerPositions.maxOf { vec -> scannerPositions.maxOf(vec::manhattanDistance) }
    }

    private fun Scan.identifyScanner(otherScan: Scan): IdentifiedScanner? =
        variations.permutationSearch(otherScan.variations) { base, other ->
            val vector = base.offsetVector(other) ?: return@permutationSearch null
            val matches = other.map { it - vector }
            return IdentifiedScanner(vector, matches)
        }

    private fun Scan.offsetVector(other: Scan): Vec3i? {
        val distanceToCount = mutableMapOf<Int, Int>()
        return permutationSearch(other) { beacon, otherBeacon ->
            val distance = beacon.squaredDistanceOfDifference(otherBeacon)
            val prevCount = distanceToCount.increment(distance, 1) ?: return@permutationSearch null
            if (prevCount >= BEACON_THRESHOLD - 1) otherBeacon - beacon else null
        }
    }
}

data class Scan(private val scrambledBeacons: List<Vec3i>) : List<Vec3i> by scrambledBeacons {
    val variations by lazy {
        buildList {
            for (orientation in 0 until ORIENTATIONS) {
                for (rotation in 0 until ROTATIONS) {
                    add(Scan(scrambledBeacons.map { it.orientate(orientation).rotate(rotation) }))
                }
            }
        }
    }

    companion object {
        private const val ROTATIONS = 4
        private const val ORIENTATIONS = 6
    }
}

data class ScanReport(val scannerPositions: List<Vec3i>, val beacons: Set<Vec3i>)
data class IdentifiedScanner(val position: Vec3i, val visibleBeacons: List<Vec3i>)
