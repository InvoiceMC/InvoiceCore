package me.outspending.invoice.core.listeners.types

import me.outspending.invoice.core.parse
import me.outspending.invoice.core.scoreboard.ScoreboardHandler
import net.kyori.adventure.title.Title
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerLevelChangeEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class PlayerListeners : Listener {
    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player
        val playerName = player.name

        val joinMessage = if (!player.hasPlayedBefore()) {
            val online = Bukkit.getOfflinePlayers().size
            "<main>$playerName <white>has joined for the first time! <gray>[<second>#$online<gray>]".parse()
        } else {
            "<gray>$playerName has joined!".parse()
        }
        event.joinMessage(joinMessage)

        player.addPotionEffect(
            PotionEffect(PotionEffectType.NIGHT_VISION, Int.MAX_VALUE, 1, false, false)
        )

        ScoreboardHandler.create(player)
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        val player = event.player

        event.quitMessage(null)

        ScoreboardHandler.delete(player)
    }

    @EventHandler
    fun onLevelup(event: PlayerLevelChangeEvent) {
        val player = event.player
        val old = event.oldLevel
        val new = event.newLevel

        if (new > old) {
            val title = Title.title(
                "<main><b>ʟᴇᴠᴇʟᴜᴘ</b>".parse(),
                "<main>$old <gray>➲ <second>$new".parse()
            )
            player.showTitle(title)
        }
    }
}