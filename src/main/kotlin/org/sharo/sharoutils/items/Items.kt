package org.sharo.sharoutils.items

import net.minecraft.item.Item
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import org.sharo.sharoutils.SharoUtilities

@Mod.EventBusSubscriber(modid = SharoUtilities.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
class Items {
    companion object {
        @JvmStatic
        val SHARO_INGOT: Item = SharoIngot()
        @JvmStatic
        val SHARO_RING: Item = SharoRing()
        @JvmStatic
        val SHARO_MANJU: Item = SharoManju()

        @JvmStatic
        @SubscribeEvent
        fun register(event: RegistryEvent.Register<Item>) {
            event.registry.register(SHARO_INGOT)
            event.registry.register(SHARO_RING)
            event.registry.register(SHARO_MANJU)
        }
    }
}
