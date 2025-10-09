package de.nofelix.abyssgazer.role.attribute.player.mana

import org.ladysnake.cca.api.v3.component.ComponentV3

interface ManaComponent : ComponentV3 {
    var currentMana: Int

    var maxMana: Int

    fun recoverMana(amount: Int)

    fun consumeMana(amount: Int)
}