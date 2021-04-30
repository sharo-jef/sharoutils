package org.sharo.sharoutils

import net.minecraft.entity.EntityType
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.DeferredWorkQueue
import net.minecraftforge.fml.ModLoadingContext
import net.minecraftforge.fml.client.registry.RenderingRegistry
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.config.ModConfig
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.sharo.sharoutils.block.Blocks
import org.sharo.sharoutils.client.render.SharoRenderer
import org.sharo.sharoutils.config.Config
import org.sharo.sharoutils.entity.EntityTypes
import org.sharo.sharoutils.entity.SharoEntity
import org.sharo.sharoutils.item.Items
import org.sharo.sharoutils.item.SharoSpawnEgg
import org.sharo.sharoutils.tab.SharoUtilitiesItemGroup

@Suppress("UNUSED_PARAMETER")
@Mod(Core.MODID)
@Mod.EventBusSubscriber(modid = Core.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = [Dist.CLIENT])
class Core {
    companion object {
        const val MODID = "sharoutils"
        @JvmStatic
        val logger: Logger = LogManager.getLogger()

        @JvmStatic
        val ITEM_GROUP: SharoUtilitiesItemGroup = SharoUtilitiesItemGroup()

        @JvmStatic
        @SubscribeEvent
        fun onRegisterEntities(event: RegistryEvent.Register<EntityType<*>>) {
            SharoSpawnEgg.initSpawnEggs()
        }

        @JvmStatic
        @SubscribeEvent
        fun onClientSetup(event: FMLClientSetupEvent) {
            RenderingRegistry.registerEntityRenderingHandler(
                EntityTypes.SHARO.get(),
                ::SharoRenderer
            )
        }
    }

    init {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.CLIENT_SPEC)

        EntityTypes.SHARO

        val bus = FMLJavaModLoadingContext.get().modEventBus

        Blocks.register.register(bus)
        EntityTypes.register.register(bus)
        Items.register.register(bus)

        bus.addListener<FMLCommonSetupEvent> { setup(it) }
        bus.addListener<FMLClientSetupEvent> { setupClient(it) }
        bus.addListener<InterModEnqueueEvent> { enqueueIMC(it) }
        bus.addListener<InterModProcessEvent> { processIMC(it) }
    }

    private fun setup(event: FMLCommonSetupEvent) {
        logger.info("setup")

        DeferredWorkQueue.runLater {
            GlobalEntityTypeAttributes.put(
                EntityTypes.SHARO.get(),
                SharoEntity.setCustomAttributes().create()
            )
        }
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
