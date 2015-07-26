package com.hea3ven.buildingbricks.core.items;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import net.minecraftforge.common.util.Constants.NBT;

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
			if (!consumeMaterial(blockType, mat, player, false))
				return false;
			if (!world.isRemote) {
				if (!useStack.getItem().onItemUse(useStack, player, world, pos, side, hitX, hitY,
						hitZ))
					return false;
			}
			consumeMaterial(blockType, mat, player, true);
			return true;
		}
	}

	private boolean consumeMaterial(MaterialBlockType blockType, Material mat, EntityPlayer player,
			boolean doConsume) {
		// TODO: prototype code, rewrite later

		if (player.capabilities.isCreativeMode)
			return true;

		int volume = blockType.getVolume();
		Map<Integer, ItemStack> invSnapshot = new HashMap<Integer, ItemStack>();
		for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
			ItemStack stack = player.inventory.getStackInSlot(i);
			Material stackMat = MaterialRegistry.getMaterialForStack(stack);
			if (stackMat != null && stackMat == mat) {
				int stackVolume = mat.getBlock(stack).getType().getVolume();
				invSnapshot.put(i, stack.copy());
				if (volume <= stackVolume * stack.stackSize) {
					int consumed = volume / stackVolume;
					volume -= stackVolume * consumed;
					stack.stackSize -= consumed;
					if (stack.stackSize == 0) {
						player.inventory.setInventorySlotContents(i, null);
					} else {
						if (volume > 0) {
							volume -= stackVolume;
							stack.stackSize -= 1;
							if (stack.stackSize == 0) {
								player.inventory.setInventorySlotContents(i, null);
							}
							while (volume < 0) {
								MaterialBlockType partBlockType = MaterialBlockType
										.getBestForVolume(-volume);
								if (doConsume) {
									ItemStack newStack = mat.getBlock(partBlockType).getStack();
									player.inventory.addItemStackToInventory(newStack.copy());
								}
								volume += partBlockType.getVolume();
							}
						}
					}
				} else {
					volume -= stackVolume * stack.stackSize;
					player.inventory.setInventorySlotContents(i, null);
				}

				if (volume == 0)
					break;
			}
		}

		if (!doConsume || volume > 0) {
			for (Entry<Integer, ItemStack> slotSnapshot : invSnapshot.entrySet()) {
				player.inventory.setInventorySlotContents(slotSnapshot.getKey(),
						slotSnapshot.getValue());
			}
		}
		return volume == 0;
	}

}
