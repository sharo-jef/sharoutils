package org.sharo.sharoutils

import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.sharo.sharoutils.block.Blocks
import org.sharo.sharoutils.entity.EntityTypes
import org.sharo.sharoutils.item.Items

class Core : ModInitializer {
    companion object {
        const val MODID = "sharoutils"

        @JvmStatic val logger: Logger = LogManager.getLogger()

        @JvmStatic lateinit var ITEM_GROUP: ItemGroup
    }

    override fun onInitialize() {
        logger.info("Initializing Sharo Utilities for Fabric")

        // Initialize registries first
        Blocks.register()
        Items.register()
        EntityTypes.register()

        // Register Item Group with items
        ITEM_GROUP =
                FabricItemGroup.builder()
                        .icon { ItemStack(Items.SHARO_INGOT) }
                        .displayName(Text.translatable("itemGroup.sharoutils"))
                        .entries { _, entries ->
                            entries.add(Items.SHARO_INGOT)
                            entries.add(Items.SHARO_RING)
                            entries.add(Items.SHARO_MANJU)
                            entries.add(Items.SHARO_NUGGET)
                            entries.add(Items.SHARO_EARTH)
                            entries.add(Items.ELEVATOR)
                            // Temporarily disabled to prevent crashes
                            // entries.add(Items.SHARO_SPAWN_EGG)
                        }
                        .build()

        Registry.register(Registries.ITEM_GROUP, Identifier.of(MODID, "sharoutils"), ITEM_GROUP)

        // Register events
        org.sharo.sharoutils.item.SharoRing.registerEvents()
        org.sharo.sharoutils.common.ElevatorEventHandler.register()

        logger.info("Sharo Utilities initialized")
    }
}
