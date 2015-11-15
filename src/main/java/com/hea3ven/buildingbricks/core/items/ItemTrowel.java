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
import com.hea3ven.buildingbricks.core.materials.MaterialStack.ItemMaterial;
import com.hea3ven.buildingbricks.core.materials.mapping.MaterialIdMapping;

public class ItemTrowel extends Item implements ItemMaterial {

	public ItemTrowel() {
		setCreativeTab(CreativeTabBuildingBricks.get());
		setUnlocalizedName("trowel");
	}

	@Override
	public void setMaterial(ItemStack stack, Material mat) {
		setBindedMaterial(stack, mat);
	}

	@Override
	public Material getMaterial(ItemStack stack) {
		return getBindedMaterial(stack);
	}

	public void setBindedMaterial(ItemStack stack, Material mat) {
		if (stack.getTagCompound() != null && stack.getTagCompound().hasKey("material", NBT.TAG_STRING))
			updateStack(stack); // convert from 1.0.x format

		if (mat == null)
			stack.setItemDamage(0);
		else
			stack.setItemDamage(MaterialIdMapping.get().getIdForMaterial(mat));
	}

	public Material getBindedMaterial(ItemStack stack) {
		if (stack.getTagCompound() != null && stack.getTagCompound().hasKey("material", NBT.TAG_STRING))
			updateStack(stack); // convert from 1.0.x format

		short matId = (short) getMetadata(stack);
		if (matId == 0)
			return null;
		else
			return MaterialIdMapping.get().getMaterialById(matId);
	}

	public MaterialBlockType getCurrentBlockType(ItemStack stack) {
		if (stack.getTagCompound() == null) {
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setInteger("blockType", getBindedMaterial(stack).getBlockRotation().getFirst().ordinal());
			stack.setTagCompound(nbt);
		}
		MaterialBlockType blockType =
				MaterialBlockType.getBlockType(stack.getTagCompound().getInteger("blockType"));
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
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side,
			float hitX, float hitY, float hitZ) {
		Material mat = getBindedMaterial(stack);
		if (mat == null)
			return super.onItemUse(stack, player, world, pos, side, hitX, hitY, hitZ);
		else {
			MaterialBlockType blockType = getCurrentBlockType(stack);
			ItemStack useStack = mat.getBlock(blockType).getStack().copy();
			MaterialItemStackConsumer consumer =
					new MaterialItemStackConsumer(blockType, mat, player.inventory);
			if (!player.capabilities.isCreativeMode && consumer.failed())
				return false;
			if (!useStack.onItemUse(player, world, pos, side, hitX, hitY, hitZ))
				return false;
			if (!player.capabilities.isCreativeMode)
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
			return StatCollector.translateToLocalFormatted("item.trowelBinded.name", mat.getLocalizedName());
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(ItemStack stack, int tintIndex) {
		if (tintIndex == 1)
			return super.getColorFromItemStack(stack, tintIndex);
		Material mat = getBindedMaterial(stack);
		if (mat == null)
			return super.getColorFromItemStack(stack, tintIndex);

		return mat.getBlock(MaterialBlockType.FULL).getItem().getColorFromItemStack(stack, tintIndex);
	}

	private void updateStack(ItemStack stack) {
		String matId = stack.getTagCompound().getString("material");
		stack.getTagCompound().removeTag("material");
		setBindedMaterial(stack, MaterialRegistry.get(matId));
	}
}
