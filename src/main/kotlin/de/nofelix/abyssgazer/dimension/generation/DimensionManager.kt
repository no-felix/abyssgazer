package de.nofelix.abyssgazer.dimension.generation

import de.nofelix.abyssgazer.dimension.dungeon.DungeonGenerator
import de.nofelix.abyssgazer.dimension.dungeon.DungeonLayout

/** Coordinates the creation of dungeon layouts and layer metadata for the Abyss dimension. */
object DimensionManager {

    private const val DEFAULT_DUNGEON_COUNT = 12

    /**
     * Creates dungeon layouts for the dimension lifecycle.
     *
     * TODO: Hook the generated layouts into actual chunk population.
     */
    fun prepareDungeonLayouts(count: Int = DEFAULT_DUNGEON_COUNT, seed: Long): List<DungeonLayout> {
        return DungeonGenerator.generateDungeonLayouts(count, seed)
    }
}
