package org.sharo.sharoutils.entity

import net.minecraft.entity.EntityClassification
import net.minecraft.entity.EntityType
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.RegistryObject
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import org.sharo.sharoutils.Core

class EntityTypes {
    companion object {
        @JvmStatic
        val register: DeferredRegister<EntityType<*>> = DeferredRegister.create(ForgeRegistries.ENTITIES, Core.MODID)
        @JvmStatic
        val SHARO: RegistryObject<EntityType<SharoEntity>> = register.register("sharo") {
            EntityType.Builder.create(::SharoEntity, EntityClassification.CREATURE)
                .size(1f, 1f)
                .build(ResourceLocation(Core.MODID, "sharo").toString())
        }
    }
}
