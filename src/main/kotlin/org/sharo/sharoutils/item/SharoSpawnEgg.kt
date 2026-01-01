package org.sharo.sharoutils.item

import net.minecraft.item.Item
import net.minecraft.item.SpawnEggItem
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.util.Identifier
import org.sharo.sharoutils.Core
import org.sharo.sharoutils.entity.EntityTypes

class SharoSpawnEgg :
        SpawnEggItem(
                Item.Settings()
                        .maxCount(64)
                        .spawnEgg(EntityTypes.SHARO)
                        .registryKey(
                                RegistryKey.of(
                                        RegistryKeys.ITEM,
                                        Identifier.of(Core.MODID, "sharo_spawn_egg")
                                )
                        )
        )
