package me.outspending.invoice.core

import me.outspending.invoice.core.helpers.FormatHelper
import me.outspending.invoice.core.helpers.NumberHelper
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.scheduler.BukkitScheduler

@PublishedApi
internal val scheduler: BukkitScheduler = Bukkit.getScheduler()
@PublishedApi
internal val formatHelper = FormatHelper()
@PublishedApi
internal val numberHelper = NumberHelper()

fun String.parse(prefix: Boolean = false, italic: Boolean = false): Component = formatHelper.parse(this, italic, prefix)
fun String.parseWithPlaceholders(vararg placeholders: TagResolver): Component =
    formatHelper.parseWithPlaceholders(this, *placeholders)

fun String.toTitleCase(): String = formatHelper.toTitleCase(this)
fun String.toTiny(): String = formatHelper.toSmallCaps(this)

fun Number.format(decimalPlace: Byte = 0): String = numberHelper.toShorten(this, decimalPlace)
fun Number.regex(decimalPlace: Byte = 0): String = numberHelper.toCommas(this, decimalPlace)
fun Number.fix(): String = numberHelper.fix(this)
fun Number.toTiny(): String = numberHelper.toTinyNumbers(this)

fun Location.center(): Location = this.clone().add(0.5, 0.5, 0.5)

inline fun delay(delay: Long, crossinline block: () -> Unit) = scheduler.runTaskLater(core, Runnable { block() }, delay)
inline fun repeat(delay: Long, period: Long, crossinline block: () -> Unit) =
    scheduler.runTaskTimer(core, Runnable { block() }, delay, period)

inline fun runTask(runAsync: Boolean = false, crossinline block: () -> Unit) =
    if (runAsync) scheduler.runTaskAsynchronously(core, Runnable { block() })
    else scheduler.runTask(core, Runnable { block() })

inline fun runTaskLater(delay: Long, runAsync: Boolean = false, crossinline block: () -> Unit) =
    if (runAsync) scheduler.runTaskLaterAsynchronously(core, Runnable { block() }, delay)
    else scheduler.runTaskLater(core, Runnable { block() }, delay)

inline fun runTaskTimer(delay: Long, period: Long, runAsync: Boolean = false, crossinline block: () -> Unit) =
    if (runAsync) scheduler.runTaskTimerAsynchronously(core, Runnable { block() }, delay, period)
    else scheduler.runTaskTimer(core, Runnable { block() }, delay, period)

fun progressBar(number: Number, max: Number = 100, bars: Int = 25, separator: String = "|", completedColor: String = "<green>", notCompletedColor: String = "<red>") =
    numberHelper.toBar(number, max, bars, separator, completedColor, notCompletedColor)