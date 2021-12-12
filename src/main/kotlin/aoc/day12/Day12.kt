package aoc.day12

import aoc.Puzzle

/**
 * @author Kris | 12/12/2021
 */
object Day12 : Puzzle<NodeTree, Int>(12) {
    override fun Sequence<String>.parse(): NodeTree {
        val connections = map {
            val (from, to) = it.split('-')
            val fromNode = Node(from, from.nodeType)
            val toNode = Node(to, to.nodeType)
            fromNode to toNode
        }
        val nodes = mutableMapOf<Node, MutableList<Node>>()
        connections.forEach { pair ->
            if (pair.first.type != NodeType.End && pair.second.type != NodeType.Start) nodes.getOrPut(pair.first, ::mutableListOf).add(pair.second)
            if (pair.second.type != NodeType.End && pair.first.type != NodeType.Start) nodes.getOrPut(pair.second, ::mutableListOf).add(pair.first)
        }
        return nodes
    }

    private val String.nodeType get() = when {
        this == "start" -> NodeType.Start
        this == "end" -> NodeType.End
        this.first().isLowerCase() -> NodeType.Small
        else -> NodeType.Large
    }

    private val NodeTree.start get() = keys.single { it.type == NodeType.Start }

    private fun NodeTree.visit(
        node: Node,
        path: Path,
        unavailableNodes: Set<Node>,
        currentPaths: MutableList<Path>,
        smallUnusedNode: Node?
    ) {
        val nextPath = Path(path + node)
        val unvisitedSmallNode = node.type == NodeType.Small && node !in unavailableNodes
        val nextUnavailableNodes = if (unvisitedSmallNode) (unavailableNodes + node) else unavailableNodes
        val nextExtraSmallNode = smallUnusedNode ?: if (node.type == NodeType.Small && !unvisitedSmallNode) node else null
        if (node.type == NodeType.End) {
            currentPaths += nextPath
            return
        }
        val connectedNodes = get(node) ?: return
        for (nextNode in connectedNodes) {
            if (nextExtraSmallNode != null && nextNode in unavailableNodes) continue
            visit(nextNode, nextPath, nextUnavailableNodes, currentPaths, nextExtraSmallNode)
        }
    }

    private fun NodeTree.countUniquePaths(smallUnusedNode: Node?): Int {
        val paths = mutableListOf<Path>()
        visit(start, Path(), setOf(), paths, smallUnusedNode)
        return paths.size
    }

    override fun NodeTree.solvePartOne(): Int = countUniquePaths(/* Set the unused node to start as part one can't visit anything twice */ start)

    override fun NodeTree.solvePartTwo(): Int = countUniquePaths(null)
}
private typealias NodeTree = Map<Node, List<Node>>
data class Node(val name: String, val type: NodeType)
data class Path(val nodes: List<Node> = emptyList()) : List<Node> by nodes
enum class NodeType {
    Start,
    Large,
    Small,
    End,
}
