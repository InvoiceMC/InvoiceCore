package me.outspending.invoice.core.data.player

import com.github.shynixn.mccoroutine.bukkit.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import me.outspending.invoice.core.InvoiceCore
import me.outspending.invoice.core.data.DataManager
import me.outspending.invoice.core.data.database
import me.outspending.invoice.core.parse
import me.outspending.invoice.core.runTaskTimer
import me.outspending.munch.Munch
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.UUID
import kotlin.time.measureTime

internal val munchPlayerData = Munch.create(PlayerData::class).process<UUID>()
internal val playerDataManager = PlayerDataManager()

fun registerPlayerDataManager() = playerDataManager.load()
fun unregisterPlayerDataManager() = playerDataManager.unload()

/** Extensions */
fun Player.getData(): PlayerData = playerDataManager.getData(this)
fun Player.getDataNullable(): PlayerData? = playerDataManager.getDataNullable(this)

class PlayerDataManager : DataManager<Player, PlayerData>() {
    private val BROADCAST_MESSAGE: String =
        listOf(
            "",
            "<#7ee37b><b>ᴘʟᴀʏᴇʀᴅᴀᴛᴀ</b>",
            "  <gray>Successfully saved <#7ee37b><u>%s</u><gray> player(s)",
            "  <gray>data in <#7ee37b><u>%s</u><gray>!",
            ""
        )
            .joinToString("\n")
    private val persistenceHandler = PlayerDataPersistenceHandler()

    init {
        runTaskTimer(6000, 6000, true) {
            saveAllData()
        }
    }

    override fun load() {
        if (isLoaded) return

        database.createTable(munchPlayerData)
        isLoaded = true
    }

    override fun unload() {
        if (!isLoaded) return

        saveAllData()
    }

    override fun saveAllData() {
        InvoiceCore.instance.launch {
            val allData = data.values.toList()
            val time = measureTime {
                async(Dispatchers.IO) { database.updateAllData(munchPlayerData, allData) }.await()
            }

            Bukkit.broadcast(BROADCAST_MESSAGE.format(data.size, time).parse())
        }
    }

    override fun saveData(key: Player) {
        if (!data.containsKey(key)) return

        val playerData: PlayerData = getData(key)
        InvoiceCore.instance.launch {
            async(Dispatchers.IO) { persistenceHandler.save(key.uniqueId, playerData) }.await()
        }
    }

    override fun unloadData(key: Player) {
        if (!data.containsKey(key)) return

        val playerData: PlayerData? = removeData(key)
        if (playerData != null) {
            InvoiceCore.instance.launch {
                async(Dispatchers.IO) { persistenceHandler.save(key.uniqueId, playerData) }.await()
            }
        }
    }

    override suspend fun loadData(key: Player): PlayerData {
        if (data.containsKey(key)) return getData(key)

        val playerData = coroutineScope {
            async(Dispatchers.IO) { persistenceHandler.load(key.uniqueId) }.await()
        }

        addData(key, playerData)
        return playerData
    }
}