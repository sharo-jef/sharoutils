package org.sharo.sharoutils.item

import net.minecraft.item.Item
import net.minecraft.item.Rarity
import org.sharo.sharoutils.Core

class SharoIngot : Item(
    Properties()
        .group(Core.ITEM_GROUP)
        .maxStackSize(64)
        .rarity(Rarity.COMMON)
)
