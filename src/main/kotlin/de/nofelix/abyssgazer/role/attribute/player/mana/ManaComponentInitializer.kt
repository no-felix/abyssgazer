package de.nofelix.abyssgazer.role.attribute.player.mana

import net.minecraft.util.Identifier
import org.ladysnake.cca.api.v3.component.ComponentKey
import org.ladysnake.cca.api.v3.component.ComponentRegistryV3
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer
import org.ladysnake.cca.api.v3.entity.RespawnCopyStrategy

object ManaComponentInitializer : EntityComponentInitializer {

    val MANA: ComponentKey<ManaComponent> =
        ComponentRegistryV3.INSTANCE
            .getOrCreate(
                Identifier.of("abyssgazer", "mana"),
                ManaComponent::class.java
            )

    override fun registerEntityComponentFactories(registry: EntityComponentFactoryRegistry) {
        registry.registerForPlayers(
            MANA,
            { playerEntity -> ManaComponentManager(playerEntity) },
            RespawnCopyStrategy.INVENTORY
        )
    }
}