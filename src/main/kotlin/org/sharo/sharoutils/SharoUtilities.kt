package org.sharo.sharoutils

import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

@Suppress("UNUSED_PARAMETER")
@Mod(SharoUtilities.MODID)
class SharoUtilities {
    companion object {
        const val MODID = "sharoutils"
        @JvmStatic
        val logger: Logger = LogManager.getLogger()
    }

    init {
        FMLJavaModLoadingContext.get().modEventBus.addListener<FMLCommonSetupEvent> { setup(it) }
        FMLJavaModLoadingContext.get().modEventBus.addListener<FMLClientSetupEvent> { setupClient(it) }
        FMLJavaModLoadingContext.get().modEventBus.addListener<InterModEnqueueEvent> { enqueueIMC(it) }
        FMLJavaModLoadingContext.get().modEventBus.addListener<InterModProcessEvent> { processIMC(it) }
        MinecraftForge.EVENT_BUS.register(this)
    }

    private fun setup(event: FMLCommonSetupEvent) {
        logger.info("setup")
    }

    private fun setupClient(event: FMLClientSetupEvent) {
        logger.info("setup client")
    }

    private fun enqueueIMC(event: InterModEnqueueEvent) {
        logger.info("enqueue imc")
    }

    private fun processIMC(event: InterModProcessEvent) {
        logger.info("process imc")
    }
}
