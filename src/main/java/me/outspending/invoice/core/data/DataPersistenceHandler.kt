package me.outspending.invoice.core.data

import me.outspending.invoice.core.data.player.PlayerDataPersistenceHandler

/**
 * This interface is used to define a data persistence handler. This is used to save and load data from a data source. This is used widely in the plugin to save and load data.
 *
 * @since 0.0.1
 * @author Outspending
 * @param K The key type
 * @param V The value type
 * @sample PlayerDataPersistenceHandler
 */
interface DataPersistenceHandler<K, V> {
    /**
     * This method is used to save the data to the data source.
     *
     * @since 0.0.1
     * @param value The value to save
     * @param data The data to save
     * @sample PlayerDataPersistenceHandler.save
     */
    fun save(value: V, data: K?)

    /**
     * This method is used to load the data from the data source.
     *
     * @since 0.0.1
     * @param value The value to load
     * @sample PlayerDataPersistenceHandler.load
     * @return The loaded data
     */
    fun load(value: V): K
}