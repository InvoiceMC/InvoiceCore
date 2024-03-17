package me.outspending.invoice.core.listeners.types

import io.papermc.paper.event.player.AsyncChatEvent
import me.outspending.invoice.core.chat.ChatHandler
import me.outspending.invoice.core.data.player.PlayerData
import me.outspending.invoice.core.data.player.getData
import me.outspending.invoice.core.format
import me.outspending.invoice.core.parseWithPlaceholders
import me.outspending.invoice.core.toTiny
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class ChatListeners : Listener {
    private val PLAYER_STATS_HOVER_MESSAGE =
        listOf(
            "",
            "<green>\$%s <dark_green>ᴍᴏɴᴇʏ",
            "<yellow>⛁%s <gold>ɢᴏʟᴅ",
            "<gray>%s <dark_gray>ʙʀᴏᴋᴇɴ",
            "<#e8d47d>%s <#debe33>ʟᴇᴠᴇʟ",
            "<#d283de>%s <#b36de8>ᴘʀᴇꜱᴛɪɢᴇ",
            ""
        )
            .joinToString("\n")

    private fun createHoverText(playerData: PlayerData, playerLevel: Int): String =
        PLAYER_STATS_HOVER_MESSAGE.format(
            playerData.balance.format(),
            playerData.gold.format(),
            playerData.blocksBroken.format(),
            playerLevel,
            playerData.prestige,
        )

    @EventHandler
    fun onChat(event: AsyncChatEvent) {
        event.renderer { player, _, message, _ ->
            val newMessage: Component =
                ChatHandler.builder(message)
                    .pingPlayers(player)
                    //.replaceItem(player.inventory.itemInMainHand)
                    .build()

            val msg = Placeholder.component("message", newMessage)

            val playerData = player.getData()
            val hoverText = createHoverText(playerData, player.level)

            "<hover:show_text:'$hoverText'>${player.name}<gold><bold>${player.level.toTiny()}</bold></hover> <gray>»<white> <message>"
                .parseWithPlaceholders(msg)
        }
    }
}