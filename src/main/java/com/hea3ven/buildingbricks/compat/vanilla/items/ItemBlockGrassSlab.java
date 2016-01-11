package com.hea3ven.buildingbricks.compat.vanilla.items;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import com.hea3ven.buildingbricks.core.items.ItemMaterialBlock;
import com.hea3ven.buildingbricks.core.materials.Material;
import com.hea3ven.buildingbricks.core.materials.MaterialRegistry;

public class ItemBlockGrassSlab extends ItemMaterialBlock {

	public ItemBlockGrassSlab(Block block) {
		super(block);
	}

	@Override
	public Material getMaterial(ItemStack stack) {
		return MaterialRegistry.get("buildingbrickscompatvanilla:grass");
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		return StatCollector.translateToLocalFormatted("blockType.slab",
				getMaterial(stack).getLocalizedName());
	}
}
