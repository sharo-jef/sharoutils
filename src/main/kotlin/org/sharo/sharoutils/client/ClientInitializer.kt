package org.sharo.sharoutils.client

import net.fabricmc.api.ClientModInitializer

class ClientInitializer : ClientModInitializer {
    override fun onInitializeClient() {
        // TODO: Register entity renderer for Sharo entity
        // The entity renderer requires proper setup with Fabric's rendering API
        // For now, avoid spawning Sharo entities to prevent crashes
        // EntityRendererRegistry.register(EntityTypes.SHARO) { context ->
        //     SkeletonEntityRenderer(context)
        // }
    }
}
