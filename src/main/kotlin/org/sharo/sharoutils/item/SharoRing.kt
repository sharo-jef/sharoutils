package org.sharo.sharoutils.item

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.minecraft.item.Item
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.util.Identifier
import net.minecraft.util.Rarity
import org.sharo.sharoutils.Core

class SharoRing :
        Item(
                Settings()
                        .maxCount(1)
                        .rarity(Rarity.EPIC)
                        .registryKey(
                                RegistryKey.of(
                                        RegistryKeys.ITEM,
                                        Identifier.of(Core.MODID, "sharo_ring")
                                )
                        )
        ) {
    companion object {
        @JvmStatic
        fun registerEvents() {
            ServerTickEvents.END_SERVER_TICK.register { server ->
                server.playerManager.playerList.forEach { player ->
                    if (player.isCreative || player.isSpectator) {
                        if (!player.abilities.allowFlying) {
                            player.abilities.allowFlying = true
                        }
                    } else {
                        // Check all inventory slots for the ring
                        var hasRing = false
                        for (slot in 0 until player.inventory.size()) {
                            val stack = player.inventory.getStack(slot)
                            if (stack.item == Items.SHARO_RING) {
                                hasRing = true
                                break
                            }
                        }

                        if (hasRing) {
                            if (!player.abilities.allowFlying) {
                                player.abilities.allowFlying = true
                                player.sendAbilitiesUpdate()
                            }
                        } else {
                            if (player.abilities.allowFlying) {
                                player.abilities.allowFlying = false
                                player.abilities.flying = false
                                player.sendAbilitiesUpdate()
                            }
                        }
                    }
                }
            }
        }
    }
}
