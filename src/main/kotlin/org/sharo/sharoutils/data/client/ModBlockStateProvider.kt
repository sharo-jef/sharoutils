package org.sharo.sharoutils.data.client

import net.minecraftforge.client.model.generators.BlockStateProvider
import net.minecraft.data.DataGenerator
import net.minecraftforge.common.data.ExistingFileHelper
import org.sharo.sharoutils.SharoUtilities
import org.sharo.sharoutils.blocks.Blocks

class ModBlockStateProvider : BlockStateProvider {
    constructor(generator: DataGenerator, existingFileHelper: ExistingFileHelper) : super(generator, SharoUtilities.MODID, existingFileHelper) {

    }

    override fun registerStatesAndModels() {
        simpleBlock(Blocks.SHARO_EARTH)
    }
}
