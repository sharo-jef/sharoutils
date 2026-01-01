package org.sharo.sharoutils.item

import net.minecraft.item.Item
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.util.Identifier
import net.minecraft.util.Rarity
import org.sharo.sharoutils.Core

class SharoNugget :
        Item(
                Settings()
                        .maxCount(64)
                        .rarity(Rarity.COMMON)
                        .registryKey(
                                RegistryKey.of(
                                        RegistryKeys.ITEM,
                                        Identifier.of(Core.MODID, "sharo_nugget")
                                )
                        )
        )
