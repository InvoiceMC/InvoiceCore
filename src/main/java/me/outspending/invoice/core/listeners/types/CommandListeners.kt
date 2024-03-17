package me.outspending.invoice.core.listeners.types

import me.outspending.invoice.core.parse
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerCommandPreprocessEvent

class CommandListeners : Listener {
    private val BLACKLISTED_COMMANDS: List<String> =
        listOf(
            "plugins",
            "pl",
            "bukkit:plugins",
            "help",
            "?",
            "bukkit:help",
            "bukkit:?",
            "tell",
            "me",
            "minecraft:tell",
            "minecraft:me",
            "version",
            "ver",
            "about",
            "bukkit:about",
            "say",
            "icanhasbukkit"
        )

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onCommand(event: PlayerCommandPreprocessEvent) {
        val player: Player = event.player
        val commandName: String = event.message.split(" ")[0].substring(1)

        if (player.hasPermission("core.bypassCommands")) return
        if (BLACKLISTED_COMMANDS.contains(commandName)) {
            event.isCancelled = true
            player.sendMessage("Unknown command. Type \"/help\" for help.".parse())
        }
    }
}