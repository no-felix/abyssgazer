package de.nofelix.abyssgazer.role.stat

import org.ladysnake.cca.api.v3.component.ComponentV3

interface CharStat : ComponentV3 {
    val stats: MutableMap<StatType, Int>
}