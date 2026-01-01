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
                                        EntityType.Builder.create(
                                                        ::SharoEntity,
                                                        SpawnGroup.CREATURE
                                                )
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

                        // Add spawn to biomes
                        BiomeModifications.addSpawn(
                                BiomeSelectors.tag(BiomeTags.IS_OVERWORLD),
                                SpawnGroup.CREATURE,
                                SHARO,
                                10, // weight
                                1, // min group size
                                3 // max group size
                        )
                }
        }
}
