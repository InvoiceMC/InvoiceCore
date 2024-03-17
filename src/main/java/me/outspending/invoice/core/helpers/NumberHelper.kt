package me.outspending.invoice.core.helpers

import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.pow

private val FORMAT_SUFFIXES: Array<String> =
    arrayOf(
        " ",
        "K",
        "M",
        "B",
        "T",
        "Q",
        "Qu",
        "S",
        "Sp",
        "O",
        "N",
        "D",
        "U",
        "Dc",
        "V",
        "Tg",
        "Pg",
        "Eg",
        "Zg",
        "Yg"
    )
private val TINY_NUMBERS: Map<Char, Char> =
    mapOf(
        '0' to '⁰',
        '1' to '¹',
        '2' to '²',
        '3' to '³',
        '4' to '⁴',
        '5' to '⁵',
        '6' to '⁶',
        '7' to '⁷',
        '8' to '⁸',
        '9' to '⁹',
    )

class NumberHelper {

    /**
     * Converts the number into 1.2 instead of 1.20000000000000000000001
     *
     * @since 0.0.1
     * @author Outspending
     * @param number The number to fix.
     */
    fun fix(number: Number): String = String.format("%.1f", number.toDouble())

    /**
     * Converts a number to a string with a prefix.
     *
     * @param number The number to convert.
     * @param decimalPlace The number of decimal places to round to.
     * @return The number as a string with a prefix.
     */
    fun toCommas(number: Number, decimalPlace: Byte = 0): String = String.format("%,.${decimalPlace}f", number)

    /**
     * Converts a number to a string with a prefix, Such as 1.2K, 3.4M, 5.6B, 7.8T, 9.10Q, 11.12Qu, 13.14S, 15.16Sp, 17.18O, 19.20N, 21.22D, 23.24U, 25.26Dc, 27.28V, 29.30Tg, 31.32Pg, 33.34Eg, 35.36Zg, 37.38Yg.
     *
     * @param number The number to convert.
     * @param decimalPlace The number of decimal places to round to. (If the number is less than 1, this will be ignored.)
     * @since 0.0.1
     * @author Outspending
     * @return The number as a string with a prefix.
     */
    fun toShorten(number: Number, decimalPlace: Byte = 0): String {
        val numValue: Long = number.toLong()
        val value: Int = floor(log10(numValue.toDouble())).toInt()
        val base: Int = value / 3

        if (value >= 3 && base < FORMAT_SUFFIXES.size) {
            val formattedNumber: Double = numValue / 10.0.pow((base * 3).toDouble())
            return if (formattedNumber % 0 == 0.0) {
                String.format("%.0f%s", formattedNumber, FORMAT_SUFFIXES[base])
            } else {
                String.format("%.${decimalPlace}f%s", formattedNumber, FORMAT_SUFFIXES[base])
            }
        } else {
            return String.format("%.0f", numValue.toDouble())
        }
    }

    /**
     * Converts a number into a progress bar string.
     *
     * @since 0.0.1
     * @author Outspending
     * @param number The number to convert.
     * @param max The maximum number.
     * @param bars The number of bars to display.
     * @param separator The separator to use.
     * @return The number as a progress bar string.
     */
    fun toBar(number: Number, max: Number = 100, bars: Int = 25, separator: String = "|", completedColor: String = "<green>", notCompletedColor: String = "<red>"): String {
        val filledBars: Int = (number.toDouble() / max.toDouble() * bars.toDouble()).toInt()
        val emptyBars = bars - filledBars
        return "$completedColor${separator.repeat(filledBars)}$notCompletedColor${separator.repeat(emptyBars)}"
    }

    /**
     * Converts a number into tiny numbers such as 100 to ¹⁰⁰.
     *
     * @param number The number to convert.
     * @since 0.0.1
     * @author Outspending
     */
    fun toTinyNumbers(number: Number): String {
        val numberString: String = number.toString()
        return buildString {
            for (char in numberString) {
                append(TINY_NUMBERS[char] ?: char)
            }
        }
    }

    /**
     * Clamps a number between a minimum and maximum.
     *
     * @param number The number to clamp.
     * @param min The minimum number.
     * @param max The maximum number.
     * @return The clamped number.
     * @since 0.0.1
     * @author Outspending
     */
    fun clamp(number: Number, min: Number, max: Number): Number {
        return when {
            number.toDouble() < min.toDouble() -> min
            number.toDouble() > max.toDouble() -> max
            else -> number
        }
    }
}