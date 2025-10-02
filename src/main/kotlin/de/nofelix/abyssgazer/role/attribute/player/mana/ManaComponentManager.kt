package de.nofelix.abyssgazer.role.attribute.player.mana

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.RegistryWrapper
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
            );

    override fun registerEntityComponentFactories(registry: EntityComponentFactoryRegistry) {
        registry.registerForPlayers(
            MANA,
            { playerEntity -> ManaComponentManager(playerEntity) },
            RespawnCopyStrategy.ALWAYS_COPY
        );
    }
}

class ManaComponentManager(private val player: PlayerEntity) : ManaComponent {
    val START_VALUE = 100;

    override var currentMana: Int = START_VALUE;

    override var maxMana: Int = START_VALUE;

    override fun recoverMana(amount: Int) {
        if (amount >= 0) this.currentMana = (this.currentMana + amount).coerceAtMost(this.maxMana);
    }

    override fun consumeMana(amount: Int) {
        if (amount >= 0) this.currentMana = (this.currentMana - amount).coerceAtLeast(0);
    }

    override fun writeToNbt(nbt: NbtCompound, reg: RegistryWrapper.WrapperLookup) {
        val saveMaxMana = this.maxMana.coerceAtLeast(0);
        val saveCurrentMana = this.currentMana.coerceIn(0, this.maxMana)
        nbt.putInt("CurrentMana", saveCurrentMana);
        nbt.putInt("MaxMana",saveMaxMana);
    }

    override fun readFromNbt(nbt: NbtCompound, reg: RegistryWrapper.WrapperLookup) {
        val loadedCurrentMana = if (nbt.contains("CurrentMana")) nbt.getInt("CurrentMana") else this.currentMana;
        val loadedMaxMana = if (nbt.contains("MaxMana")) nbt.getInt("MaxMana") else this.maxMana;
        this.maxMana = loadedMaxMana.coerceAtLeast(0);
        this.currentMana = loadedCurrentMana.coerceIn(0, this.maxMana);
    }
}