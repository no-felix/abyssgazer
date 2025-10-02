package de.nofelix.abyssgazer.role.attribute.player.mana

import org.ladysnake.cca.api.v3.component.Component

interface ManaComponent : Component {
    var currentMana: Int;

    var maxMana: Int;

    fun recoverMana(amount: Int);

    fun consumeMana(amount: Int);

}