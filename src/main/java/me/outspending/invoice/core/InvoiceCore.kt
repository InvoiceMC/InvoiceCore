package me.outspending.invoice.core

import com.github.shynixn.mccoroutine.bukkit.launch
import me.outspending.invoice.core.config.setupConfig
import me.outspending.invoice.core.data.DatabaseManager
import me.outspending.invoice.core.data.player.registerPlayerDataManager
import me.outspending.invoice.core.data.player.unregisterPlayerDataManager
import org.bukkit.plugin.java.JavaPlugin
import kotlin.time.measureTime

class InvoiceCore : JavaPlugin() {
    companion object {
        lateinit var instance: JavaPlugin

        var databaseManager = DatabaseManager()
    }

    override fun onEnable() {
        instance = this

        val time = measureTime {
            // Register all listeners
            RegistryType.registerAll()

            // Register database
            databaseManager.start()
            registerPlayerDataManager()

            setupConfig(instance)
        }

        logger.info("InvoiceCore has successfully loaded in $time.")
    }

    override fun onDisable() {
        unregisterPlayerDataManager()

        databaseManager.stop()
    }
}
