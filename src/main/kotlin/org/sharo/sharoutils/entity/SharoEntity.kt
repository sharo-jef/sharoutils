package org.sharo.sharoutils.entity

import net.minecraft.block.Blocks
import net.minecraft.entity.*
import net.minecraft.entity.ai.attributes.AttributeModifierMap
import net.minecraft.entity.ai.attributes.Attributes
import net.minecraft.entity.ai.goal.*
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity
import net.minecraft.entity.monster.SkeletonEntity
import net.minecraft.entity.monster.ZombifiedPiglinEntity
import net.minecraft.entity.passive.IronGolemEntity
import net.minecraft.entity.passive.TurtleEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.potion.EffectInstance
import net.minecraft.potion.Effects
import net.minecraft.util.*
import net.minecraft.util.math.BlockPos
import net.minecraft.world.*
import org.sharo.sharoutils.Core
import org.sharo.sharoutils.config.Config

class SharoEntity(
    type: EntityType<out SkeletonEntity>,
    worldIn: World
) : SkeletonEntity(type, worldIn) {
    companion object {
        @JvmStatic
        fun setCustomAttributes(): AttributeModifierMap.MutableAttribute {
            return MobEntity.registerAttributes()
                .createMutableAttribute(Attributes.MAX_HEALTH, Config.sharoHealth.toDouble())
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, .25)
                .createMutableAttribute(Attributes.FOLLOW_RANGE, 32.0)
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 3.0)
                .createMutableAttribute(Attributes.ATTACK_KNOCKBACK, 1.0)
                .createMutableAttribute(Attributes.ATTACK_SPEED, 10.0)
        }
    }

    override fun registerGoals() {
        goalSelector.addGoal(4, AttackTurtleEggGoal(this, 1.0, 3))
        goalSelector.addGoal(8, LookAtGoal(this, PlayerEntity::class.java, 8.0f))
        goalSelector.addGoal(8, LookRandomlyGoal(this))
        goalSelector.addGoal(2, MeleeAttackGoal(this, 1.0, false))
        goalSelector.addGoal(7, WaterAvoidingRandomWalkingGoal(this, 1.0))
        targetSelector.addGoal(
            1, HurtByTargetGoal(this).setCallsForHelp(
                ZombifiedPiglinEntity::class.java
            )
        )
        targetSelector.addGoal(2, NearestAttackableTargetGoal(this, PlayerEntity::class.java, true))
        targetSelector.addGoal(
            3, NearestAttackableTargetGoal(
                this,
                AbstractVillagerEntity::class.java, false
            )
        )
        targetSelector.addGoal(
            3, NearestAttackableTargetGoal(
                this,
                IronGolemEntity::class.java, true
            )
        )
        targetSelector.addGoal(
            5, NearestAttackableTargetGoal(
                this,
                TurtleEntity::class.java, 10, true, false, TurtleEntity.TARGET_DRY_BABY
            )
        )
    }

    override fun attackEntityAsMob(entityIn: Entity): Boolean {
        val flag = super.attackEntityAsMob(entityIn)
        if (flag && this.heldItemMainhand.isEmpty && entityIn is LivingEntity) {
            val f = world.getDifficultyForLocation(position).additionalDifficulty
            entityIn.addPotionEffect(EffectInstance(Effects.HUNGER, 140 * f.toInt()))
        }
        return flag
    }

    override fun getLootTable(): ResourceLocation
        = ResourceLocation(Core.MODID, "sharo_loot_table")

    override fun getAmbientSound(): SoundEvent
        = SoundEvents.ENTITY_ZOMBIE_AMBIENT

    override fun getHurtSound(damageSourceIn: DamageSource): SoundEvent
        = SoundEvents.ENTITY_ZOMBIE_HURT

    override fun getDeathSound(): SoundEvent
        = SoundEvents.ENTITY_ZOMBIE_DEATH

    override fun getStepSound(): SoundEvent
        = SoundEvents.ENTITY_ZOMBIE_STEP

    override fun getCreatureAttribute(): CreatureAttribute
         = CreatureAttribute.UNDEAD

    override fun setEquipmentBasedOnDifficulty(difficulty: DifficultyInstance) {}

    internal class AttackTurtleEggGoal(creatureIn: CreatureEntity?, speed: Double, yMax: Int) :
        BreakBlockGoal(Blocks.TURTLE_EGG, creatureIn, speed, yMax) {
        override fun playBreakingSound(worldIn: IWorld, pos: BlockPos) {
            worldIn.playSound(
                null as PlayerEntity?,
                pos,
                SoundEvents.ENTITY_ZOMBIE_DESTROY_EGG,
                SoundCategory.HOSTILE,
                0.5f,
                0.9f
            )
        }

        override fun playBrokenSound(worldIn: World, pos: BlockPos) {
            worldIn.playSound(
                null as PlayerEntity?,
                pos,
                SoundEvents.ENTITY_TURTLE_EGG_BREAK,
                SoundCategory.BLOCKS,
                0.7f,
                0.9f + worldIn.rand.nextFloat() * 0.2f
            )
        }

        override fun getTargetDistanceSq(): Double {
            return 1.14
        }
    }
}
