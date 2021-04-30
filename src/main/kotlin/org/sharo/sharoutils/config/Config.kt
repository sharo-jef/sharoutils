package org.sharo.sharoutils.config

import net.minecraftforge.common.ForgeConfigSpec
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.config.ModConfig
import org.sharo.sharoutils.Core

@Mod.EventBusSubscriber(modid = Core.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
class Config {
    companion object {
        @JvmStatic
        private val specPair = ForgeConfigSpec.Builder().configure(::ClientConfig)
        @JvmStatic
        val CLIENT_SPEC: ForgeConfigSpec = specPair.right
        @JvmStatic
        val CLIENT: ClientConfig = specPair.left

        @JvmStatic
        var spawnSharo = true
        @JvmStatic
        var sharoHealth = 20
        @JvmStatic
        var sharoSpawnWeight = 10

        @JvmStatic
        fun bakeConfig() {
            spawnSharo = CLIENT.spawnSharo.get()
            sharoHealth = CLIENT.sharoHealth.get()
            sharoSpawnWeight = CLIENT.sharoSpawnWeight.get()
        }

        @JvmStatic
        @SubscribeEvent
        fun onModConfigEvent(event: ModConfig.ModConfigEvent) {
            Core.logger.info("SPEC: ${event.config.spec}")
            if (event.config.spec === CLIENT_SPEC) {
                bakeConfig()
            }
        }
    }

    class ClientConfig(builder: ForgeConfigSpec.Builder) {
        val spawnSharo: ForgeConfigSpec.BooleanValue
        val sharoHealth: ForgeConfigSpec.IntValue
        val sharoSpawnWeight: ForgeConfigSpec.IntValue
        init {
            builder
                .comment("Settings about SharoEntity")
                .push("Sharo")
            spawnSharo = builder
                .comment("Set whether Sharo will spawn. (Default: true)")
                .translation("${Core.MODID}.config.spawnSharo")
                .define("spawnSharo", true)
            sharoHealth = builder
                .comment("Set max health of Sharo. (Default: 20)")
                .translation("${Core.MODID}.config.sharoHealth")
                .defineInRange("sharoHealth", 20, 1, 1000)
            sharoSpawnWeight = builder
                .comment("Set spawn weight of Sharo. (Default: 10)")
                .translation("${Core.MODID}.config.sharoSpawnWeight")
                .defineInRange("sharoSpawnWeight", 10, 0, 1000)
            builder.pop()
        }
    }
}
