package me.outspending.invoice.core.data

import me.outspending.invoice.core.InvoiceCore
import me.outspending.invoice.core.interfaces.Startable
import me.outspending.invoice.core.interfaces.Stopable
import me.outspending.munch.connection.MunchConnection

private const val DATABASE_NAME = "database.db"

val database = MunchConnection.global()

class DatabaseManager : Startable, Stopable {
    override fun start() {
        val dataFolder = InvoiceCore.instance.dataFolder
        if (!dataFolder.exists()) dataFolder.mkdirs()

        database.connect(dataFolder, DATABASE_NAME)
    }

    override fun stop() {
        if (!database.isConnected()) return
        else database.disconnect()
    }
}