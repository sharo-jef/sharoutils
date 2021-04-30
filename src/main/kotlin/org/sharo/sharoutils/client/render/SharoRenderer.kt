package org.sharo.sharoutils.client.render

import net.minecraft.client.renderer.entity.EntityRendererManager
import net.minecraft.client.renderer.entity.MobRenderer
import net.minecraft.client.renderer.entity.model.PlayerModel
import net.minecraft.util.ResourceLocation
import org.sharo.sharoutils.Core
import org.sharo.sharoutils.entity.SharoEntity

class SharoRenderer(
    rendererManager: EntityRendererManager
) : MobRenderer<SharoEntity, PlayerModel<SharoEntity>>(
    rendererManager,
    PlayerModel<SharoEntity>(.1f, true),
    .5f
) {
    private val texture: ResourceLocation = ResourceLocation(
        Core.MODID,
        "textures/entity/sharo.png"
    )

    override fun getEntityTexture(entity: SharoEntity): ResourceLocation = texture
}
