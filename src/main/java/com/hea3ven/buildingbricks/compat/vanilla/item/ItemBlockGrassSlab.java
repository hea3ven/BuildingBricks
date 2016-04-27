package com.hea3ven.buildingbricks.compat.vanilla.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;

import com.hea3ven.buildingbricks.core.item.ItemMaterialBlock;
import com.hea3ven.buildingbricks.core.materials.Material;
import com.hea3ven.buildingbricks.core.materials.MaterialRegistry;

public class ItemBlockGrassSlab extends ItemMaterialBlock {

	public ItemBlockGrassSlab(Block block) {
		super(block);
	}

	@Override
	public Material getMaterial(ItemStack stack) {
		return MaterialRegistry.get("minecraft:grass");
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		return I18n.translateToLocalFormatted("blockType.slab",
				getMaterial(stack).getLocalizedName());
	}
}
