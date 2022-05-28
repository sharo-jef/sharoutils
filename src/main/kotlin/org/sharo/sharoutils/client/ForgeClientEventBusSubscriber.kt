package org.sharo.sharoutils.client

import net.minecraft.entity.EntityClassification
import net.minecraft.world.biome.Biome
import net.minecraft.world.biome.MobSpawnInfo
import net.minecraftforge.event.world.BiomeLoadingEvent
import net.minecraftforge.eventbus.api.EventPriority
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import org.sharo.sharoutils.Core
import org.sharo.sharoutils.config.Config
import org.sharo.sharoutils.entity.EntityTypes

@Mod.EventBusSubscriber(modid = Core.MODID)
class ForgeClientEventBusSubscriber {
    companion object {
        @JvmStatic
        @SubscribeEvent(priority = EventPriority.HIGH)
        fun onBiomeLoadingEvent(event: BiomeLoadingEvent) {
            if (
                event.category in arrayOf(
                    Biome.Category.BEACH,
                    Biome.Category.DESERT,
                    Biome.Category.EXTREME_HILLS,
                    Biome.Category.FOREST,
                    Biome.Category.ICY,
                    Biome.Category.MESA,
                    Biome.Category.MUSHROOM,
                    Biome.Category.PLAINS,
                    Biome.Category.OCEAN,
                    Biome.Category.RIVER,
                    Biome.Category.SAVANNA,
                    Biome.Category.SWAMP,
                    Biome.Category.TAIGA
                )
            ) {
                // 問題が多いのでスポーンしないように設定 (1.0.8)
//                val spawns = event.spawns.getSpawner(EntityClassification.MONSTER)
//                spawns.add(MobSpawnInfo.Spawners(EntityTypes.SHARO.get(), Config.sharoSpawnWeight, 1, 4))
            }
        }
    }
}
