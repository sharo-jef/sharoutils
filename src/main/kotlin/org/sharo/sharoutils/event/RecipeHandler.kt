package org.sharo.sharoutils.event

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.PrimitiveCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceCondition
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditionType
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditions
import net.minecraft.util.Identifier
import org.sharo.sharoutils.Core
import org.sharo.sharoutils.config.ModConfig

object RecipeHandler {
    private val CONFIG_ENABLED_CODEC: MapCodec<ConfigEnabledCondition> =
            RecordCodecBuilder.mapCodec { instance ->
                instance.group(
                                PrimitiveCodec.STRING
                                        .fieldOf("config_key")
                                        .forGetter(ConfigEnabledCondition::configKey)
                        )
                        .apply(instance, ::ConfigEnabledCondition)
            }

    private val CONFIG_ENABLED_TYPE: ResourceConditionType<ConfigEnabledCondition> =
            ResourceConditionType.create(
                    Identifier.of(Core.MODID, "config_enabled"),
                    CONFIG_ENABLED_CODEC
            )

    fun register() {
        // Register the custom resource condition type
        ResourceConditions.register(CONFIG_ENABLED_TYPE)
        Core.logger.info("Registered custom recipe condition: sharoutils:config_enabled")
    }

    data class ConfigEnabledCondition(val configKey: String) : ResourceCondition {
        override fun getType(): ResourceConditionType<*> = CONFIG_ENABLED_TYPE

        override fun test(
                registryInfoGetter: net.minecraft.registry.RegistryOps.RegistryInfoGetter?
        ): Boolean {
            val enabled =
                    when (configKey) {
                        "sharoRingCraftingEnabled" -> ModConfig.INSTANCE.sharoRingCraftingEnabled
                        else -> {
                            Core.logger.warn("Unknown config key in recipe condition: $configKey")
                            true
                        }
                    }
            Core.logger.info("Recipe condition check: $configKey = $enabled")
            return enabled
        }
    }
}
