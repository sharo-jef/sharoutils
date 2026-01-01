package org.sharo.sharoutils.common

import java.util.*
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.minecraft.server.network.ServerPlayerEntity
import org.sharo.sharoutils.block.Elevator

object ElevatorEventHandler {
    private val prevSneak: MutableMap<UUID, Boolean> = mutableMapOf()
    private val prevOnGround: MutableMap<UUID, Boolean> = mutableMapOf()

    fun register() {
        ServerTickEvents.END_SERVER_TICK.register { server ->
            server.playerManager.playerList.forEach { player -> handlePlayer(player) }
        }
    }

    private fun handlePlayer(player: ServerPlayerEntity) {
        val uuid = player.uuid
        val currentSneak = player.isSneaking
        val currentOnGround = player.isOnGround

        // Detect sneak press (transition from not sneaking to sneaking)
        val wasSneak = prevSneak[uuid] ?: false
        if (!wasSneak && currentSneak) {
            // Player just started sneaking
            Elevator.onSneak(player)
        }
        prevSneak[uuid] = currentSneak

        // Detect jump (player leaves ground while moving upward)
        val wasOnGround = prevOnGround[uuid] ?: true
        if (wasOnGround && !currentOnGround && player.velocity.y > 0.0) {
            // Player just jumped
            Elevator.onJump(player)
        }
        prevOnGround[uuid] = currentOnGround
    }
}
