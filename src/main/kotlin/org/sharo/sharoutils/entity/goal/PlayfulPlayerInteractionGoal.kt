package org.sharo.sharoutils.entity.goal

import java.util.*
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.ai.goal.Goal
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.server.world.ServerWorld
import org.sharo.sharoutils.config.ModConfig
import org.sharo.sharoutils.entity.SharoEntity
import org.sharo.sharoutils.item.Items

/** ランダムウォーク中にプレイヤーが近くにいたら、超低確率で 近づいて「屈伸」「sharo饅頭を投げる」「素手で殴る(0ダメージ)」のいずれかをランダムに行い、走って逃げる */
class PlayfulPlayerInteractionGoal(private val entity: SharoEntity) : Goal() {

    private var targetPlayer: PlayerEntity? = null
    private var actionState = ActionState.IDLE
    private var selectedAction: PlayfulAction? = null
    private var actionTick = 0
    private var crouchCount = 0
    private var previousWeapon: ItemStack? = null

    // ランダムに選択されるアクションの種類
    private enum class PlayfulAction {
        CROUCH, // 屈伸
        GIVE_ITEM, // アイテム投げ
        PUNCH // 素手で殴る
    }

    // 各アクション状態
    private enum class ActionState {
        IDLE, // 待機中
        APPROACHING, // プレイヤーに近づき中
        PERFORMING, // アクション実行中
        FLEEING // 逃走中
    }

    init {
        controls = EnumSet.of(Control.MOVE, Control.LOOK)
    }

    override fun canStart(): Boolean {
        // ターゲットがいる場合は発動しない（戦闘中は発動しない）
        if (entity.target != null) return false

        // 既にアクション中なら継続
        if (actionState != ActionState.IDLE) return true

        // 超低確率でチェック（設定から取得）
        if (entity.getEntityWorld().random.nextDouble() > ModConfig.INSTANCE.playfulTriggerChance)
                return false

        // 近くのプレイヤーを探す（24ブロック以内）
        val nearbyPlayer = entity.getEntityWorld().getClosestPlayer(entity, 24.0)
        if (nearbyPlayer != null && nearbyPlayer.isAlive && !nearbyPlayer.isSpectator) {
            targetPlayer = nearbyPlayer
            return true
        }

        return false
    }

    override fun shouldContinue(): Boolean {
        // アクション中は継続
        if (actionState != ActionState.IDLE) return true

        // プレイヤーが遠くに行ったり死んだら終了
        val player = targetPlayer ?: return false
        return player.isAlive &&
                !player.isSpectator &&
                entity.squaredDistanceTo(player) < 900.0 // 30ブロック以内
    }

    override fun start() {
        // 武器を仕舞う（全アクションで武器不要）
        val currentWeapon = entity.getEquippedStack(EquipmentSlot.MAINHAND)
        if (!currentWeapon.isEmpty) {
            previousWeapon = currentWeapon.copy()
            entity.equipStack(EquipmentSlot.MAINHAND, ItemStack.EMPTY)
        }

        // ランダムにアクションを選択
        selectedAction = PlayfulAction.entries.random()
        actionState = ActionState.APPROACHING
        actionTick = 0
        crouchCount = 0
    }

    override fun stop() {
        // 武器を元に戻す
        if (previousWeapon != null) {
            entity.equipStack(EquipmentSlot.MAINHAND, previousWeapon)
            previousWeapon = null
        }

        // ポーズとスニーク状態をリセット
        entity.isSneaking = false
        entity.setPose(net.minecraft.entity.EntityPose.STANDING)

        targetPlayer = null
        selectedAction = null
        actionState = ActionState.IDLE
        actionTick = 0
        crouchCount = 0
        entity.navigation.stop()
    }

    override fun tick() {
        val player = targetPlayer ?: return

        // プレイヤーを見る
        entity.lookControl.lookAt(player, 30.0f, 30.0f)

        when (actionState) {
            ActionState.APPROACHING -> tickApproaching(player)
            ActionState.PERFORMING -> tickPerforming(player)
            ActionState.FLEEING -> tickFleeing(player)
            ActionState.IDLE -> {}
        }

        actionTick++
    }

    private fun tickApproaching(player: PlayerEntity) {
        val distance = entity.squaredDistanceTo(player)

        // プレイヤーに近づく（2ブロック以内を目標）
        if (distance > 4.0) {
            entity.navigation.startMovingTo(player, 1.0)
        } else {
            // 十分近づいたら選択されたアクションを実行
            entity.navigation.stop()
            actionState = ActionState.PERFORMING
            actionTick = 0
            crouchCount = 0 // アクション開始時にリセット
        }
    }

    private fun tickPerforming(player: PlayerEntity) {
        when (selectedAction) {
            PlayfulAction.CROUCH -> performCrouch()
            PlayfulAction.GIVE_ITEM -> performGiveItem(player)
            PlayfulAction.PUNCH -> performPunch(player)
            null -> {} // 念のため
        }
    }

    private fun performCrouch() {
        // 1ティックごとに超高速で屈伸（上下動作）
        // スニークのオンオフで屈伸を表現
        val shouldCrouch = (actionTick % 2) == 0
        entity.isSneaking = shouldCrouch

        // ポーズも設定して視覚的に屈伸
        if (shouldCrouch) {
            entity.setPose(net.minecraft.entity.EntityPose.CROUCHING)
        } else {
            entity.setPose(net.minecraft.entity.EntityPose.STANDING)
        }

        // 8回屈伸したら逃走へ (16ティック = 0.8秒)
        if (actionTick >= 16) {
            entity.isSneaking = false
            entity.setPose(net.minecraft.entity.EntityPose.STANDING)
            actionState = ActionState.FLEEING
            actionTick = 0
        }
    }

    private fun performGiveItem(player: PlayerEntity) {
        // 20ティック待ってからアイテムを投げる
        if (actionTick >= 20) {
            // sharo饅頭を投げる
            val sharoManju = ItemStack(Items.SHARO_MANJU)

            // ServerWorldでアイテムをドロップ
            if (entity.getEntityWorld() is ServerWorld) {
                val serverWorld = entity.getEntityWorld() as ServerWorld
                entity.dropStack(serverWorld, sharoManju, 1.0f)
            }

            actionState = ActionState.FLEEING
            actionTick = 0
        }
    }

    private fun performPunch(player: PlayerEntity) {
        // 1ティック目に殴る（武器は既にstart()で仕舞われている）
        if (actionTick == 1) {
            // 実際にプレイヤーを攻撃する（ダメージ0.1）
            if (entity.getEntityWorld() is ServerWorld) {
                val serverWorld = entity.getEntityWorld() as ServerWorld

                // 攻撃モーション
                entity.swingHand(net.minecraft.util.Hand.MAIN_HAND)

                // tryAttackを使って実際に攻撃（ダメージ0.1で微小ダメージ）
                // 0.0fだと攻撃判定が発生しないため0.1fに設定
                val damageSource = serverWorld.damageSources.mobAttack(entity)
                player.damage(serverWorld, damageSource, 0.1f)

                // ノックバック効果を追加
                val dx = player.x - entity.x
                val dz = player.z - entity.z
                player.takeKnockback(0.2, dx, dz)

                // サウンド
                serverWorld.playSound(
                        null,
                        entity.blockPos,
                        net.minecraft.sound.SoundEvents.ENTITY_PLAYER_ATTACK_WEAK,
                        entity.soundCategory,
                        1.0f,
                        1.0f
                )
            }
        }

        // 2ティック後に即座に逃走開始
        if (actionTick >= 2) {
            actionState = ActionState.FLEEING
            actionTick = 0
        }
    }

    private fun tickFleeing(player: PlayerEntity) {
        // プレイヤーから逃げる（20ブロック離れるまで）
        val distance = entity.squaredDistanceTo(player)

        if (distance < 400.0) { // 20ブロック未満
            // プレイヤーから離れる方向に移動
            val dx = entity.x - player.x
            val dz = entity.z - player.z
            val length = kotlin.math.sqrt(dx * dx + dz * dz)

            if (length > 0) {
                val targetX = entity.x + (dx / length) * 3.0
                val targetZ = entity.z + (dz / length) * 3.0

                // 走って逃げる（速度1.5倍）
                entity.navigation.startMovingTo(targetX, entity.y, targetZ, 1.5)
            }
        } else {
            // 十分離れたので終了
            stop()
        }

        // 200ティック（10秒）経過したら強制終了
        if (actionTick >= 200) {
            stop()
        }
    }

    /**
     * Get the weapon that was temporarily removed during playful interaction. This is used to drop
     * the weapon when the entity dies during playful state.
     */
    fun getStoredWeapon(): ItemStack? {
        return previousWeapon
    }
}
