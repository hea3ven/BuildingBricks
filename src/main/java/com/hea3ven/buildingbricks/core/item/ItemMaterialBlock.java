package com.hea3ven.buildingbricks.core.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import net.minecraftforge.common.util.Constants.NBT;

import com.hea3ven.buildingbricks.core.block.base.BlockMaterial;
import com.hea3ven.buildingbricks.core.materials.Material;
import com.hea3ven.buildingbricks.core.materials.MaterialRegistry;
import com.hea3ven.buildingbricks.core.materials.MaterialStack;
import com.hea3ven.buildingbricks.core.materials.MaterialStack.ItemMaterial;

public class ItemMaterialBlock extends ItemBlock implements ItemMaterial {

	public ItemMaterialBlock(Block block) {
		super(block);
		setHasSubtypes(true);
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
		if (!stack.hasTagCompound())
			stack.setTagCompound(new NBTTagCompound());
		stack.getTagCompound().setString("material", mat.getMaterialId());
	}

	@Override
	public Material getMaterial(ItemStack stack) {
		if (stack.getTagCompound() != null && stack.getTagCompound().hasKey("material", NBT.TAG_STRING))
			return MaterialRegistry.get(stack.getTagCompound().getString("material"));
		else
			return null;
	}

	@Override
	public int getMetadata(ItemStack stack) {
		Material mat = getMaterial(stack);
		return mat == null ? 0 : MaterialRegistry.getMeta(mat);
	}

	@Override
	public int getDamage(ItemStack stack) {
		return getMetadata(stack);
	}
}
