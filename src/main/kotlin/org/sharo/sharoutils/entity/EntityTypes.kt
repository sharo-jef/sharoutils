package org.sharo.sharoutils.entity

import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricDefaultAttributeRegistry
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnGroup
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier
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
                            EntityType.Builder.create(::SharoEntity, SpawnGroup.CREATURE)
                                    .dimensions(1f, 1f)
                                    .build(sharoKey)
                    )

            // Register entity attributes
            FabricDefaultAttributeRegistry.register(SHARO, SharoEntity.createSharoAttributes())
        }
    }
}
