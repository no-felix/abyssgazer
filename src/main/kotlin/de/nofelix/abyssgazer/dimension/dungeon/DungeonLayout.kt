package de.nofelix.abyssgazer.dimension.dungeon

import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

/**
 * Immutable description of a single dungeon layout consisting of rooms and directed connections.
 *
 * The layout is purely data-oriented so the runtime can project rooms into world coordinates, spawn mobs, or decorate
 * them without recomputing the underlying graph. Generation happens offline via [DungeonGenerator].
 */
data class DungeonLayout(
    val dungeonIndex: Int,
    val seed: Long,
    val rooms: List<DungeonRoom>,
    val connections: List<RoomConnection>
) {
    /** Verifies that all rooms are reachable from the first one by traversing the connections graph. */
    fun isFullyConnected(): Boolean {
        if (rooms.isEmpty()) return true
        val roomIds = rooms.mapTo(mutableSetOf()) { it.id }
        val adjacency = mutableMapOf<Int, MutableSet<Int>>()
        connections.forEach { connection ->
            if (connection.fromRoomId in roomIds && connection.toRoomId in roomIds) {
                adjacency.getOrPut(connection.fromRoomId) { mutableSetOf() }.add(connection.toRoomId)
                adjacency.getOrPut(connection.toRoomId) { mutableSetOf() }.add(connection.fromRoomId)
            }
        }
        val visited = mutableSetOf<Int>()
        val stack = ArrayDeque<Int>()
        stack.add(rooms.first().id)
        while (stack.isNotEmpty()) {
            val current = stack.removeLast()
            if (!visited.add(current)) continue
            adjacency[current]?.forEach { neighbor ->
                if (neighbor !in visited) {
                    stack.addLast(neighbor)
                }
            }
        }
        return visited.size == rooms.size
    }
}

/** Basic description of a room in the dungeon grid. */
data class DungeonRoom(
    val id: Int,
    val bounds: RoomBounds,
    val kind: RoomKind = RoomKind.Standard,
    val tags: Set<String> = emptySet(),
    val adjacentRoomIds: Set<Int> = emptySet()
) {
    /** Convenient accessor that reports the center point of the room for distance calculations. */
    val center: GridPoint = GridPoint(bounds.x + bounds.width / 2.0, bounds.z + bounds.height / 2.0)

    /** Derived value representing the room surface area. */
    val area: Int
        get() = bounds.width * bounds.height
}

/**
 * Encapsulates an undirected connection between two rooms.
 *
 * Corridors are stored with a `from`/`to` ordering, but downstream logic treats them as undirected by looking at both
 * ids. The [corridorWidth] provides the generator with detail information for carving the passage.
 */
data class RoomConnection(
    val fromRoomId: Int, val toRoomId: Int, val corridorWidth: Int, val type: ConnectionType = ConnectionType.Corridor
)

/** Axis aligned bounding box describing room placement within the dungeon grid. */
data class RoomBounds(val x: Int, val z: Int, val width: Int, val height: Int) {
    /**
     * Returns true if two rooms overlap after accounting for [padding], used to guarantee wall spacing.
     * When [padding] is 0, checks for direct overlap; negative values reduce spacing, positive values increase it.
     */
    fun intersects(other: RoomBounds, padding: Int = 0): Boolean {
        val noOverlapX = x + width + padding <= other.x || other.x + other.width + padding <= x
        val noOverlapZ = z + height + padding <= other.z || other.z + other.height + padding <= z
        return !(noOverlapX || noOverlapZ)
    }

    /** Merges two bounds into their outer hull. Handy for future features like multi-room arenas. */
    fun merge(other: RoomBounds): RoomBounds {
        val minX = min(x, other.x)
        val minZ = min(z, other.z)
        val maxX = max(x + width, other.x + other.width)
        val maxZ = max(z + height, other.z + other.height)
        return RoomBounds(minX, minZ, maxX - minX, maxZ - minZ)
    }

    /** Manhattan distance describing how far two rooms sit apart when carving a primary corridor. */
    fun manhattanDistance(other: RoomBounds): Int {
        val dx = when {
            other.x > x + width -> other.x - (x + width)
            x > other.x + other.width -> x - (other.x + other.width)
            else -> 0
        }
        val dz = when {
            other.z > z + height -> other.z - (z + height)
            z > other.z + other.height -> z - (other.z + other.height)
            else -> 0
        }
        return abs(dx) + abs(dz)
    }
}

/** TODO: Expand with concrete room types (Boss, Treasure, Secret, etc.). */
sealed interface RoomKind {
    /** Default catch-all room type. */
    data object Standard : RoomKind
}

/** TODO: Add additional connection categories (locked door, teleport, etc.). */
sealed interface ConnectionType {
    data object Corridor : ConnectionType
}

/** Small helper structure that represents a 2D lattice point in the dungeon plane. */
data class GridPoint(val x: Double, val z: Double)
