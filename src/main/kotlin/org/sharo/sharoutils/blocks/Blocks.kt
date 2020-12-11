package org.sharo.sharoutils.blocks

import net.minecraft.block.Block
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.item.Rarity
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import org.sharo.sharoutils.SharoUtilities

@Mod.EventBusSubscriber(modid = SharoUtilities.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
class Blocks {
    companion object {
        @JvmStatic
        val SHARO_EARTH: Block = SharoEarth()
        @JvmStatic
        val ELEVATOR: Block = Elevator()

        @JvmStatic
        @SubscribeEvent
        fun registerItem(event: RegistryEvent.Register<Item>) {
            event.registry.register(
                BlockItem(
                    SHARO_EARTH,
                    Item.Properties()
                        .group(ItemGroup.BUILDING_BLOCKS)
                        .maxStackSize(64)
                        .rarity(Rarity.RARE)
                )
                    .setRegistryName("sharo_earth")
            )
            event.registry.register(
                BlockItem(
                    ELEVATOR,
                    Item.Properties()
                        .group(ItemGroup.TRANSPORTATION)
                        .maxStackSize(64)
                        .rarity(Rarity.COMMON)
                )
                    .setRegistryName("elevator")
            )
        }

        @JvmStatic
        @SubscribeEvent
        fun registerBlock(event: RegistryEvent.Register<Block>) {
            event.registry.register(SHARO_EARTH)
            event.registry.register(ELEVATOR)
        }
    }
}
