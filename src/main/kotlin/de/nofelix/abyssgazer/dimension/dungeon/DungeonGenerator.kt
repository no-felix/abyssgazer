package de.nofelix.abyssgazer.dimension.dungeon

import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random
import org.slf4j.LoggerFactory

/** Generates procedural dungeon layouts consisting of rooms and connecting corridors. */
object DungeonGenerator {

    private val logger = LoggerFactory.getLogger(DungeonGenerator::class.java)

    private const val MAX_DUNGEONS = 30

    // MIX_CONSTANT is derived from the golden ratio and is commonly used in hashing algorithms (e.g., splitmix64)
    // to improve bit dispersion and reduce collisions.
    private const val MIX_CONSTANT: Long = -0x61C8864680B583EBL

    private val DEFAULT_CONFIG = GenerationConfig()

    /**
     * Tunable parameters for the dungeon layout algorithm.
     *
     * @property layoutWidth Width of the virtual dungeon grid that we try to populate with rooms.
     * @property layoutHeight Height of the virtual dungeon grid.
     * @property minRooms Minimum number of rooms we aim to place before giving up.
     * @property maxRooms Hard upper bound for rooms in a single layout.
     * @property roomSizeRange Inclusive range from which both room width and height are sampled.
     * @property corridorWidthRange Random range for the corridor width between connected rooms.
     * @property minimumRoomGap Padding in tiles that we keep between rooms to avoid touching walls.
     * @property maxPlacementAttempts How many attempts we spend trying to place non-overlapping rooms.
     * @property extraConnectionChance Probability that we punch an additional corridor between non-MST neighbors.
     * @property extraConnectionSamplesPerRoom Number of random neighbors we try for the extra-connection dice roll.
     */
    data class GenerationConfig(
        val layoutWidth: Int = 128,
        val layoutHeight: Int = 128,
        val minRooms: Int = 6,
        val maxRooms: Int = 10,
        val roomSizeRange: IntRange = 6..14,
        val corridorWidthRange: IntRange = 2..3,
        val minimumRoomGap: Int = 2,
        val maxPlacementAttempts: Int = 120,
        val extraConnectionChance: Double = 0.25,
        val extraConnectionSamplesPerRoom: Int = 4
    ) {
        init {
            require(minRooms > 1) { "A dungeon requires at least two rooms." }
            require(maxRooms >= minRooms) { "maxRooms must be >= minRooms" }
            require(roomSizeRange.first >= 3) { "Rooms must have a reasonable size." }
            require(corridorWidthRange.first >= 1) { "Corridor width cannot be zero." }
            require(extraConnectionChance in 0.0..1.0) {
                "Connection chance must be a probability."
            }
            require(extraConnectionSamplesPerRoom >= 0) {
                "extraConnectionSamplesPerRoom cannot be negative."
            }
        }

        fun normalizedRoomWidthRange(): IntRange = roomSizeRange.coerceWithin(3..layoutWidth)
        fun normalizedRoomHeightRange(): IntRange = roomSizeRange.coerceWithin(3..layoutHeight)

        private fun IntRange.coerceWithin(boundary: IntRange): IntRange {
            val start = max(first, boundary.first)
            val end = min(last, boundary.last)
            return if (start <= end) start..end else boundary
        }
    }

    /**
     * Generates a deterministic layout for a single dungeon index.
     *
     * The algorithm is broken down into three phases:
     * 1. Sample a pseudo-random target room count and place axis-aligned rooms without overlaps.
     * 2. Build a minimum spanning tree to guarantee a connected layout and optionally add extra shortcuts.
     * 3. Annotate each room with its neighbors and corridor widths to hand over to the runtime generator.
     *
     * Corridor width and optional extra connections are derived from the provided [GenerationConfig], which
     * means calling this function with the same `(dungeonIndex, baseSeed, config)` triple is reproducible.
     */
    fun generateDungeonLayout(
        dungeonIndex: Int, baseSeed: Long, config: GenerationConfig = DEFAULT_CONFIG
    ): DungeonLayout {
        require(dungeonIndex in 0 until MAX_DUNGEONS) {
            "Dungeon index $dungeonIndex is outside the supported range [0, ${MAX_DUNGEONS - 1}]"
        }
        val mixedSeed = mixSeed(baseSeed, dungeonIndex)
        val random = Random(mixedSeed)

        val targetRoomCount = random.nextInt(config.minRooms, config.maxRooms + 1)
        val roomBounds = generateRoomBounds(random, targetRoomCount, config)
        if (roomBounds.size < config.minRooms) {
            logger.warn(
                "Dungeon #{} generated only {} rooms (requested >= {}) due to placement constraints.",
                dungeonIndex,
                roomBounds.size,
                config.minRooms
            )
        }

        val (connections, adjacency) = connectRooms(roomBounds, random, config)
        val rooms = roomBounds.mapIndexed { id, bounds ->
            DungeonRoom(
                id = id,
                bounds = bounds,
                kind = RoomKind.Standard,
                tags = emptySet(),
                adjacentRoomIds = adjacency[id] ?: emptySet()
            )
        }

        val layout = DungeonLayout(dungeonIndex, mixedSeed, rooms, connections)
        if (!layout.isFullyConnected()) {
            logger.error(
                "Dungeon #{} layout is not fully connected after generation.", dungeonIndex
            )
        }

        logger.debug(
            "Generated dungeon #{} with {} rooms, {} connections (seed={}).",
            dungeonIndex,
            rooms.size,
            connections.size,
            mixedSeed
        )
        return layout
    }

    /** Produces several dungeon layouts in sequence. Useful for batch generation at startup. */
    fun generateDungeonLayouts(
        count: Int, baseSeed: Long, config: GenerationConfig = DEFAULT_CONFIG
    ): List<DungeonLayout> {
        val clamped = count.coerceIn(0, MAX_DUNGEONS)
        if (count != clamped) {
            logger.warn(
                "Requested {} dungeons but generation is limited to {} for performance.", count, clamped
            )
        }
        return (0 until clamped).map { index -> generateDungeonLayout(index, baseSeed, config) }
    }

    /** Debug helper that prints a dungeon layout to the log output for inspection. */
    fun debugLogDungeonLayout(
        dungeonIndex: Int, baseSeed: Long, config: GenerationConfig = DEFAULT_CONFIG
    ) {
        val layout = generateDungeonLayout(dungeonIndex, baseSeed, config)
        logger.info("Dungeon #{} layout summary:", dungeonIndex)
        layout.rooms.forEach { room ->
            logger.info(
                "- Room {}: pos=({}, {}), size={}x{}, neighbors={}",
                room.id,
                room.bounds.x,
                room.bounds.z,
                room.bounds.width,
                room.bounds.height,
                room.adjacentRoomIds.sorted()
            )
        }
        layout.connections.forEach { connection ->
            logger.info(
                "  Connection {} <-> {} ({})", connection.fromRoomId, connection.toRoomId, connection.type
            )
        }
    }

    /**
     * Places non-overlapping room bounding boxes on the virtual grid.
     *
     * Rooms are sampled with random width/height inside the allowed ranges. We keep retrying until we reach
     * [targetRoomCount] or run out of [GenerationConfig.maxPlacementAttempts]. Placement failure with sparse maps is
     * acceptable: the generation still succeeds, but a warning is emitted so balancing can be adjusted.
     */
    private fun generateRoomBounds(
        random: Random, targetRoomCount: Int, config: GenerationConfig
    ): List<RoomBounds> {
        val rooms = mutableListOf<RoomBounds>()
        var attempts = 0
        while (rooms.size < targetRoomCount && attempts < config.maxPlacementAttempts) {
            attempts++
            val width = random.nextInt(
                config.normalizedRoomWidthRange().first, config.normalizedRoomWidthRange().last + 1
            )
            val height = random.nextInt(
                config.normalizedRoomHeightRange().first, config.normalizedRoomHeightRange().last + 1
            )
            val maxX = config.layoutWidth - width
            val maxZ = config.layoutHeight - height
            if (maxX < 0 || maxZ < 0) continue
            val x = random.nextInt(maxX + 1)
            val z = random.nextInt(maxZ + 1)
            val candidate = RoomBounds(x, z, width, height)
            if (rooms.none { it.intersects(candidate, padding = config.minimumRoomGap) }) {
                rooms.add(candidate)
            }
        }
        return rooms
    }

    /**
     * Connects the room graph with corridors and returns both explicit edges and adjacency lookup.
     *
     * We first compute a minimum spanning tree (Prim's algorithm) to guarantee reachability, then optionally create
     * additional shortcuts between random room pairs based on [GenerationConfig.extraConnectionChance]. Corridor
     * width is sampled per edge inside [GenerationConfig.corridorWidthRange].
     */
    private fun connectRooms(
        rooms: List<RoomBounds>, random: Random, config: GenerationConfig
    ): Pair<List<RoomConnection>, Map<Int, Set<Int>>> {
        if (rooms.size <= 1) {
            return emptyList<RoomConnection>() to emptyMap()
        }

        val requiredEdges = buildMinimumSpanningSet(rooms)
        val connections = requiredEdges.map { (a, b) ->
            RoomConnection(
                fromRoomId = a, toRoomId = b, corridorWidth = random.nextInt(
                    config.corridorWidthRange.first, config.corridorWidthRange.last + 1
                )
            )
        }.toMutableList()

        val existingPairs = connections.map { orderedPair(it.fromRoomId, it.toRoomId) }.toMutableSet()
        if (rooms.size > 1 && config.extraConnectionSamplesPerRoom > 0 && config.extraConnectionChance > 0.0) {
            for (i in rooms.indices) {
                var samples = 0
                val maxSamples = min(config.extraConnectionSamplesPerRoom, rooms.size - 1)
                while (samples < maxSamples) {
                    samples++
                    val j = pickDifferentRoomIndex(random, currentIndex = i, totalRooms = rooms.size)
                    val pair = orderedPair(i, j)
                    if (pair in existingPairs) continue
                    if (random.nextDouble() < config.extraConnectionChance) {
                        connections.add(
                            RoomConnection(
                                fromRoomId = i,
                                toRoomId = j,
                                corridorWidth = random.nextInt(
                                    config.corridorWidthRange.first,
                                    config.corridorWidthRange.last + 1
                                )
                            )
                        )
                        existingPairs.add(pair)
                    }
                }
            }
        }

        val adjacency = mutableMapOf<Int, MutableSet<Int>>()
        connections.forEach { connection ->
            adjacency.getOrPut(connection.fromRoomId) { mutableSetOf() }.add(connection.toRoomId)
            adjacency.getOrPut(connection.toRoomId) { mutableSetOf() }.add(connection.fromRoomId)
        }
        return connections to adjacency.mapValues { it.value.toSet() }
    }

    /**
     * Builds a minimum spanning tree over the room centroids using Prim's algorithm.
     *
     * The resulting edge list forms the backbone for corridor placement and ensures the graph stays connected even if
     * no optional shortcuts are added later.
     */
    private fun buildMinimumSpanningSet(rooms: List<RoomBounds>): List<Pair<Int, Int>> {
        val size = rooms.size
        if (size <= 1) return emptyList()

        val inTree = BooleanArray(size)
        val bestDistance = IntArray(size) { Int.MAX_VALUE }
        val parent = IntArray(size) { -1 }
        val edges = ArrayList<Pair<Int, Int>>(size - 1)

        inTree[0] = true
        for (i in 1 until size) {
            bestDistance[i] = rooms[0].manhattanDistance(rooms[i])
            parent[i] = 0
        }

        for (step in 1 until size) {
            var nextIndex = -1
            var minDistance = Int.MAX_VALUE
            for (i in 0 until size) {
                if (!inTree[i] && bestDistance[i] < minDistance) {
                    minDistance = bestDistance[i]
                    nextIndex = i
                }
            }
            if (nextIndex == -1) break

            inTree[nextIndex] = true
            val parentIndex = parent[nextIndex]
            if (parentIndex != -1) {
                edges.add(parentIndex to nextIndex)
            }

            for (i in 0 until size) {
                if (inTree[i]) continue
                val distance = rooms[nextIndex].manhattanDistance(rooms[i])
                if (distance < bestDistance[i]) {
                    bestDistance[i] = distance
                    parent[i] = nextIndex
                }
            }
        }

        return edges
    }

    /**
     * Returns a normalized Pair of room ids (lowest first), suitable for use as a key in sets or maps
     * to avoid directionality issues when tracking connections.
     */
    private fun orderedPair(a: Int, b: Int): Pair<Int, Int> = if (a < b) a to b else b to a

    /** Picks a random room index that is guaranteed to be different from [currentIndex]. */
    private fun pickDifferentRoomIndex(random: Random, currentIndex: Int, totalRooms: Int): Int {
        val rawIndex = random.nextInt(totalRooms - 1)
        return if (rawIndex >= currentIndex) rawIndex + 1 else rawIndex
    }

    /**
     * Mixes the base seed with the dungeon index to produce a deterministic, well-dispersed seed.
     *
     * The approach ensures that each dungeon index produces a unique and reproducible seed, minimizing
     * correlations and collisions between generated layouts.
     *
     * @param seed The base world or session seed.
     * @param dungeonIndex The index of the dungeon to differentiate layouts.
     * @return A mixed seed suitable for deterministic random generation.
     */
    private fun mixSeed(seed: Long, dungeonIndex: Int): Long {
        var mixed = seed xor (dungeonIndex.toLong() shl 32)
        mixed = mixed xor (mixed ushr 33)
        mixed = mixed xor MIX_CONSTANT
        mixed = mixed xor (mixed ushr 29)
        return mixed
    }
}
