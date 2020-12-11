package org.sharo.sharoutils.items

import net.minecraft.item.Food
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.item.Rarity
import net.minecraft.potion.EffectInstance
import net.minecraft.potion.Effects
import net.minecraftforge.fml.common.Mod
import org.sharo.sharoutils.SharoUtilities

@Mod.EventBusSubscriber(modid = SharoUtilities.MODID)
class SharoManju : Item {
    constructor() : super(
        Properties()
            .group(ItemGroup.FOOD)
            .maxStackSize(64)
            .food(
                Food.Builder()
                    .hunger(20)
                    .saturation(20f)
                    .effect({ EffectInstance(Effects.JUMP_BOOST, 60 * 20, 3) }, 1f)
                    .build()
            )
            .rarity(Rarity.COMMON)
    ) {
        setRegistryName("sharo_manju")
    }
}
