package de.nofelix.abyssgazer.role.attribute.player.mana

import net.minecraft.entity.Entity
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.RegistryWrapper
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent


class ManaComponentManager(private val provider: Entity) : ManaComponent, AutoSyncedComponent {
    val START_VALUE = 100

    override var currentMana: Int = START_VALUE

    override var maxMana: Int = START_VALUE

    override fun recoverMana(amount: Int) {
        if (amount >= 0) this.currentMana = (this.currentMana + amount).coerceAtMost(this.maxMana);
    }

    override fun consumeMana(amount: Int) {
        if (amount >= 0) this.currentMana = (this.currentMana - amount).coerceAtLeast(0);
    }

    override fun writeToNbt(nbt: NbtCompound, reg: RegistryWrapper.WrapperLookup) {
        val saveMaxMana = this.maxMana.coerceAtLeast(0)
        val saveCurrentMana = this.currentMana.coerceIn(0, this.maxMana)
        nbt.putInt("CurrentMana", saveCurrentMana)
        nbt.putInt("MaxMana",saveMaxMana)
    }

    override fun readFromNbt(nbt: NbtCompound, reg: RegistryWrapper.WrapperLookup) {
        val loadedCurrentMana = if (nbt.contains("CurrentMana")) nbt.getInt("CurrentMana") else this.currentMana
        val loadedMaxMana = if (nbt.contains("MaxMana")) nbt.getInt("MaxMana") else this.maxMana
        this.maxMana = loadedMaxMana.coerceAtLeast(0)
        this.currentMana = loadedCurrentMana.coerceIn(0, this.maxMana)
    }
}