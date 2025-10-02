package de.nofelix.abyssgazer.role.stat

import org.ladysnake.cca.api.v3.component.Component

interface CharStat : Component {
    val stats: MutableMap<StatType, Int>

}