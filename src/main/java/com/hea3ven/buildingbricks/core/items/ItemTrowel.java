package com.hea3ven.buildingbricks.core.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import com.hea3ven.buildingbricks.core.materials.Material;
import com.hea3ven.buildingbricks.core.materials.MaterialBlockType;
import com.hea3ven.buildingbricks.core.materials.MaterialRegistry;

public class ItemTrowel extends Item {

	public ItemTrowel() {
		setCreativeTab(CreativeTabs.tabTools);
		setUnlocalizedName("trowel");
	}

	public void setBindedMaterial(ItemStack stack, Material mat) {
		if (stack.getTagCompound() == null)
			stack.setTagCompound(new NBTTagCompound());
		if (mat != null) {
			stack.getTagCompound().setInteger("material", mat.globalId);
			stack.getTagCompound().setInteger("blockType",
					mat.getBlockRotation().getFirst().ordinal());
		} else {
			stack.getTagCompound().removeTag("material");
			stack.getTagCompound().removeTag("blockType");
		}
	}

	public Material getBindedMaterial(ItemStack stack) {
		if (stack.getTagCompound() == null || !stack.getTagCompound().hasKey("material"))
			return null;

		return MaterialRegistry.get(stack.getTagCompound().getInteger("material"));
	}

	public MaterialBlockType getCurrentBlockType(ItemStack stack) {
		return MaterialBlockType.getBlockType(stack.getTagCompound().getInteger("blockType"));
	}

	public void setCurrentBlockType(ItemStack stack, MaterialBlockType blockType) {
		stack.getTagCompound().setInteger("blockType", blockType.ordinal());
	}

	public void setNextBlockRotation(ItemStack stack) {
		Material mat = getBindedMaterial(stack);
		if (mat != null) {
			MaterialBlockType blockType = getCurrentBlockType(stack);
			blockType = mat.getBlockRotation().getNext(blockType);
			setCurrentBlockType(stack, blockType);
		}
	}

	public void setPrevBlockRotation(ItemStack stack) {
		Material mat = getBindedMaterial(stack);
		if (mat != null) {
			MaterialBlockType blockType = getCurrentBlockType(stack);
			blockType = mat.getBlockRotation().getPrev(blockType);
			setCurrentBlockType(stack, blockType);
		}
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos,
			EnumFacing side, float hitX, float hitY, float hitZ) {
		Material mat = getBindedMaterial(stack);
		if (mat == null)
			return super.onItemUse(stack, playerIn, worldIn, pos, side, hitX, hitY, hitZ);
		else {
			MaterialBlockType blockType = getCurrentBlockType(stack);
			Item item = mat.getBlockItem(blockType);
			return item.onItemUse(stack, playerIn, worldIn, pos, side, hitX, hitY, hitZ);
		}
	}

}
