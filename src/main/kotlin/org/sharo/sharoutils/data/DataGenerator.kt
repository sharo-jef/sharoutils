package org.sharo.sharoutils.data

import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent
import org.sharo.sharoutils.SharoUtilities
import org.sharo.sharoutils.data.client.ModBlockStateProvider
import org.sharo.sharoutils.data.client.ModItemModelProvider

@Mod.EventBusSubscriber(modid = SharoUtilities.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
class DataGenerator private constructor() {
    companion object {
        @JvmStatic
        @SubscribeEvent
        fun gatherData(event: GatherDataEvent) {
            event.generator.addProvider(ModItemModelProvider(event.generator, event.existingFileHelper))
            event.generator.addProvider(ModBlockStateProvider(event.generator, event.existingFileHelper))
        }
    }
}
