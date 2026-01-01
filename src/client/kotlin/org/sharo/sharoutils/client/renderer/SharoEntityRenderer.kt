package org.sharo.sharoutils.client.renderer

import net.minecraft.client.render.entity.BipedEntityRenderer
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.render.entity.model.BipedEntityModel
import net.minecraft.client.render.entity.model.EntityModelLayers
import net.minecraft.client.render.entity.state.BipedEntityRenderState
import net.minecraft.util.Identifier
import org.sharo.sharoutils.Core
import org.sharo.sharoutils.entity.SharoEntity

class SharoEntityRenderer(context: EntityRendererFactory.Context) :
        BipedEntityRenderer<
                SharoEntity, BipedEntityRenderState, BipedEntityModel<BipedEntityRenderState>>(
                context,
                BipedEntityModel(context.getPart(EntityModelLayers.PLAYER)),
                0.5f
        ) {

    companion object {
        private val TEXTURE = Identifier.of(Core.MODID, "textures/entity/sharo.png")
    }

    override fun createRenderState(): BipedEntityRenderState {
        return BipedEntityRenderState()
    }

    override fun updateRenderState(
            entity: SharoEntity,
            state: BipedEntityRenderState,
            tickDelta: Float
    ) {
        super.updateRenderState(entity, state, tickDelta)
        state.handSwingProgress = entity.getHandSwingProgress(tickDelta)
        state.isUsingItem = entity.isUsingItem
        state.itemUseTime = entity.itemUseTime.toFloat()
        state.activeHand = entity.activeHand

        // Set bow attack pose when entity is actively using a bow
        val isHoldingBow =
                entity.mainHandStack.item is net.minecraft.item.BowItem ||
                        entity.offHandStack.item is net.minecraft.item.BowItem
        if (isHoldingBow && entity.isUsingItem) {
            state.rightArmPose = BipedEntityModel.ArmPose.BOW_AND_ARROW
            state.leftArmPose = BipedEntityModel.ArmPose.BOW_AND_ARROW
        }
    }

    override fun getTexture(state: BipedEntityRenderState): Identifier {
        return TEXTURE
    }
}
