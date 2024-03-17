package me.outspending.invoice.core.listeners.types

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.FoodLevelChangeEvent

class MiscListeners : Listener {
    @EventHandler fun onDamage(event: EntityDamageEvent) = run { event.isCancelled = true }
    @EventHandler fun onFoodLevelChange(event: FoodLevelChangeEvent) = run { event.isCancelled = true }
}