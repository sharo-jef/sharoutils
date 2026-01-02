package org.sharo.sharoutils.entity.goal

import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.ai.goal.Goal
import net.minecraft.item.ItemStack
import net.minecraft.particle.ItemStackParticleEffect
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Hand
import net.minecraft.util.math.Vec3d
import org.sharo.sharoutils.entity.SharoEntity
import org.sharo.sharoutils.item.Items

/** HP が少なくなったときに左手に sharo manju を持って食べて回復する Goal 武器は右手に持ったまま維持する */
class EatSharoManjuGoal(private val entity: SharoEntity) : Goal() {

    private var eatingTicks = 0
    private var storedOffhandItem: ItemStack = ItemStack.EMPTY
    private var cooldownTicks = 0

    companion object {
        private const val EATING_DURATION = 32 // 食べるのにかかるティック数（32 ticks = 1.6秒）
        private const val HEALTH_THRESHOLD = 0.4f // HP が 40% 以下になったら食べる
        private const val COOLDOWN_DURATION = 200 // クールダウン時間（200 ticks = 10秒）
    }

    override fun canStart(): Boolean {
        // クールダウン中は開始しない
        if (cooldownTicks > 0) return false

        // HP が閾値以下で、まだ食事中でない場合のみ開始
        if (eatingTicks > 0) return false

        val health = entity.health
        val maxHealth = entity.maxHealth

        return health / maxHealth <= HEALTH_THRESHOLD && health < maxHealth
    }

    override fun shouldContinue(): Boolean {
        // 食事中の場合は続行
        return eatingTicks > 0
    }

    override fun start() {
        // 現在のオフハンドアイテムを保存
        storedOffhandItem = entity.getEquippedStack(EquipmentSlot.OFFHAND).copy()

        // 左手に sharo manju を装備
        entity.equipStack(EquipmentSlot.OFFHAND, ItemStack(Items.SHARO_MANJU))

        // 食事開始（左手を使用中にする）
        entity.setCurrentHand(Hand.OFF_HAND)
        eatingTicks = EATING_DURATION
    }

    override fun tick() {
        // クールダウンを減らす
        if (cooldownTicks > 0) {
            cooldownTicks--
        }

        if (eatingTicks > 0) {
            eatingTicks--

            // 移動を停止
            entity.navigation.stop()

            // 食べかすのパーティクルエフェクトを表示
            // バニラと同じ条件: 使用時間の21.875%を超えてから、4 tickごとに表示
            val elapsedTicks = EATING_DURATION - eatingTicks
            val particleStartThreshold = (EATING_DURATION * 0.21875f).toInt()
            if (elapsedTicks > particleStartThreshold && eatingTicks % 4 == 0) {
                if (entity.isUsingItem) {
                    val activeStack = entity.activeItem
                    if (!activeStack.isEmpty && entity.entityWorld is ServerWorld) {
                        val world = entity.entityWorld as ServerWorld

                        // バニラのspawnItemParticlesと同じロジックをサーバー側で実装
                        // パーティクルをサーバー側から送信してクライアント側で表示させる
                        for (i in 0 until 5) {
                            // 速度ベクトル（ランダム）
                            val velX = (entity.random.nextFloat() - 0.5) * 0.1
                            val velY = entity.random.nextFloat() * 0.1 + 0.1
                            var velocity = Vec3d(velX.toDouble(), velY.toDouble(), 0.0)

                            // エンティティの向きに合わせて回転
                            val pitchRad = -entity.pitch * Math.PI / 180.0
                            val yawRad = -entity.yaw * Math.PI / 180.0
                            velocity = velocity.rotateX(pitchRad.toFloat())
                            velocity = velocity.rotateY(yawRad.toFloat())

                            // スポーン位置（口の前方）
                            val offsetY = -entity.random.nextFloat() * 0.6 - 0.3
                            var position =
                                    Vec3d(
                                            (entity.random.nextFloat() - 0.5) * 0.3,
                                            offsetY.toDouble(),
                                            0.6
                                    )
                            position = position.rotateX(pitchRad.toFloat())
                            position = position.rotateY(yawRad.toFloat())
                            position = position.add(entity.x, entity.eyeY, entity.z)

                            // サーバー側からパーティクルを送信
                            world.spawnParticles(
                                    ItemStackParticleEffect(ParticleTypes.ITEM, activeStack),
                                    position.x,
                                    position.y,
                                    position.z,
                                    1, // count
                                    velocity.x,
                                    velocity.y + 0.05,
                                    velocity.z,
                                    0.0 // speed
                            )
                        }
                    }
                }
            }

            // 食事完了
            if (eatingTicks == 0) {
                // 食事アニメーションを終了
                entity.clearActiveItem()

                // HP を回復（sharo manju の回復量: 20）
                val foodComponent =
                        Items.SHARO_MANJU.components.get(
                                net.minecraft.component.DataComponentTypes.FOOD
                        )
                if (foodComponent != null) {
                    entity.heal(foodComponent.nutrition.toFloat())
                }

                // 左手のアイテムを消費
                val offhandStack = entity.getEquippedStack(EquipmentSlot.OFFHAND)
                offhandStack.decrement(1)

                // 元のオフハンドアイテムに戻す
                entity.equipStack(EquipmentSlot.OFFHAND, storedOffhandItem)
                storedOffhandItem = ItemStack.EMPTY

                // クールダウンを設定
                cooldownTicks = COOLDOWN_DURATION
            }
        }
    }

    override fun stop() {
        // 食事アニメーションを終了
        entity.clearActiveItem()

        // 中断された場合も元に戻す
        if (!storedOffhandItem.isEmpty) {
            entity.equipStack(EquipmentSlot.OFFHAND, storedOffhandItem)
            storedOffhandItem = ItemStack.EMPTY
        }
        eatingTicks = 0
    }
}
