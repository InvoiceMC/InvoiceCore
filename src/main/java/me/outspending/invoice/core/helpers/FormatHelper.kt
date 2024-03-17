package me.outspending.invoice.core.helpers

import me.outspending.invoice.core.config.ConfigValues
import me.outspending.invoice.core.core
import me.outspending.invoice.core.helpers.FormatHelper.Companion.chatcolorResolver
import me.outspending.invoice.core.helpers.FormatHelper.Companion.mainColorResolver
import me.outspending.invoice.core.helpers.FormatHelper.Companion.secondColorResolver
import me.outspending.invoice.core.parse
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.Tag
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags
import java.util.*

private val MAIN_COLOR = TextColor.color(232, 132, 56)
private val SECOND_COLOR = TextColor.color(224, 161, 112)

private val miniMessage = MiniMessage.builder()
    .tags(
        TagResolver.builder()
            .resolver(StandardTags.defaults())
            .resolver(mainColorResolver())
            .resolver(secondColorResolver())
            .resolver(chatcolorResolver())
            .build()
    )
    .build()

val small_caps: List<Pair<Char, Char>> = listOf( // Moved to List to remove all the overhead
    'a' to 'ᴀ',
    'b' to 'ʙ',
    'c' to 'ᴄ',
    'd' to 'ᴅ',
    'e' to 'ᴇ',
    'f' to 'ꜰ',
    'g' to 'ɢ',
    'h' to 'ʜ',
    'i' to 'ɪ',
    'j' to 'ᴊ',
    'k' to 'ᴋ',
    'l' to 'ʟ',
    'm' to 'ᴍ',
    'n' to 'ɴ',
    'o' to 'ᴏ',
    'p' to 'ᴘ',
    'q' to 'ǫ',
    'r' to 'ʀ',
    's' to 'ꜱ',
    't' to 'ᴛ',
    'u' to 'ᴜ',
    'v' to 'ᴠ',
    'w' to 'ᴡ',
    'x' to 'x',
    'y' to 'ʏ',
    'z' to 'ᴢ'
)

/**
 * Used to handle formatting of strings, such as small caps, parsing to components, ect.
 *
 * @since 0.0.1
 * @author Outspending
 */
class FormatHelper {

    /**
     * Converts a string to small caps.
     *
     * @author Outspending
     * @since 0.0.1
     */
    fun toSmallCaps(string: String): String {
        var result = string
        for ((from, to) in small_caps) {
            result = result.replace(from, to)
        }

        return result
    }

    /**
     * Parses a string to a component.
     *
     * @param string The string to parse.
     * @param italic Whether the string should be italic.
     * @param prefix Whether the string should be prefixed.
     * @return The parsed component.
     * @since 0.0.1
     * @author Outspending
     */
    fun parse(string: String, italic: Boolean = false, prefix: Boolean = false): Component =
        parseInternal(string, italic, prefix, null)

    /**
     * Parses a string to a component with placeholders.
     *
     * @param string The string to parse.
     * @param placeholders The placeholders to use.
     * @return The parsed component.
     * @since 0.0.1
     * @author Outspending
     */
    fun parseWithPlaceholders(string: String, vararg placeholders: TagResolver): Component =
        parseInternal(string = string, placeholders = placeholders)

    private fun parseInternal(
        string: String,
        italic: Boolean = false,
        prefix: Boolean = false,
        placeholders: Array<out TagResolver>?
    ): Component {
        val message: Component = if (placeholders == null) miniMessage.deserialize(string) else miniMessage.deserialize(
            string,
            *placeholders
        )
        if (italic) message.decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE)

        return if (prefix) ConfigValues.PREFIX.append(message) else message
    }

    /**
     * Sanitizes a string.
     *
     * @param string The string to sanitize.
     * @return The sanitized string.
     * @since 0.0.1
     * @author Outspending
     */
    fun sanitize(string: String) = miniMessage.escapeTags(string)

    /**
     * Converts a string to title case. This means that the first letter of each word is capitalized.
     *
     * @param string The string to convert.
     * @return The converted string.
     * @since 0.0.1
     * @author Outspending
     */
    fun toTitleCase(string: String) =
        string.split(" ").joinToString(" ") { it.lowercase().replaceFirstChar { char -> char.uppercaseChar() } }

    companion object {
        @JvmStatic
        internal fun chatcolorResolver(): TagResolver {
            return TagResolver.resolver(
                "chatcolor"
            ) { args: ArgumentQueue, _ ->
                val uuidString = args.popOr("uuid expected").value()
                val uuid = UUID.fromString(uuidString)
                val player =
                    core.server.getPlayer(uuid) ?: throw IllegalArgumentException("Player $uuidString not found")

                val color = NamedTextColor.GRAY // TODO: Get the player's chatcolor for real
                Tag.styling(TextColor.color(color.red(), color.green(), color.blue()))
            }
        }

        @JvmStatic
        internal fun mainColorResolver(): TagResolver {
            return TagResolver.resolver(
                "main"
            ) { _: ArgumentQueue, _ ->
                Tag.styling(MAIN_COLOR)
            }
        }

        @JvmStatic
        internal fun secondColorResolver(): TagResolver {
            return TagResolver.resolver(
                "second"
            ) { _: ArgumentQueue, _ ->
                Tag.styling(SECOND_COLOR)
            }
        }

        @JvmStatic
        private fun ArgumentQueue.nextOrNull() = if (hasNext()) pop().value() else null
    }
}