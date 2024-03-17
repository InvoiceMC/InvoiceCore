package me.outspending.invoice.core

import me.outspending.invoice.core.interfaces.Registrable
import me.outspending.invoice.core.listeners.ListenerRegistry

enum class RegistryType(private val registry: Registrable, private vararg val packages: String) {
    LISTENERS(ListenerRegistry(), "me.outspending.invoice.core.listeners.types");

    fun register() = registry.register(*packages)

    companion object {
        fun registerAll() = entries.forEach { it.register() }
    }
}