package org.sharo.sharoutils.item

import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.Rarity
import net.minecraftforge.event.TickEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import org.sharo.sharoutils.Core
import org.sharo.sharoutils.item.Items.Companion.SHARO_RING

@Mod.EventBusSubscriber(modid = Core.MODID)
class SharoRing : Item(
    Properties()
        .group(Core.ITEM_GROUP)
        .maxStackSize(1)
        .rarity(Rarity.EPIC)
) {
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
                if (player.inventory.hasItemStack(ItemStack(SHARO_RING.get()))) {
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
}
