package me.outspending.invoice.core.listeners

import com.github.shynixn.mccoroutine.bukkit.registerSuspendingEvents
import me.outspending.invoice.core.core
import me.outspending.invoice.core.data.player.PlayerDataListeners
import me.outspending.invoice.core.interfaces.Registrable
import me.outspending.invoice.core.listeners.types.ChatListeners
import me.outspending.invoice.core.listeners.types.CommandListeners
import me.outspending.invoice.core.listeners.types.MiscListeners
import me.outspending.invoice.core.listeners.types.PlayerListeners

class ListenerRegistry : Registrable {
    override fun register(vararg packages: String) {
        val pluginManager = core.server.pluginManager

        pluginManager.registerEvents(ChatListeners(), core)
        pluginManager.registerEvents(CommandListeners(), core)
        pluginManager.registerEvents(MiscListeners(), core)

        pluginManager.registerSuspendingEvents(PlayerDataListeners(), core)
        pluginManager.registerEvents(PlayerListeners(), core)
    }
}