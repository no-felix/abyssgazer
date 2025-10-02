package de.nofelix.abyssgazer.role

import com.mojang.brigadier.context.CommandContext
import de.nofelix.abyssgazer.role.attribute.player.mana.ManaComponentInitializer
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.Text

object AbyssgazerCommands : ModInitializer {
    override fun onInitialize() {
        CommandRegistrationCallback.EVENT.register { dispatcher, _, _ ->
            dispatcher.register(
                CommandManager.literal("mana")
                    .executes { ctx: CommandContext<ServerCommandSource> ->
                        val player = ctx.source.player
                        if (player == null) {
                            ctx.source.sendError(Text.literal("Nur Spieler können diesen Befehl ausführen."))
                            return@executes 0
                        }
                        val mana = ManaComponentInitializer.MANA.get(player)
                        player.sendMessage(Text.literal("Mana: ${mana.currentMana}/${mana.maxMana}"))
                        1
                    }
            )
        }
    }
}