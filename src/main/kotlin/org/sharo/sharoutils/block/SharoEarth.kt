package org.sharo.sharoutils.block

import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.GrassBlock
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnReason
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.random.Random
import org.sharo.sharoutils.Core
import org.sharo.sharoutils.config.ModConfig

class SharoEarth :
        GrassBlock(
                Settings.copy(Blocks.GRASS_BLOCK)
                        .strength(3f, 10f)
                        .registryKey(
                                RegistryKey.of(
                                        RegistryKeys.BLOCK,
                                        Identifier.of(Core.MODID, "sharo_earth")
                                )
                        )
        ) {
        override fun randomTick(
                state: BlockState,
                world: ServerWorld,
                pos: BlockPos,
                random: Random
        ) {
                super.randomTick(state, world, pos, random)

                // Check if spawning is enabled in config
                if (!ModConfig.INSTANCE.sharoEarthSpawnEnabled) {
                        return
                }

                // Temporarily disabled Sharo entity to prevent crashes
                val entityTypes =
                        arrayOf(
                                EntityType.SKELETON,
                                EntityType.ZOMBIE,
                                EntityType.SPIDER,
                                EntityType.CREEPER,
                                EntityType.ENDERMAN,
                                // EntityTypes.SHARO,
                                )
                val entityType = entityTypes.random()
                // Spawn 1 block above to prevent entities from being stuck in the block
                val spawnPos = pos.up()
                val minCount = ModConfig.INSTANCE.sharoEarthSpawnMinCount
                val maxCount = ModConfig.INSTANCE.sharoEarthSpawnMaxCount
                val spawnCount =
                        if (maxCount > minCount) {
                                random.nextInt(maxCount - minCount + 1) + minCount
                        } else {
                                minCount
                        }
                for (i in 1..spawnCount) {
                        entityType.spawn(world, spawnPos, SpawnReason.EVENT)
                }
        }
}
