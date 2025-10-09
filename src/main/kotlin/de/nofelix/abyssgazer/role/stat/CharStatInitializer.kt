package de.nofelix.abyssgazer.role.stat

import net.minecraft.util.Identifier
import org.ladysnake.cca.api.v3.component.ComponentKey
import org.ladysnake.cca.api.v3.component.ComponentRegistryV3
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer
import org.ladysnake.cca.api.v3.entity.RespawnCopyStrategy

object CharStatInitializer : EntityComponentInitializer {

    val STATS: ComponentKey<CharStat> =
        ComponentRegistryV3.INSTANCE
            .getOrCreate(
                Identifier.of("abyssgazer", "stats"),
                CharStat::class.java
            )

    override fun registerEntityComponentFactories(registry: EntityComponentFactoryRegistry) {
        registry.registerForPlayers(
            STATS,
            {playerEntity -> CharacterStats(playerEntity)},
            RespawnCopyStrategy.INVENTORY
        )
    }
}