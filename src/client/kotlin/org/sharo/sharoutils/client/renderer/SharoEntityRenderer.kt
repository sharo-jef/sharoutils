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

    override fun getTexture(state: BipedEntityRenderState): Identifier {
        return TEXTURE
    }
}
