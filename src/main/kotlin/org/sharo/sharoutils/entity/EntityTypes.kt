package org.sharo.sharoutils.entity

import net.fabricmc.fabric.api.biome.v1.BiomeModifications
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricDefaultAttributeRegistry
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnGroup
import net.minecraft.entity.SpawnLocationTypes
import net.minecraft.entity.SpawnRestriction
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.tag.BiomeTags
import net.minecraft.util.Identifier
import net.minecraft.world.Heightmap
import org.sharo.sharoutils.Core
import org.sharo.sharoutils.config.ModConfig

class EntityTypes {
        companion object {
                @JvmStatic lateinit var SHARO: EntityType<SharoEntity>

                @JvmStatic
                fun register() {
                        val sharoKey =
                                net.minecraft.registry.RegistryKey.of(
                                        net.minecraft.registry.RegistryKeys.ENTITY_TYPE,
                                        Identifier.of(Core.MODID, "sharo")
                                )

                        SHARO =
                                Registry.register(
                                        Registries.ENTITY_TYPE,
                                        Identifier.of(Core.MODID, "sharo"),
                                        EntityType.Builder.create(::SharoEntity, SpawnGroup.AMBIENT)
                                                .dimensions(0.6f, 1.8f)
                                                .build(sharoKey)
                                )

                        // Register entity attributes
                        FabricDefaultAttributeRegistry.register(
                                SHARO,
                                SharoEntity.createSharoAttributes()
                        )

                        // Register spawn restrictions
                        SpawnRestriction.register(
                                SHARO,
                                SpawnLocationTypes.ON_GROUND,
                                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                                SharoEntity::canSpawn
                        )

                        // Add spawn to biomes (AMBIENT = 独立したスポーン枠、昼夜問わず)
                        // Only register natural spawning if enabled in config
                        if (ModConfig.INSTANCE.sharoNaturalSpawnEnabled) {
                                BiomeModifications.addSpawn(
                                        BiomeSelectors.tag(BiomeTags.IS_OVERWORLD),
                                        SpawnGroup.AMBIENT,
                                        SHARO,
                                        ModConfig.INSTANCE.sharoSpawnWeight,
                                        ModConfig.INSTANCE.sharoSpawnMinGroupSize,
                                        ModConfig.INSTANCE.sharoSpawnMaxGroupSize
                                )
                        }
                }
        }
}
