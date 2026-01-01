package org.sharo.sharoutils.block

import net.minecraft.block.Block
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier
import org.sharo.sharoutils.Core

class Blocks {
        companion object {
                @JvmStatic lateinit var SHARO_EARTH: Block

                @JvmStatic lateinit var ELEVATOR: Block

                @JvmStatic
                fun register() {
                        SHARO_EARTH =
                                Registry.register(
                                        Registries.BLOCK,
                                        Identifier.of(Core.MODID, "sharo_earth"),
                                        SharoEarth()
                                )

                        ELEVATOR =
                                Registry.register(
                                        Registries.BLOCK,
                                        Identifier.of(Core.MODID, "elevator"),
                                        Elevator()
                                )
                }
        }
}
