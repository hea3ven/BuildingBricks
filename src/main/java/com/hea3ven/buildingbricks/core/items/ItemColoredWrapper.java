package com.hea3ven.buildingbricks.core.items;

import net.minecraft.block.Block;
import net.minecraft.item.ItemColored;
import net.minecraft.item.ItemStack;

import com.hea3ven.buildingbricks.core.blocks.base.BlockMaterial;
import com.hea3ven.buildingbricks.core.tileentity.TileMaterial;

public class ItemColoredWrapper extends ItemColored {

	public ItemColoredWrapper(Block block) {
		super(block, false);
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		return ((BlockMaterial) field_150939_a).getLocalizedName(TileMaterial.getStackMaterial(stack));
	}
}
