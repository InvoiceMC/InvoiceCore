package me.outspending.invoice.core.config

import dev.dejvokep.boostedyaml.YamlDocument
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

internal val config = DefaultConfig()

fun setupConfig(plugin: JavaPlugin) = config.setup(plugin)

class DefaultConfig {
    lateinit var config: YamlDocument
    private var isSetup = false

    fun setup(plugin: JavaPlugin) {
        if (isSetup) return

        config = YamlDocument.create(File(plugin.dataFolder, "config.yml"), plugin.getResource("config.yml")!!)
        isSetup = true
    }
}