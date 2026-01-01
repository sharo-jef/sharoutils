package org.sharo.sharoutils.entity

import net.minecraft.entity.*
import net.minecraft.entity.ai.RangedAttackMob
import net.minecraft.entity.ai.goal.*
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.mob.*
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.ArrowEntity
import net.minecraft.item.Items
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.random.Random
import net.minecraft.world.ServerWorldAccess
import net.minecraft.world.World

class SharoEntity(type: EntityType<out HostileEntity>, world: World) :
        HostileEntity(type, world), RangedAttackMob {

    private var hasBow = false

    override fun initialize(
            world: net.minecraft.world.ServerWorldAccess,
            difficulty: net.minecraft.world.LocalDifficulty,
            spawnReason: SpawnReason,
            entityData: net.minecraft.entity.EntityData?
    ): net.minecraft.entity.EntityData? {
        // 50% chance to spawn with a bow
        hasBow = this.random.nextBoolean()
        if (hasBow) {
            this.setStackInHand(
                    net.minecraft.util.Hand.MAIN_HAND,
                    net.minecraft.item.ItemStack(Items.BOW)
            )
        }
        return super.initialize(world, difficulty, spawnReason, entityData)
    }

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

        @JvmStatic
        fun canSpawn(
                type: EntityType<out SharoEntity>,
                world: ServerWorldAccess,
                spawnReason: SpawnReason,
                pos: BlockPos,
                random: Random
        ): Boolean {
            // Spawn on solid blocks with sufficient light (like animals)
            return world.getBlockState(pos.down()).isSolid && world.getBaseLightLevel(pos, 0) > 8
        }
    }

    override fun initGoals() {
        // Basic behavior goals
        goalSelector.add(1, SwimGoal(this))
        // Attack goals - check for bow on tick
        goalSelector.add(
                2,
                object : BowAttackGoal<SharoEntity>(this, 1.0, 20, 15.0f) {
                    override fun canStart(): Boolean {
                        return this@SharoEntity.isHoldingBow() && super.canStart()
                    }
                    override fun shouldContinue(): Boolean {
                        return this@SharoEntity.isHoldingBow() && super.shouldContinue()
                    }
                }
        )
        goalSelector.add(
                3,
                object : MeleeAttackGoal(this, 1.2, false) {
                    override fun canStart(): Boolean {
                        return !this@SharoEntity.isHoldingBow() && super.canStart()
                    }
                    override fun shouldContinue(): Boolean {
                        return !this@SharoEntity.isHoldingBow() && super.shouldContinue()
                    }
                }
        )
        goalSelector.add(4, WanderAroundFarGoal(this, 1.0))
        goalSelector.add(5, LookAtEntityGoal(this, PlayerEntity::class.java, 8.0f))
        goalSelector.add(6, LookAroundGoal(this))

        // Target goals - attack hostile mobs and defend against attacks
        // Don't attack other Sharo entities when taking revenge
        targetSelector.add(
                1,
                object : RevengeGoal(this) {
                    override fun canStart(): Boolean {
                        return super.canStart() && target !is SharoEntity
                    }
                    override fun setMobEntityTarget(mob: MobEntity?, target: LivingEntity?) {
                        if (target !is SharoEntity) {
                            super.setMobEntityTarget(mob, target)
                        }
                    }
                }
        )
        targetSelector.add(2, ActiveTargetGoal(this, ZombieEntity::class.java, true))
        targetSelector.add(2, ActiveTargetGoal(this, SkeletonEntity::class.java, true))
        targetSelector.add(2, ActiveTargetGoal(this, SpiderEntity::class.java, true))
        // Only target Enderman if not holding a bow (they teleport away from arrows)
        targetSelector.add(
                2,
                object : ActiveTargetGoal<EndermanEntity>(this, EndermanEntity::class.java, true) {
                    override fun canStart(): Boolean {
                        return !this@SharoEntity.isHoldingBow() && super.canStart()
                    }
                }
        )
        targetSelector.add(2, ActiveTargetGoal(this, WitchEntity::class.java, true))
        targetSelector.add(2, ActiveTargetGoal(this, SlimeEntity::class.java, true))
        // Use a filter to exclude Creepers from general hostile targeting
        targetSelector.add(
                3,
                object : ActiveTargetGoal<HostileEntity>(this, HostileEntity::class.java, true) {
                    override fun canStart(): Boolean {
                        if (!super.canStart()) return false
                        val currentTarget = this.mob.target
                        return currentTarget !is CreeperEntity && currentTarget !is EndermanEntity
                    }
                }
        )
    }

    private fun isHoldingBow(): Boolean {
        return this.mainHandStack.isOf(Items.BOW) || this.offHandStack.isOf(Items.BOW)
    }

    override fun tryAttack(world: net.minecraft.server.world.ServerWorld, target: Entity): Boolean {
        // Don't attack other Sharo entities
        if (target is SharoEntity) {
            return false
        }
        val flag = super.tryAttack(world, target)
        if (flag && this.mainHandStack.isEmpty && target is LivingEntity) {
            @Suppress("UNUSED_VARIABLE") val f = world.getLocalDifficulty(blockPos).localDifficulty
            // Effects can be added here if needed
        }
        return flag
    }

    override fun setTarget(target: LivingEntity?) {
        // Don't target other Sharo entities
        if (target is SharoEntity) {
            return
        }
        super.setTarget(target)
    }

    override fun shootAt(target: LivingEntity, pullProgress: Float) {
        // Only shoot if holding a bow
        if (!this.mainHandStack.isOf(Items.BOW) && !this.offHandStack.isOf(Items.BOW)) {
            return
        }

        val itemStack = this.getProjectileType(this.mainHandStack)
        val arrow = ArrowEntity(this.getEntityWorld(), this, itemStack, null)
        val d = target.x - this.x
        val e = target.getEyeY() - 0.1 - arrow.y
        val f = target.z - this.z
        val g = Math.sqrt(d * d + f * f)

        arrow.setVelocity(
                d,
                e + g * 0.20000000298023224,
                f,
                1.6f,
                14.0f - this.getEntityWorld().difficulty.id * 4.0f
        )

        this.playSound(
                SoundEvents.ENTITY_SKELETON_SHOOT,
                1.0f,
                1.0f / (this.random.nextFloat() * 0.4f + 0.8f)
        )
        this.getEntityWorld().spawnEntity(arrow)
    }

    override fun canUseRangedWeapon(weapon: net.minecraft.item.ItemStack): Boolean {
        return weapon.isOf(Items.BOW)
    }

    override fun getAmbientSound(): SoundEvent = SoundEvents.ENTITY_ZOMBIE_AMBIENT

    override fun getHurtSound(source: DamageSource): SoundEvent = SoundEvents.ENTITY_ZOMBIE_HURT

    override fun getDeathSound(): SoundEvent = SoundEvents.ENTITY_ZOMBIE_DEATH
}
