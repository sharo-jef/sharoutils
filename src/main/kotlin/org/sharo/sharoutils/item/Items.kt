package org.sharo.sharoutils.item

import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.item.Rarity
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.RegistryObject
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import org.sharo.sharoutils.Core
import org.sharo.sharoutils.block.Blocks
import org.sharo.sharoutils.entity.EntityTypes
import org.sharo.sharoutils.tab.SharoUtilitiesItemGroup

@Mod.EventBusSubscriber(modid = Core.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
class Items {
    companion object {
        @JvmStatic
        val register: DeferredRegister<Item> = DeferredRegister.create(ForgeRegistries.ITEMS, Core.MODID)

        @JvmStatic
        val SHARO_INGOT: RegistryObject<SharoIngot> = register.register("sharo_ingot", ::SharoIngot)
        @JvmStatic
        val SHARO_RING: RegistryObject<SharoRing> = register.register("sharo_ring", ::SharoRing)
        @JvmStatic
        val SHARO_MANJU: RegistryObject<SharoManju> = register.register("sharo_manju", ::SharoManju)
        @JvmStatic
        val SHARO_NUGGET: RegistryObject<SharoNugget> = register.register("sharo_nugget", ::SharoNugget)
//        @JvmStatic
//        val SHARO_SPAWN_EGG: RegistryObject<SharoSpawnEgg> = register.register("sharo_spawn_egg") {
//            SharoSpawnEgg(
//                EntityTypes.SHARO,
//                0xa2d085,
//                0x646461,
//                Item.Properties().group(Core.ITEM_GROUP)
//            )
//        }

        @JvmStatic
        val SHARO_EARTH: RegistryObject<BlockItem> = register.register("sharo_earth") {
            BlockItem(
                Blocks.SHARO_EARTH.get(),
                Item.Properties()
                    .group(Core.ITEM_GROUP)
                    .maxStackSize(64)
                    .rarity(Rarity.RARE)
            )
        }
        @JvmStatic
        val ELEVATOR: RegistryObject<BlockItem> = register.register("elevator") {
            BlockItem(
                Blocks.ELEVATOR.get(),
                Item.Properties()
                    .group(Core.ITEM_GROUP)
                    .maxStackSize(64)
                    .rarity(Rarity.COMMON)
            )
        }
    }
}
