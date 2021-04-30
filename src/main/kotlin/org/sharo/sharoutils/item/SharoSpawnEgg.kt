package org.sharo.sharoutils.item

import net.minecraft.block.DispenserBlock
import net.minecraft.dispenser.DefaultDispenseItemBehavior
import net.minecraft.dispenser.IBlockSource
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnReason
import net.minecraft.item.ItemStack
import net.minecraft.item.SpawnEggItem
import net.minecraft.nbt.CompoundNBT
import net.minecraft.util.Direction
import net.minecraftforge.fml.RegistryObject
import org.sharo.sharoutils.entity.SharoEntity
import net.minecraftforge.common.util.Lazy

class SharoSpawnEgg(
    entityTypeSupplier: RegistryObject<EntityType<SharoEntity>>,
    primaryColorIn: Int,
    secondaryColorIn: Int,
    builder: Properties
) : SpawnEggItem(
    entityTypeSupplier.get(),
    primaryColorIn,
    secondaryColorIn,
    builder
) {
    companion object {
        @JvmStatic
        val UNADDED_EGGS: MutableList<SharoSpawnEgg> = arrayListOf()
        @JvmStatic
        fun initSpawnEggs() {
            val dispenseBehavior = object : DefaultDispenseItemBehavior() {
                override fun dispenseStack(source: IBlockSource, stack: ItemStack): ItemStack {
                    val direction = source.blockState[DispenserBlock.FACING]
                    val egg = stack.item as SpawnEggItem
                    val type = egg.getType(stack.tag)
                    when (direction) {
                        Direction.UP ->
                            type.spawn(source.world, stack, null, source.blockPos, SpawnReason.DISPENSER, true, false)
                        Direction.NORTH ->
                            type.spawn(source.world, stack, null, source.blockPos.north(), SpawnReason.DISPENSER, false, false)
                        Direction.EAST ->
                            type.spawn(source.world, stack, null, source.blockPos.east(), SpawnReason.DISPENSER, false, false)
                        Direction.SOUTH ->
                            type.spawn(source.world, stack, null, source.blockPos.south(), SpawnReason.DISPENSER, false, false)
                        Direction.WEST ->
                            type.spawn(source.world, stack, null, source.blockPos.west(), SpawnReason.DISPENSER, false, false)
                        Direction.DOWN ->
                            type.spawn(source.world, stack, null, source.blockPos.down(), SpawnReason.DISPENSER, false, false)
                        null ->
                            Unit
                    }
                    stack.shrink(1)
                    return stack
                }
            }

            for (egg in UNADDED_EGGS) {
                DispenserBlock.registerDispenseBehavior(egg, dispenseBehavior)
            }
            UNADDED_EGGS.clear()
        }
    }

    init {
        UNADDED_EGGS.add(this)
    }

    private val entityTypeSupplier: Lazy<out EntityType<*>> = Lazy.of(entityTypeSupplier::get)

    override fun getType(nbt: CompoundNBT?): EntityType<*> {
        return entityTypeSupplier.get()
    }
}
