package org.sharo.sharoutils.entity

import net.minecraft.entity.*
import net.minecraft.entity.ai.goal.*
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.mob.MobEntity
import net.minecraft.entity.mob.PathAwareEntity
import net.minecraft.entity.mob.ZombifiedPiglinEntity
import net.minecraft.entity.passive.IronGolemEntity
import net.minecraft.entity.passive.MerchantEntity
import net.minecraft.entity.passive.TurtleEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import net.minecraft.world.World

class SharoEntity(type: EntityType<out PathAwareEntity>, world: World) :
        PathAwareEntity(type, world) {
    companion object {
        @JvmStatic
        fun createSharoAttributes(): DefaultAttributeContainer.Builder {
            return MobEntity.createMobAttributes()
                    .add(EntityAttributes.MAX_HEALTH, 20.0)
                    .add(EntityAttributes.MOVEMENT_SPEED, 0.25)
                    .add(EntityAttributes.FOLLOW_RANGE, 32.0)
                    .add(EntityAttributes.ATTACK_DAMAGE, 3.0)
                    .add(EntityAttributes.ATTACK_KNOCKBACK, 1.0)
        }
    }

    override fun initGoals() {
        goalSelector.add(4, BreakDoorGoal(this) { true })
        goalSelector.add(8, LookAtEntityGoal(this, PlayerEntity::class.java, 8.0f))
        goalSelector.add(8, LookAroundGoal(this))
        goalSelector.add(2, MeleeAttackGoal(this, 1.0, false))
        goalSelector.add(7, WanderAroundFarGoal(this, 1.0))
        targetSelector.add(1, RevengeGoal(this, ZombifiedPiglinEntity::class.java))
        targetSelector.add(2, ActiveTargetGoal(this, PlayerEntity::class.java, true))
        targetSelector.add(3, ActiveTargetGoal(this, MerchantEntity::class.java, false))
        targetSelector.add(3, ActiveTargetGoal(this, IronGolemEntity::class.java, true))
        targetSelector.add(
                5,
                ActiveTargetGoal(this, TurtleEntity::class.java, 10, true, false) {
                        entity: LivingEntity,
                        world: net.minecraft.server.world.ServerWorld ->
                    entity is TurtleEntity && entity.isBaby
                }
        )
    }

    override fun tryAttack(world: net.minecraft.server.world.ServerWorld, target: Entity): Boolean {
        val flag = super.tryAttack(world, target)
        if (flag && this.mainHandStack.isEmpty && target is LivingEntity) {
            val f = world.getLocalDifficulty(blockPos).localDifficulty
            // Effects can be added here if needed
        }
        return flag
    }

    override fun getAmbientSound(): SoundEvent = SoundEvents.ENTITY_ZOMBIE_AMBIENT

    override fun getHurtSound(source: DamageSource): SoundEvent = SoundEvents.ENTITY_ZOMBIE_HURT

    override fun getDeathSound(): SoundEvent = SoundEvents.ENTITY_ZOMBIE_DEATH
}
