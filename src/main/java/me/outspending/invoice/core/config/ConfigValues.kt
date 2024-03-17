package me.outspending.invoice.core.config

import me.outspending.invoice.core.parse

interface ConfigValues {
    companion object {
        val PREFIX = config.config.getString("prefix").parse()
    }
}