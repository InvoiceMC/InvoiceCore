package me.outspending.invoice.core

import com.github.shynixn.mccoroutine.bukkit.SuspendingJavaPlugin
import com.github.shynixn.mccoroutine.bukkit.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import me.outspending.invoice.core.config.setupConfig
import me.outspending.invoice.core.data.DatabaseManager
import me.outspending.invoice.core.data.player.registerPlayerDataManager
import me.outspending.invoice.core.data.player.unregisterPlayerDataManager
import kotlin.time.measureTime

lateinit var core: SuspendingJavaPlugin
var databaseManager = DatabaseManager()

class InvoiceCore : SuspendingJavaPlugin() {
    override suspend fun onEnableAsync() {
        core = this

        core.launch {
            val time = measureTime {
                // Register all listeners
                RegistryType.registerAll()

                // Register database
                databaseManager.start()
                registerPlayerDataManager()

                setupConfig(core)
            }

            logger.info("InvoiceCore has successfully loaded in $time.")
        }
    }

    override suspend fun onDisableAsync() {
        unregisterPlayerDataManager()

        databaseManager.stop()
    }
}
