package de.nofelix.abyssgazer.role.stat

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.RegistryWrapper

enum class StatType { HP, STRENGTH, DEXTERITY, INTELLIGENCE, FAITH }

class CharacterStats(player: PlayerEntity) : CharStat {

    companion object {
        val STAT_START_VALUE = 10
        val ZERO_VALUE = 0
        val LVL_POINTS_VALUE = 5
        val LVL_START_VALUE = 1
    }

    var name: String = player.name.string;
    var xp: Int = ZERO_VALUE;
    var level: Int = LVL_START_VALUE;
    var levelUpPoints: Int = LVL_POINTS_VALUE;
    override val stats: MutableMap<StatType, Int> = mutableMapOf(
        StatType.HP to STAT_START_VALUE,
        StatType.STRENGTH to STAT_START_VALUE,
        StatType.DEXTERITY to STAT_START_VALUE,
        StatType.INTELLIGENCE to STAT_START_VALUE,
        StatType.FAITH to STAT_START_VALUE,
    )

    override fun writeToNbt(nbt: NbtCompound, reg: RegistryWrapper.WrapperLookup) {
        nbt.putString("Name", this.name)
        nbt.putInt("XP", this.xp)
        nbt.putInt("Level", this.level)
        nbt.putInt("LevelPoints", this.levelUpPoints)
        nbt.putInt("HP", stats.getValue(StatType.HP))
        nbt.putInt("Str", stats.getValue(StatType.STRENGTH))
        nbt.putInt("Dex", stats.getValue(StatType.DEXTERITY))
        nbt.putInt("Int", stats.getValue(StatType.INTELLIGENCE))
        nbt.putInt("Faith", stats.getValue(StatType.FAITH))
    }

    override fun readFromNbt(nbt: NbtCompound, reg: RegistryWrapper.WrapperLookup) {
        this.name = if (nbt.contains("Name")) nbt.getString("Name") else this.name
        this.xp = if (nbt.contains("XP")) nbt.getInt("XP").coerceAtLeast(ZERO_VALUE) else ZERO_VALUE
        this.level = if (nbt.contains("Level")) nbt.getInt("Level").coerceAtLeast(LVL_START_VALUE) else LVL_START_VALUE
        this.levelUpPoints =
            if (nbt.contains("LevelPoints")) nbt.getInt("LevelPoints").coerceAtLeast(ZERO_VALUE) else ZERO_VALUE
        val loadedStats: Map<StatType, Int> = mutableMapOf(
            StatType.HP to nbt.getInt("HP").coerceAtLeast(STAT_START_VALUE),
            StatType.STRENGTH to nbt.getInt("Str").coerceAtLeast(STAT_START_VALUE),
            StatType.DEXTERITY to nbt.getInt("Dex").coerceAtLeast(STAT_START_VALUE),
            StatType.INTELLIGENCE to nbt.getInt("Int").coerceAtLeast(STAT_START_VALUE),
            StatType.FAITH to nbt.getInt("Faith").coerceAtLeast(STAT_START_VALUE),
        )
        this.stats.putAll(loadedStats)
    }

    //TODO: impl character stat logic here
}

