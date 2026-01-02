package org.sharo.sharoutils.entity

import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.enchantment.provider.EnchantmentProviders
import net.minecraft.entity.*
import net.minecraft.entity.ai.RangedAttackMob
import net.minecraft.entity.ai.goal.*
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.mob.*
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.ArrowEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.registry.tag.ItemTags
import net.minecraft.registry.tag.TagKey
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.random.Random
import net.minecraft.world.LocalDifficulty
import net.minecraft.world.ServerWorldAccess
import net.minecraft.world.World

class SharoEntity(type: EntityType<out HostileEntity>, world: World) :
        HostileEntity(type, world), RangedAttackMob, Angerable {

    // Anger management fields
    private var angerEndTime: Long = 0L
    private var angryAt: LazyEntityReference<LivingEntity>? = null

    // Attack goals - stored as fields so we can dynamically switch them
    private val bowAttackGoal =
            object : BowAttackGoal<SharoEntity>(this, 1.0, 20, 15.0f) {
                override fun canStart(): Boolean = isHoldingBow() && super.canStart()
                override fun shouldContinue(): Boolean = isHoldingBow() && super.shouldContinue()
            }

    private val meleeAttackGoal =
            object : MeleeAttackGoal(this, 1.2, false) {
                override fun canStart(): Boolean = !isHoldingBow() && super.canStart()
                override fun shouldContinue(): Boolean = !isHoldingBow() && super.shouldContinue()
            }

    override fun initialize(
            world: ServerWorldAccess,
            difficulty: LocalDifficulty,
            spawnReason: SpawnReason,
            entityData: EntityData?
    ): EntityData? {
        val result = super.initialize(world, difficulty, spawnReason, entityData)

        // Initialize equipment: 80% unarmed, 10% sword, 10% bow
        initEquipment(world.random, difficulty)

        // Apply enchantments to equipped items (like vanilla)
        updateEnchantments(world, world.random, difficulty)

        // Set up AI based on equipment
        updateAttackType()

        // Allow picking up loot based on difficulty
        setCanPickUpLoot(world.random.nextFloat() < 0.55f * difficulty.clampedLocalDifficulty)

        return result
    }

    override fun initEquipment(random: Random, localDifficulty: LocalDifficulty) {
        super.initEquipment(random, localDifficulty)

        // 80% unarmed, 10% sword, 10% bow (no armor)
        val roll = random.nextFloat()

        when {
            roll < 0.8f -> {
                // 80% - Unarmed (素手)
                equipStack(EquipmentSlot.MAINHAND, ItemStack.EMPTY)
            }
            roll < 0.9f -> {
                // 10% - Sword (剣)
                // Choose sword tier based on difficulty
                val swordTier = random.nextInt(3)
                val sword =
                        when (swordTier) {
                            0 -> Items.WOODEN_SWORD
                            1 -> Items.STONE_SWORD
                            else -> Items.IRON_SWORD
                        }
                equipStack(EquipmentSlot.MAINHAND, ItemStack(sword))
            }
            else -> {
                // 10% - Bow (弓)
                equipStack(EquipmentSlot.MAINHAND, ItemStack(Items.BOW))
            }
        }
    }

    /** Apply enchantments to equipment (same as vanilla) */
    override fun updateEnchantments(
            world: ServerWorldAccess,
            random: Random,
            localDifficulty: LocalDifficulty
    ) {
        // Enchant main hand weapon with 25% chance (same as vanilla)
        enchantMainHandItem(world, random, localDifficulty)

        // Enchant armor pieces with 50% chance (but we don't use armor for now)
        for (slot in EquipmentSlot.entries) {
            if (slot.type == EquipmentSlot.Type.HUMANOID_ARMOR) {
                enchantEquipment(world, random, slot, localDifficulty)
            }
        }
    }

    override fun enchantMainHandItem(
            world: ServerWorldAccess,
            random: Random,
            localDifficulty: LocalDifficulty
    ) {
        enchantEquipment(world, EquipmentSlot.MAINHAND, random, 0.25f, localDifficulty)
    }

    override fun enchantEquipment(
            world: ServerWorldAccess,
            random: Random,
            slot: EquipmentSlot,
            localDifficulty: LocalDifficulty
    ) {
        enchantEquipment(world, slot, random, 0.5f, localDifficulty)
    }

    private fun enchantEquipment(
            world: ServerWorldAccess,
            slot: EquipmentSlot,
            random: Random,
            power: Float,
            localDifficulty: LocalDifficulty
    ) {
        val itemStack = getEquippedStack(slot)
        if (!itemStack.isEmpty &&
                        random.nextFloat() < power * localDifficulty.clampedLocalDifficulty
        ) {
            EnchantmentHelper.applyEnchantmentProvider(
                    itemStack,
                    world.registryManager,
                    EnchantmentProviders.MOB_SPAWN_EQUIPMENT,
                    localDifficulty,
                    random
            )
            equipStack(slot, itemStack)
        }
    }

    /**
     * Update AI based on current equipment
     * - If holding bow: use ranged attack
     * - Otherwise: use melee attack
     */
    fun updateAttackType() {
        if (getEntityWorld() != null && !getEntityWorld().isClient) {
            goalSelector.remove(bowAttackGoal)

            if (isHoldingBow()) {
                goalSelector.add(2, bowAttackGoal)
            } else {
                goalSelector.add(3, meleeAttackGoal)
            }
        }
    }

    override fun getPreferredWeapons(): TagKey<Item>? {
        // Prefer skeleton weapons (bows and swords)
        return ItemTags.SKELETON_PREFERRED_WEAPONS
    }

    companion object {
        private val ANGER_TIME_RANGE =
                net.minecraft.util.math.intprovider.UniformIntProvider.create(20, 39)
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
                @Suppress("UNUSED_PARAMETER") random: Random
        ): Boolean {
            // Spawn on solid blocks with sufficient light (like animals)
            return world.getBlockState(pos.down()).isSolidBlock(world, pos.down()) &&
                    world.getBaseLightLevel(pos, 0) > 8
        }
    }

    override fun initGoals() {
        // Basic behavior goals
        goalSelector.add(1, SwimGoal(this))
        // Attack goals are added dynamically by updateAttackType()
        goalSelector.add(4, WanderAroundFarGoal(this, 1.0))
        goalSelector.add(5, LookAtEntityGoal(this, PlayerEntity::class.java, 8.0f))
        goalSelector.add(6, LookAroundGoal(this))

        // Target goals - attack hostile mobs and defend against attacks
        // Universal anger system - attack together like zombie piglins
        // When attacked by any entity (including players), will become angry and attack back
        targetSelector.add(1, RevengeGoal(this).setGroupRevenge(SharoEntity::class.java))
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
        // Use a filter to exclude Creepers and Enderman from general hostile targeting
        targetSelector.add(
                3,
                object : ActiveTargetGoal<HostileEntity>(this, HostileEntity::class.java, true) {
                    override fun canStart(): Boolean {
                        if (!super.canStart()) return false
                        val potentialTarget = this.targetEntity
                        // Exclude Creepers (爆発するので) and Enderman (already handled above)
                        return potentialTarget !is CreeperEntity &&
                                potentialTarget !is EndermanEntity
                    }
                }
        )
    }

    private fun isHoldingBow(): Boolean {
        return mainHandStack.isOf(Items.BOW) || offHandStack.isOf(Items.BOW)
    }

    override fun onEquipStack(slot: EquipmentSlot, oldStack: ItemStack, newStack: ItemStack) {
        super.onEquipStack(slot, oldStack, newStack)
        if (!getEntityWorld().isClient) {
            // When equipment changes, update attack type
            updateAttackType()
        }
    }

    override fun readCustomData(view: net.minecraft.storage.ReadView) {
        super.readCustomData(view)
        readAngerFromData(getEntityWorld(), view)
        // Update attack type after loading from NBT
        updateAttackType()
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
        if (!mainHandStack.isOf(Items.BOW) && !offHandStack.isOf(Items.BOW)) {
            return
        }

        val itemStack = getProjectileType(mainHandStack)
        val arrow = ArrowEntity(getEntityWorld(), this, itemStack, null)
        val d = target.x - x
        val e = target.getEyeY() - 0.1 - arrow.y
        val f = target.z - z
        val g = Math.sqrt(d * d + f * f)

        arrow.setVelocity(
                d,
                e + g * 0.20000000298023224,
                f,
                1.6f,
                14.0f - getEntityWorld().difficulty.id * 4.0f
        )

        playSound(
                SoundEvents.ENTITY_SKELETON_SHOOT,
                1.0f,
                1.0f / (random.nextFloat() * 0.4f + 0.8f)
        )
        getEntityWorld().spawnEntity(arrow)
    }

    override fun canUseRangedWeapon(weapon: ItemStack): Boolean {
        return weapon.isOf(Items.BOW)
    }

    override fun getAmbientSound(): SoundEvent = SoundEvents.ENTITY_ZOMBIE_AMBIENT

    override fun getHurtSound(source: DamageSource): SoundEvent = SoundEvents.ENTITY_ZOMBIE_HURT

    override fun getDeathSound(): SoundEvent = SoundEvents.ENTITY_ZOMBIE_DEATH

    // Damage handling - become angry when attacked
    override fun damage(world: ServerWorld, source: DamageSource, amount: Float): Boolean {
        val result = super.damage(world, source, amount)

        if (result && !world.isClient) {
            // Become angry when attacked by a living entity
            val attacker = source.attacker
            if (attacker is LivingEntity && attacker !is SharoEntity) {
                this.chooseRandomAngerTime()
                this.setAngryAt(LazyEntityReference.of(attacker))

                // Spread anger to nearby Sharo entities immediately (like zombie piglins)
                val nearbySharers =
                        world.getEntitiesByClass(
                                SharoEntity::class.java,
                                this.boundingBox.expand(32.0)
                        ) { it != this }

                for (sharo in nearbySharers) {
                    sharo.chooseRandomAngerTime()
                    sharo.setAngryAt(LazyEntityReference.of(attacker))
                    // Also set target directly for immediate attack
                    sharo.target = attacker
                }
            }
        }

        return result
    }

    // Angerable interface implementation
    override fun getAngerEndTime(): Long {
        return this.angerEndTime
    }

    override fun setAngerEndTime(time: Long) {
        this.angerEndTime = time
    }

    override fun chooseRandomAngerTime() {
        val angerTime = ANGER_TIME_RANGE.get(this.random) * 20 // Convert to ticks
        this.angerEndTime = this.getEntityWorld().time + angerTime
    }

    override fun setAngryAt(angryAt: LazyEntityReference<LivingEntity>?) {
        this.angryAt = angryAt
    }

    override fun getAngryAt(): LazyEntityReference<LivingEntity>? {
        return this.angryAt
    }

    override fun canTarget(type: EntityType<*>?): Boolean {
        // Don't target other Sharo entities
        return type != EntityTypes.SHARO && super.canTarget(type)
    }

    // Tick method to update anger
    override fun mobTick(world: ServerWorld) {
        super.mobTick(world)

        // Clear anger if time expired
        if (this.angerEndTime > 0 && world.time >= this.angerEndTime) {
            this.angerEndTime = 0L
            this.angryAt = null
        }
    }

    // NBT serialization for anger state
    override fun writeCustomData(view: net.minecraft.storage.WriteView) {
        super.writeCustomData(view)
        writeAngerToData(view)
    }
}
