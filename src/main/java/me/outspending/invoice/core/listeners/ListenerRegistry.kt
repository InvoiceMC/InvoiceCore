package me.outspending.invoice.core.listeners

import com.github.shynixn.mccoroutine.bukkit.registerSuspendingEvents
import me.outspending.invoice.core.InvoiceCore
import me.outspending.invoice.core.data.player.PlayerDataListeners
import me.outspending.invoice.core.interfaces.Registrable
import me.outspending.invoice.core.listeners.types.ChatListeners
import me.outspending.invoice.core.listeners.types.CommandListeners
import me.outspending.invoice.core.listeners.types.MiscListeners
import me.outspending.invoice.core.listeners.types.PlayerListeners

class ListenerRegistry : Registrable {
    override fun register(vararg packages: String) {
        val instance = InvoiceCore.instance
        val pluginManager = instance.server.pluginManager

        pluginManager.registerEvents(ChatListeners(), instance)
        pluginManager.registerEvents(CommandListeners(), instance)
        pluginManager.registerEvents(MiscListeners(), instance)

        pluginManager.registerSuspendingEvents(PlayerDataListeners(), instance)
        pluginManager.registerEvents(PlayerListeners(), instance)
    }
}