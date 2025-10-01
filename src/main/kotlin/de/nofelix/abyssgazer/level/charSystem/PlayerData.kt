package de.nofelix.abyssgazer.level.charSystem

interface PlayerData {
    /**
     * Holds player-specific data such as health, strength, experience points, level...
     */

    fun setHealth(health: Int) {}
    fun getHealth(): Int;

    fun setStrength(strength: Int) {};
    fun getStrength(): Int;

    fun setDexterity(dexterity: Int) {};
    fun getDexterity(): Int;

    fun setIntelligence(intelligence: Int) {};
    fun getIntelligence(): Int;

    fun setFaith(faith: Int) {};
    fun getFaith(): Int;

    fun setXp(xp: Int) {};
    fun getXp(): Int;

    fun setLevel(level: Int) {};
    fun getLevel(): Int;

    fun setSkillPoints(skillPoints: Int) {};
    fun getSkillPoints(): Int;

}