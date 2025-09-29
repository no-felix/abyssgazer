package de.nofelix

import de.nofelix.abyssgazer.dimension.dungeon.DungeonGenerator
import net.fabricmc.api.ModInitializer
import net.fabricmc.loader.api.FabricLoader
import org.slf4j.LoggerFactory

object AbyssGazer : ModInitializer {
    private val logger = LoggerFactory.getLogger("abyssgazer")

    override fun onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.
        logger.info("Hello Fabric world!")

        if (FabricLoader.getInstance().isDevelopmentEnvironment) {
            // TODO: Remove this when Chunk Generation is implemented.
            // This is just to generate a sample dungeon layout for testing purposes.
            logger.info("Generating sample dungeon layout...")
            DungeonGenerator.debugLogDungeonLayout(dungeonIndex = 0, baseSeed = 0xAB5510L)
        }
    }
}