package org.sharo.sharoutils.block

import net.minecraft.block.Block
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.item.Rarity
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.RegistryObject
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import org.sharo.sharoutils.Core

@Mod.EventBusSubscriber(modid = Core.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
class Blocks {
    companion object {
        @JvmStatic
        val register: DeferredRegister<Block> = DeferredRegister.create(ForgeRegistries.BLOCKS, Core.MODID)

        @JvmStatic
        val SHARO_EARTH_BLOCK: SharoEarth = SharoEarth()
        @JvmStatic
        val ELEVATOR_BLOCK: Elevator = Elevator()

        @JvmStatic
        val SHARO_EARTH: RegistryObject<Block> = register.register("sharo_earth") { SHARO_EARTH_BLOCK }
        @JvmStatic
        val ELEVATOR: RegistryObject<Block> = register.register("elevator") { ELEVATOR_BLOCK }
    }
}
