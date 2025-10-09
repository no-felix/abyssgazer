package de.nofelix.abyssgazer.commands

import com.mojang.brigadier.Command
import de.nofelix.abyssgazer.role.attribute.player.mana.ManaComponentInitializer
import de.nofelix.abyssgazer.role.stat.CharStatInitializer
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.Text.literal

class BasicCommands {
    companion object {

        @JvmStatic
        fun register() {
            CommandRegistrationCallback.EVENT.register { dispatcher, _, _ ->
                val manaNode = CommandManager
                    .literal("mana")
                    .executes(CustomManaCommand)
                    .build()

                val statsNode = CommandManager
                    .literal("stats")
                    .executes(CustomStatCommand)
                    .build()

                dispatcher.root.addChild(manaNode)
                dispatcher.root.addChild(statsNode)
            }
        }


        val CustomManaCommand: Command<ServerCommandSource> = Command { context ->
            val player = context?.source?.player ?: run {
                context?.source?.sendFeedback({ literal("This command must be run by a player.") }, false)
                0
            }
            val mana = ManaComponentInitializer.MANA[player].currentMana
            context.source?.sendFeedback({ literal("Mana: $mana") }, false)
            Command.SINGLE_SUCCESS
        }

        val CustomStatCommand: Command<ServerCommandSource> = Command { context ->
            val player = context?.source?.player ?: run {
                context?.source?.sendFeedback({ literal("This command must be run by a player.") }, false)
                0
            }
            val stats = CharStatInitializer.STATS[player].stats
            context.source?.sendFeedback({ literal("Stats: $stats") }, false)
            Command.SINGLE_SUCCESS
        }
    }
}
