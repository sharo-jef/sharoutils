package org.sharo.sharoutils.block

import java.util.*
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import org.sharo.sharoutils.Core

class Elevator :
        Block(
                Settings.copy(Blocks.IRON_BLOCK)
                        .strength(3f, 10f)
                        .registryKey(
                                RegistryKey.of(
                                        RegistryKeys.BLOCK,
                                        Identifier.of(Core.MODID, "elevator")
                                )
                        )
        ) {
    companion object {
        @JvmStatic val prevSneak: MutableMap<UUID, Boolean> = mutableMapOf()

        @JvmStatic val prevJump: MutableMap<UUID, Boolean> = mutableMapOf()

        @JvmStatic
        fun registerEvents() {
            // Player tick events are handled differently in Fabric
            // We'll use fabric lifecycle events
        }

        @JvmStatic
        fun onSneak(player: PlayerEntity) {
            if (player !is net.minecraft.server.network.ServerPlayerEntity) return
            if (isOnElevator(player)) {
                val world =
                        (player as net.minecraft.entity.Entity).entityWorld as?
                                net.minecraft.server.world.ServerWorld
                                ?: return
                val y = getAnotherFloor(world, player.blockPos, -1)
                if (y != -1) {
                    // Teleport using the simple method
                    player.teleport(player.x, (y + 1).toDouble(), player.z, true)
                    player.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f)
                }
            }
        }

        @JvmStatic
        fun onJump(player: PlayerEntity) {
            if (player !is net.minecraft.server.network.ServerPlayerEntity) return
            if (isOnElevator(player)) {
                val world =
                        (player as net.minecraft.entity.Entity).entityWorld as?
                                net.minecraft.server.world.ServerWorld
                                ?: return
                val y = getAnotherFloor(world, player.blockPos, 1)
                if (y != -1) {
                    // Teleport using the simple method
                    player.teleport(player.x, (y + 1).toDouble(), player.z, true)
                    player.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f)
                }
            }
        }

        @JvmStatic
        fun isOnElevator(player: PlayerEntity): Boolean {
            val world = (player as net.minecraft.entity.Entity).entityWorld
            val blockPos = BlockPos.ofFloored(player.x, player.y - 1, player.z)
            val blockState = world.getBlockState(blockPos)
            return blockState.block is Elevator
        }

        /**
         * @param direction 1: +y, -1: -y
         * @return -1: not found, other: y coord of floor
         */
        @JvmStatic
        fun getAnotherFloor(worldIn: World, pos: BlockPos, direction: Int): Int {
            var y = pos.y
            var anotherFloor = -1
            when (direction) {
                1 -> {
                    y++ // Start searching from the next block
                    while (y < worldIn.height) {
                        if (worldIn.getBlockState(BlockPos(pos.x, y, pos.z)).block is Elevator) {
                            anotherFloor = y
                            break
                        }
                        y++
                    }
                }
                -1 -> {
                    y -= 2 // Start searching 2 blocks below
                    while (y >= worldIn.bottomY) {
                        if (worldIn.getBlockState(BlockPos(pos.x, y, pos.z)).block is Elevator) {
                            anotherFloor = y
                            break
                        }
                        y--
                    }
                }
            }
            return anotherFloor
        }
    }
}
