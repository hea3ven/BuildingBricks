package com.hea3ven.buildingbricks.core.items;

import net.minecraft.block.Block;
import net.minecraft.item.ItemColored;
import net.minecraft.item.ItemStack;

import com.hea3ven.buildingbricks.core.blocks.base.BlockMaterial;
import com.hea3ven.buildingbricks.core.materials.Material;
import com.hea3ven.buildingbricks.core.materials.MaterialRegistry;
import com.hea3ven.buildingbricks.core.materials.MaterialStack;
import com.hea3ven.buildingbricks.core.materials.MaterialStack.ItemMaterial;
import com.hea3ven.buildingbricks.core.materials.mapping.MaterialIdMapping;

public class ItemColoredWrapper extends ItemColored implements ItemMaterial {

	public ItemColoredWrapper(Block block) {
		super(block, false);
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		Material mat = MaterialStack.get(stack);
		if (mat == null)
			return "tile.invalidMaterialBlock.name";
		return ((BlockMaterial) block).getLocalizedName(mat);
	}

	@Override
	public void setMaterial(ItemStack stack, Material mat) {
		if (stack.hasTagCompound()) // update from 1.0.x
			stack.setTagCompound(null);

		stack.setItemDamage(MaterialIdMapping.get().getIdForMaterial(mat));
	}

	@Override
	public Material getMaterial(ItemStack stack) {
		if (stack.hasTagCompound()) { // update from 1.0.x
			String matId = stack.getTagCompound().getString("material");
			stack.setTagCompound(null);
			setMaterial(stack, MaterialRegistry.get(matId));
		}

		short matId = (short) stack.getItemDamage();
		if (matId == 0)
			return MaterialRegistry.get("cobblestone");
		return MaterialIdMapping.get().getMaterialById(matId);
	}
}
