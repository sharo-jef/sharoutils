package org.sharo.sharoutils.blocks

import net.minecraft.block.BlockState
import net.minecraft.block.GrassBlock
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnReason
import net.minecraft.util.math.BlockPos
import net.minecraft.world.server.ServerWorld
import net.minecraftforge.common.ToolType
import net.minecraftforge.fml.common.Mod
import org.sharo.sharoutils.SharoUtilities
import java.util.*

@Mod.EventBusSubscriber(modid = SharoUtilities.MODID)
class SharoEarth : GrassBlock {
    constructor() : super(
        Properties
            .create(Material.ROCK)
            .hardnessAndResistance(3f, 10f)
            .harvestLevel(1)
            .harvestTool(ToolType.SHOVEL)
            .sound(SoundType.WET_GRASS)
            .tickRandomly()
    ) {
        setRegistryName("sharo_earth")
    }

    override fun randomTick(state: BlockState, worldIn: ServerWorld, pos: BlockPos, random: Random) {
        super.randomTick(state, worldIn, pos, random)
        val entityTypes = arrayOf(
            EntityType.SKELETON,
            EntityType.ZOMBIE,
            EntityType.SPIDER,
            EntityType.CREEPER,
            EntityType.ENDERMAN
        )
        val rand = Random(System.currentTimeMillis())
        val entityType = entityTypes[rand.nextInt(entityTypes.size)]
        for (i in 1..rand.nextInt(3)) {
            entityType.spawn(worldIn, null, null, pos, SpawnReason.EVENT, true, false)
        }
    }
}
