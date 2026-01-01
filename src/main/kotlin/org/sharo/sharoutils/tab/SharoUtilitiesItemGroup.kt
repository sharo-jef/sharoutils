package org.sharo.sharoutils.tab

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import org.sharo.sharoutils.Core
import org.sharo.sharoutils.item.Items

object SharoUtilitiesItemGroup {
    val SHARO_GROUP: RegistryKey<ItemGroup> =
            RegistryKey.of(RegistryKeys.ITEM_GROUP, Identifier.of(Core.MODID, "sharo_utilities"))

    fun register() {
        Registry.register(
                Registries.ITEM_GROUP,
                SHARO_GROUP,
                FabricItemGroup.builder()
                        .icon { ItemStack(Items.SHARO_INGOT) }
                        .displayName(Text.literal("Sharo Utilities"))
                        .build()
        )
    }
}
