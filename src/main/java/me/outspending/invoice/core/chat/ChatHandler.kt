package me.outspending.invoice.core.chat

import me.outspending.invoice.core.creator.Creator
import me.outspending.invoice.core.parse
import me.outspending.invoice.core.parseWithPlaceholders
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.JoinConfiguration
import net.kyori.adventure.text.TextReplacementConfig
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

private const val ITEM_FORMAT: String = "<dark_gray>[<white><displayname> <gray><amount><dark_gray>]"
private const val ITEM_LORE_FORMAT: String =
    "<dark_gray>[<white><hover:show_text:'<lore>'><displayname></hover> <gray><amount><dark_gray>]"

class ChatHandler {
    companion object {
        private val plainSerializer = PlainTextComponentSerializer.builder().build()

        /**
         * Creates a new ChatHandler builder with the given component
         *
         * @since 0.0.1
         * @param component The component to start with
         * @author Outspending
         * @return The builder
         */
        @JvmStatic
        fun builder(component: Component) = Builder(component)

        /**
         * Converts an item into an adventure component. If it doesn't have a lore it won't have a hover event. If it does it will.
         *
         * This method is private and should only be used internally. If you are wanting to use this method use [replaceItem] instead.
         *
         * @since 0.0.1
         * @param item The item to convert
         * @return The converted component
         */
        @JvmStatic
        private fun convertItemToComponent(item: ItemStack): Component {
            val itemMeta = item.itemMeta

            val displayName: Component = itemMeta.displayName() ?: item.type.name.parse()
            val lore: List<Component>? = itemMeta.lore()
            val amount: Int = item.amount

            val displayNamePlaceholder = Placeholder.component("displayname", displayName)
            val amountPlaceholder = Placeholder.component("amount", "x$amount".parse())

            return if (lore != null) {
                val lorePlaceholder = Placeholder.component(
                    "lore",
                    Component.join(JoinConfiguration.separator(Component.newline()), lore)
                )

                ITEM_LORE_FORMAT.parseWithPlaceholders(lorePlaceholder, displayNamePlaceholder, amountPlaceholder)
            } else {
                ITEM_FORMAT.parseWithPlaceholders(displayNamePlaceholder, amountPlaceholder)
            }
        }

        /**
         * Replaces the "[item]" placeholder with the given [item]
         *
         * @since 0.0.1
         * @param component The component to replace the item in
         * @param item The item to replace the placeholder with
         * @author Outspending
         * @return The component with the item replaced
         */
        @JvmStatic
        fun replaceItem(component: Component, item: ItemStack): Component {
            if (item.type.isAir) return component
            val replacement = TextReplacementConfig.builder()
                .matchLiteral("[item]")
                .replacement(convertItemToComponent(item))
                .build()

            return component.replaceText(replacement)
        }

        /**
         * Converts the given "[message]" placeholder into a ping for the players that were mentioned in the message. It also sends an action bar message to the mentioned players.
         *
         * @since 0.0.1
         * @param message The message to ping the players in
         * @param sender The player that sent the message
         * @author Outspending
         * @return The component with the players pinged
         */
        @JvmStatic
        fun pingPlayers(message: Component, sender: Player): Component {
            val plainMessage = plainSerializer.serialize(message)

            var newComponent = message
            Bukkit.getOnlinePlayers()
                .filter { plainMessage.contains(it.name) }
                .forEach { player ->
                    val replacement = TextReplacementConfig.builder()
                        .matchLiteral(player.name)
                        .replacement("<yellow><u>@${player.name}".parse())
                        .build()

                    newComponent = message.replaceText(replacement)

                    player.sendActionBar("<main><u>@${sender.name}</u> <gray>has pinged you!".parse(true))
                }

            return newComponent
        }
    }

    /**
     * The builder for the ChatHandler
     *
     * @since 0.0.1
     * @param component The component to start with
     * @author Outspending
     */
    class Builder(private var component: Component) : Creator<Component> {
        fun replaceItem(item: ItemStack): Builder = apply {
            component = replaceItem(component, item)
        }

        fun pingPlayers(sender: Player): Builder = apply {
            component = pingPlayers(component, sender)
        }

        override fun build(): Component = component
    }
}