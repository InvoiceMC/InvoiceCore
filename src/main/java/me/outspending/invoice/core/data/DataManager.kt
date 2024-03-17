package me.outspending.invoice.core.data

/**
 * A class that represents a data manager, which is widely used to manage data inside InvoiceCore.
 *
 * This method contains all the necessary methods to manage data, such as loading, unloading, saving, and getting data.
 * These are also already implemented in the class, so you don't have to worry about implementing them.
 *
 * @since 0.0.1
 * @param K The key type
 * @param V The value type
 * @author Outspending
 */
abstract class DataManager<K : Any, V : Any>(val data: MutableMap<K, V>) {
    constructor() : this(mutableMapOf())

    /**
     * Whether the data manager is loaded or not
     */
    var isLoaded = false
        internal set

    /**
     * The method to load the data manager, usually used to create a table inside the database
     *
     * @since 0.0.1
     * @author Outspending
     */
    abstract fun load()

    /**
     * This method is used to unload the data manager, usually used to save all the loaded data from memory to the database
     *
     * This usually calls the [saveAllData] method to save all the data to the database.
     *
     * @since 0.0.1
     * @author Outspending
     */
    abstract fun unload()

    /**
     * This method saves all the loaded data to the database from memory *(aka [data])*
     *
     * @since 0.0.1
     * @author Outspending
     */
    abstract fun saveAllData()

    /**
     * This method loads data into memory from the database. Which is added to [data] and can be accessed using [getData] or [getDataNullable]
     *
     * @since 0.0.1
     * @param key The key to load the data
     * @author Outspending
     * @return The loaded data
     */
    abstract suspend fun loadData(key: K): V

    /**
     * This method saves the data to the database then it will be removed from memory
     *
     * @since 0.0.1
     * @param key The key to unload the data
     * @author Outspending
     */
    abstract fun unloadData(key: K)

    /**
     * This method saves the data to the database, although it won't be removed from memory.
     *
     * @since 0.0.1
     * @param key The key to save the data
     * @return The saved data
     */
    abstract fun saveData(key: K)

    /**
     * This method grabs the data from memory using its key. If the data is not found, it will throw an error. If you aren't sure if the data exists, use [getDataNullable] instead.
     *
     * @since 0.0.1
     * @param key The key to get the data
     * @author Outspending
     * @throws NullPointerException If the data is not found
     * @return The data
     */
    fun getData(key: K): V = getDataNullable(key)!!

    /**
     * This method grabs the data from memory using its key. If the data is not found, it will return null. If you are sure that the data exists, use [getData] instead.
     *
     * @since 0.0.1
     * @param key The key to get the data
     * @return The data, or null if the data is not found
     */
    fun getDataNullable(key: K): V? = data[key]

    /**
     * This method clears all the data from memory
     *
     * @since 0.0.1
     * @author Outspending
     */
    fun clear() = data.clear()

    /**
     * This method checks if the data exists in memory
     *
     * @since 0.0.1
     * @param key The key to check if the data exists
     * @return Whether the data exists or not
     */
    fun hasData(key: K) = data.containsKey(key)

    /**
     * This method adds data to memory
     *
     * @since 0.0.1
     * @param key The key to add the data
     * @param value The data to add
     */
    fun addData(key: K, value: V) = data.put(key, value)

    /**
     * This method removes data from memory
     *
     * @since 0.0.1
     * @param key The key to remove the data
     */
    fun removeData(key: K) = data.remove(key)

    /**
     * This method gets all the data from memory as a list, which is useful for iterating through all the data
     *
     * @since 0.0.1
     * @return A list of all the data
     */
    fun getAllDataList() : List<V> = data.values.toList()

    /**
     * This method gets all the data from memory
     *
     * @since 0.0.1
     * @return A map of all the data
     */
    fun getAllDataMap() : Map<K, V> = data
}