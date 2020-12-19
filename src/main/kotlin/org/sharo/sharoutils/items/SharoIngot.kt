package org.sharo.sharoutils.items

import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.item.Rarity
import net.minecraftforge.fml.common.Mod
import org.sharo.sharoutils.SharoUtilities

@Mod.EventBusSubscriber(modid = SharoUtilities.MODID)
class SharoIngot : Item(
    Properties()
        .group(ItemGroup.MATERIALS)
        .maxStackSize(64)
        .rarity(Rarity.COMMON)
) {
    init {
        setRegistryName("sharo_ingot")
    }
}
