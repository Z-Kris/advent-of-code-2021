package aoc.day12

import aoc.Puzzle

/**
 * @author Kris | 12/12/2021
 */
object Day12 : Puzzle<NodeTree, Int>(12) {
    private const val START_LABEL = "start"
    private const val END_LABEL = "end"
    private const val SUCCESSFUL_PATH = 1
    private const val FAILED_PATH = 0

    @OptIn(ExperimentalStdlibApi::class)
    override fun Sequence<String>.parse(): NodeTree = buildMap<Node, MutableList<Node>> {
        this@parse.forEach {
            val (fromLabel, toLabel) = it.split('-')
            val from = Node(fromLabel, fromLabel.nodeType)
            val to = Node(toLabel, toLabel.nodeType)
            if (isValid(from, to)) getOrPut(from, ::mutableListOf).add(to)
            if (isValid(to, from)) getOrPut(to, ::mutableListOf).add(from)
        }
    }

    private fun isValid(from: Node, to: Node) = from.type != NodeType.End && to.type != NodeType.Start

    private val String.nodeType get() = when {
        this == START_LABEL -> NodeType.Start
        this == END_LABEL -> NodeType.End
        this.first().isLowerCase() -> NodeType.Small
        else -> NodeType.Large
    }

    private val NodeTree.start get() = keys.single { it.type == NodeType.Start }

    private fun NodeTree.visit(
        node: Node,
        path: Path,
        smallUnusedNode: Node?
    ): Int {
        /* If we've reached the end, return here. */
        if (node.type == NodeType.End) return SUCCESSFUL_PATH
        /* If there are no more connected nodes to this node, return here. */
        val connectedNodes = get(node) ?: return FAILED_PATH
        /* Create a new path that's the continuation of previous, plus this node. */
        val nextPath = Path(path + node)
        /* Determine the unused node - if the unused node is already set, do nothing. */
        /* Otherwise, if the type is small, and it's already in the unavailable nodes, set it to this, otherwise none. */
        val nextSmallUnusedNode = smallUnusedNode ?: if (node.type == NodeType.Small && node in path) node else null
        /* Now, compute the sum of all the remaining possible paths from this path onward. */
        return connectedNodes.filterNot { it.type == NodeType.Small && nextSmallUnusedNode != null && it in path }.sumOf { nextNode ->
            visit(nextNode, nextPath, nextSmallUnusedNode)
        }
    }

    override fun NodeTree.solvePartOne(): Int = visit(start, Path(), /* Set the unused node to start as part one can't visit anything twice */ start)
    override fun NodeTree.solvePartTwo(): Int = visit(start, Path(), null)
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
