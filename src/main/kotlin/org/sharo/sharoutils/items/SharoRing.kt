package org.sharo.sharoutils.items

import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.item.Rarity
import net.minecraftforge.event.TickEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import org.sharo.sharoutils.SharoUtilities
import org.sharo.sharoutils.items.Items.Companion.SHARO_RING

@Mod.EventBusSubscriber(modid = SharoUtilities.MODID)
class SharoRing : Item {
    companion object {
        @JvmStatic
        @SubscribeEvent
        fun tick(event: TickEvent.PlayerTickEvent) {
            val player = event.player
            if (player.isCreative || player.isSpectator) {
                if (!player.abilities.allowFlying) {
                    player.abilities.allowFlying = true
                }
            } else {
                if (player.inventory.hasItemStack(ItemStack(SHARO_RING))) {
                    if (!player.abilities.allowFlying) {
                        player.abilities.allowFlying = true
                    }
                } else {
                    if (player.abilities.allowFlying) {
                        player.abilities.allowFlying = false
                        player.abilities.isFlying = false
                    }
                }
            }
        }
    }

    constructor() : super(
        Properties()
            .group(ItemGroup.MATERIALS)
            .maxStackSize(1)
            .rarity(Rarity.EPIC)
    ) {
        setRegistryName("sharo_ring")
    }
}
