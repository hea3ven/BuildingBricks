package com.hea3ven.buildingbricks.core.materials;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.block.Block;

import com.hea3ven.buildingbricks.core.materials.mapping.IdMappingLoader;

public class BlockDescriptionMaterial extends BlockDescription {
	public BlockDescriptionMaterial(MaterialBlockType blockType, Block block, Material mat) {
		super(blockType, block);
		IdMappingLoader.dynamicStacks.add(Pair.of(getStack(), mat));
	}
}
