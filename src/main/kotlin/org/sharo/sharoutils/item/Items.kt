package org.sharo.sharoutils.item

import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier
import net.minecraft.util.Rarity
import org.sharo.sharoutils.Core
import org.sharo.sharoutils.block.Blocks

class Items {
        companion object {
                @JvmStatic lateinit var SHARO_INGOT: Item

                @JvmStatic lateinit var SHARO_RING: Item

                @JvmStatic lateinit var SHARO_MANJU: Item

                @JvmStatic lateinit var SHARO_NUGGET: Item

                @JvmStatic lateinit var SHARO_EARTH: BlockItem

                @JvmStatic lateinit var ELEVATOR: BlockItem

                @JvmStatic lateinit var SHARO_SPAWN_EGG: Item

                @JvmStatic
                fun register() {
                        SHARO_INGOT =
                                Registry.register(
                                        Registries.ITEM,
                                        Identifier.of(Core.MODID, "sharo_ingot"),
                                        SharoIngot()
                                )

                        SHARO_RING =
                                Registry.register(
                                        Registries.ITEM,
                                        Identifier.of(Core.MODID, "sharo_ring"),
                                        SharoRing()
                                )

                        SHARO_MANJU =
                                Registry.register(
                                        Registries.ITEM,
                                        Identifier.of(Core.MODID, "sharo_manju"),
                                        SharoManju()
                                )

                        SHARO_NUGGET =
                                Registry.register(
                                        Registries.ITEM,
                                        Identifier.of(Core.MODID, "sharo_nugget"),
                                        SharoNugget()
                                )

                        SHARO_EARTH =
                                Registry.register(
                                        Registries.ITEM,
                                        Identifier.of(Core.MODID, "sharo_earth"),
                                        BlockItem(
                                                Blocks.SHARO_EARTH,
                                                Item.Settings()
                                                        .maxCount(64)
                                                        .rarity(Rarity.RARE)
                                                        .registryKey(
                                                                net.minecraft.registry.RegistryKey
                                                                        .of(
                                                                                net.minecraft
                                                                                        .registry
                                                                                        .RegistryKeys
                                                                                        .ITEM,
                                                                                Identifier.of(
                                                                                        Core.MODID,
                                                                                        "sharo_earth"
                                                                                )
                                                                        )
                                                        )
                                        )
                                )

                        ELEVATOR =
                                Registry.register(
                                        Registries.ITEM,
                                        Identifier.of(Core.MODID, "elevator"),
                                        BlockItem(
                                                Blocks.ELEVATOR,
                                                Item.Settings()
                                                        .maxCount(64)
                                                        .rarity(Rarity.COMMON)
                                                        .registryKey(
                                                                net.minecraft.registry.RegistryKey
                                                                        .of(
                                                                                net.minecraft
                                                                                        .registry
                                                                                        .RegistryKeys
                                                                                        .ITEM,
                                                                                Identifier.of(
                                                                                        Core.MODID,
                                                                                        "elevator"
                                                                                )
                                                                        )
                                                        )
                                        )
                                )

                        SHARO_SPAWN_EGG =
                                Registry.register(
                                        Registries.ITEM,
                                        Identifier.of(Core.MODID, "sharo_spawn_egg"),
                                        SharoSpawnEgg()
                                )
                }
        }
}
