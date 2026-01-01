package org.sharo.sharoutils.client

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry
import org.sharo.sharoutils.client.renderer.SharoEntityRenderer
import org.sharo.sharoutils.entity.EntityTypes

class ClientInitializer : ClientModInitializer {
    override fun onInitializeClient() {
        // Register entity renderer for Sharo entity
        EntityRendererRegistry.register(EntityTypes.SHARO) { context ->
            SharoEntityRenderer(context)
        }
    }
}
