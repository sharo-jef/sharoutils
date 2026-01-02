package org.sharo.sharoutils.config

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import net.fabricmc.loader.api.FabricLoader
import org.slf4j.LoggerFactory

data class ModConfig(
        // Playful interaction settings
        val playfulTriggerChance: Double = 0.0001,

        // Sharo entity spawn settings
        val sharoNaturalSpawnEnabled: Boolean = true,
        val sharoSpawnWeight: Int = 100,
        val sharoSpawnMinGroupSize: Int = 2,
        val sharoSpawnMaxGroupSize: Int = 4,

        // Sharo Earth spawn settings
        val sharoEarthSpawnEnabled: Boolean = true,
        val sharoEarthSpawnMinCount: Int = 1,
        val sharoEarthSpawnMaxCount: Int = 3,

        // Sharo Ring recipe settings
        val sharoRingCraftingEnabled: Boolean = true
) {
    companion object {
        private val LOGGER = LoggerFactory.getLogger(ModConfig::class.java)
        private val GSON: Gson = GsonBuilder().setPrettyPrinting().create()
        private val CONFIG_FILE: File =
                FabricLoader.getInstance().configDir.resolve("sharoutils.json").toFile()

        @JvmStatic
        var INSTANCE: ModConfig = ModConfig()
            private set

        @JvmStatic
        fun load() {
            if (CONFIG_FILE.exists()) {
                try {
                    FileReader(CONFIG_FILE).use { reader ->
                        INSTANCE = GSON.fromJson(reader, ModConfig::class.java)
                        LOGGER.info("Loaded config from ${CONFIG_FILE.absolutePath}")
                    }
                } catch (e: Exception) {
                    LOGGER.error("Failed to load config, using defaults", e)
                    save()
                }
            } else {
                LOGGER.info("Config file not found, creating default config")
                save()
            }
        }

        @JvmStatic
        fun save() {
            try {
                CONFIG_FILE.parentFile.mkdirs()
                FileWriter(CONFIG_FILE).use { writer ->
                    GSON.toJson(INSTANCE, writer)
                    LOGGER.info("Saved config to ${CONFIG_FILE.absolutePath}")
                }
            } catch (e: Exception) {
                LOGGER.error("Failed to save config", e)
            }
        }
    }
}
