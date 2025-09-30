package de.nofelix.abyssgazer.`character-statistic`

import net.minecraft.entity.player.PlayerEntity

enum class StatType { HP, STRENGTH, DEXTERITY, INTELLIGENCE, FAITH }

data class Stats(
    val stats: MutableMap<StatType, Int> = mutableMapOf(
        StatType.HP to 10,
        StatType.STRENGTH to 10,
        StatType.DEXTERITY to 10,
        StatType.INTELLIGENCE to 10,
        StatType.FAITH to 10,
    )
)

class CharacterStats(
    player: PlayerEntity,
) {
    var xp: Int = 0;
    var level: Int = 1;
    var levelUpPoints: Int = 5;
    val stats: Stats = Stats()
    val name: String = player.name.string;

    //TODO: impl character stat logic here
}

