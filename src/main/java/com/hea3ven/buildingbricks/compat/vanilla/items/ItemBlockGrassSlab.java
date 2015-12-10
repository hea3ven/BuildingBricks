package com.hea3ven.buildingbricks.compat.vanilla.items;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import com.hea3ven.buildingbricks.core.items.ItemColoredWrapper;
import com.hea3ven.buildingbricks.core.materials.Material;
import com.hea3ven.buildingbricks.core.materials.MaterialRegistry;

public class ItemBlockGrassSlab extends ItemColoredWrapper {

	public ItemBlockGrassSlab(Block block) {
		super(block);
	}

	@Override
	public Material getMaterial(ItemStack stack) {
		return MaterialRegistry.get("buildingbrickscompatvanilla:grass");
	}
}
