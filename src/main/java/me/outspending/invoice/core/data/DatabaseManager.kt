package me.outspending.invoice.core.data

import me.outspending.invoice.core.core
import me.outspending.invoice.core.interfaces.Startable
import me.outspending.invoice.core.interfaces.Stopable
import me.outspending.munch.connection.MunchConnection

private const val DATABASE_NAME = "database.db"

val database = MunchConnection.global()

class DatabaseManager : Startable, Stopable {
    override fun start() {
        val dataFolder = core.dataFolder
        if (!dataFolder.exists()) dataFolder.mkdirs()

        database.connect(dataFolder, DATABASE_NAME)
    }

    override fun stop() {
        if (!database.isConnected()) return
        else database.disconnect()
    }
}