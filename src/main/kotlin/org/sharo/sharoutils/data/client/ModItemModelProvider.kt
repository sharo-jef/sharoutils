package org.sharo.sharoutils.data.client

import net.minecraft.data.DataGenerator
import net.minecraftforge.client.model.generators.ItemModelProvider
import net.minecraftforge.common.data.ExistingFileHelper
import org.sharo.sharoutils.SharoUtilities

class ModItemModelProvider(
    generator: DataGenerator,
    existingFileHelper: ExistingFileHelper
) : ItemModelProvider(generator, SharoUtilities.MODID, existingFileHelper) {
    override fun registerModels() {
        val itemGenerated = getExistingFile(mcLoc("item/generated"))
        getBuilder("sharo_ring").parent(itemGenerated).texture("layer0", "item/sharo_ring")
    }
}
