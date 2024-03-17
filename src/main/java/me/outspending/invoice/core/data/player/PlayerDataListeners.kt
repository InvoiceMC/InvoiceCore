package me.outspending.invoice.core.data.player

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerLoginEvent
import org.bukkit.event.player.PlayerQuitEvent

class PlayerDataListeners : Listener {
    @EventHandler
    suspend fun onPlayerLogin(event: PlayerLoginEvent) {
        playerDataManager.loadData(event.player)
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        playerDataManager.saveData(event.player)
    }
}