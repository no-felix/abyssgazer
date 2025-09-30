package de.nofelix.abyssgazer.`character-statistic`

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
    val name : String,
    var xp: Int,
    var level: Int,
    var levelUpPoints: Int,
    val stats: Stats = Stats()
){
    //TODO: impl character stat logic here
}

