package com.hea3ven.buildingbricks.core.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.hea3ven.buildingbricks.core.inventory.MaterialItemStackConsumer;
import com.hea3ven.buildingbricks.core.items.creativetab.CreativeTabBuildingBricks;
import com.hea3ven.buildingbricks.core.materials.Material;
import com.hea3ven.buildingbricks.core.materials.MaterialBlockType;
import com.hea3ven.buildingbricks.core.materials.MaterialRegistry;

public class ItemTrowel extends Item {

	public ItemTrowel() {
		setCreativeTab(CreativeTabBuildingBricks.get());
		setUnlocalizedName("trowel");
	}

	public void setBindedMaterial(ItemStack stack, Material mat) {
		if (stack.getTagCompound() == null)
			stack.setTagCompound(new NBTTagCompound());
		if (mat != null) {
			stack.getTagCompound().setString("material", mat.materialId());
			stack.getTagCompound().setInteger("blockType",
					mat.getBlockRotation().getFirst().ordinal());
		} else {
			stack.getTagCompound().removeTag("material");
			stack.getTagCompound().removeTag("blockType");
		}
	}

	public Material getBindedMaterial(ItemStack stack) {
		if (stack.getTagCompound() == null
				|| !stack.getTagCompound().hasKey("material", NBT.TAG_STRING))
			return null;

		return MaterialRegistry.get(stack.getTagCompound().getString("material"));
	}

	public MaterialBlockType getCurrentBlockType(ItemStack stack) {
		MaterialBlockType blockType = MaterialBlockType
				.getBlockType(stack.getTagCompound().getInteger("blockType"));
		Material mat = getBindedMaterial(stack);
		if (mat != null && mat.getBlock(blockType) == null) {
			blockType = mat.getBlockRotation().getNext(blockType);
			setCurrentBlockType(stack, blockType);
		}
		return blockType;
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
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos,
			EnumFacing side, float hitX, float hitY, float hitZ) {
		Material mat = getBindedMaterial(stack);
		if (mat == null)
			return super.onItemUse(stack, player, world, pos, side, hitX, hitY, hitZ);
		else {
			MaterialBlockType blockType = getCurrentBlockType(stack);
			ItemStack useStack = mat.getBlock(blockType).getStack().copy();
			MaterialItemStackConsumer consumer = new MaterialItemStackConsumer(blockType, mat,
					player.inventory);
			if (consumer.failed())
				return false;
			if (!useStack.onItemUse(player, world, pos, side, hitX, hitY, hitZ))
				return false;
			consumer.apply(world, player.getPosition());
			return true;
		}
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		Material mat = getBindedMaterial(stack);
		if (mat == null)
			return super.getItemStackDisplayName(stack);
		else
			return StatCollector.translateToLocalFormatted("item.trowelBinded.name",
					mat.getLocalizedName());
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(ItemStack stack, int tintIndex) {
		if (tintIndex == 1)
			return super.getColorFromItemStack(stack, tintIndex);
		Material mat = getBindedMaterial(stack);
		if (mat == null)
			return super.getColorFromItemStack(stack, tintIndex);

		return mat.getBlock(MaterialBlockType.FULL).getItem().getColorFromItemStack(stack,
				tintIndex);
	}

}
