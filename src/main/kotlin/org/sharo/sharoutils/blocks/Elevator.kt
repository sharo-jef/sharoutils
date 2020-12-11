package org.sharo.sharoutils.blocks

import net.minecraft.block.Block
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.ResourceLocation
import net.minecraft.util.SoundCategory
import net.minecraft.util.SoundEvent
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.common.ToolType
import net.minecraftforge.event.TickEvent
import net.minecraftforge.event.entity.living.LivingEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import org.sharo.sharoutils.SharoUtilities
import java.util.*

@Mod.EventBusSubscriber(modid = SharoUtilities.MODID)
class Elevator : Block {
    companion object {
        @JvmStatic
        var prevPlayers: MutableMap<World, MutableMap<UUID, Boolean>> = mutableMapOf<World, MutableMap<UUID, Boolean>>()

        @JvmStatic
        @SubscribeEvent
        fun tickHandler(event: TickEvent.PlayerTickEvent) {
            val prevSneak = prevPlayers[event.player.world]?.get(event.player.uniqueID)
            if (
                prevSneak != event.player.isSneaking
                && event.player.isSneaking
            ) {
                sneak(event.player)
            }
            if (prevPlayers[event.player.world] == null) {
                prevPlayers[event.player.world] = mutableMapOf<UUID, Boolean>()
            }
            prevPlayers[event.player.world]
                ?.set(
                    event.player.uniqueID,
                    event.player.isSneaking
                )
        }

        @JvmStatic
        @SubscribeEvent
        fun jumpHandler(event: LivingEvent.LivingJumpEvent) {
            val entity = event.entity
            if (entity is PlayerEntity) {
                jump(entity)
            }
        }

        @JvmStatic
        fun sneak(player: PlayerEntity) {
            if (isOnElevator(player)) {
                val y = getAnotherFloor(player.world, player.position, -1)
                if (y != -1) {
                    if (player.attemptTeleport(player.posX, (y + 1).toDouble(), player.posZ, true)) {
                        player.playSound(SoundEvent(ResourceLocation("minecraft:entity.enderman.teleport")), .1f, 1f)
                    }
                }
            }
        }

        @JvmStatic
        fun jump(player: PlayerEntity) {
            if (isOnElevator(player)) {
                val y = getAnotherFloor(player.world, player.position, 1)
                if (y != -1) {
                    if (player.attemptTeleport(player.posX, (y + 1).toDouble(), player.posZ, true)) {
                        player.playSound(SoundEvent(ResourceLocation("minecraft:entity.enderman.teleport")), .1f, 1f)
                    }
                }
            }
        }

        @JvmStatic
        fun isOnElevator(player: PlayerEntity): Boolean {
            val world = player.world
            val blockPos = BlockPos(player.posX, player.posY - 1, player.posZ)
            val blockState = world.getBlockState(blockPos)
            if (blockState.block is Elevator) {
                return true
            }
            return false
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
                    while (y <= 1024) {
                        if (worldIn.getBlockState(BlockPos(pos.x, y, pos.z)).block is Elevator) {
                            anotherFloor = y
                            break
                        }
                        y++
                    }
                }
                -1 -> {
                    y -= 2
                    while (y >= 0) {
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
    constructor() : super(
        Properties
            .create(Material.IRON)
            .hardnessAndResistance(3f, 10f)
            .harvestLevel(0)
            .harvestTool(ToolType.PICKAXE)
            .sound(SoundType.GROUND)
    ) {
        setRegistryName("elevator")
    }
}
