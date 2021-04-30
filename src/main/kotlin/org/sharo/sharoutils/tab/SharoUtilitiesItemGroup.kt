package org.sharo.sharoutils.tab

import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import org.sharo.sharoutils.item.Items

class SharoUtilitiesItemGroup : ItemGroup("Sharo Utilities") {
    override fun createIcon(): ItemStack {
        return ItemStack(Items.SHARO_INGOT.get())
    }
}
