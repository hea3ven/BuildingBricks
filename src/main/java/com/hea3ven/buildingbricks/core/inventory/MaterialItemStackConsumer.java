package com.hea3ven.buildingbricks.core.inventory;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import com.hea3ven.buildingbricks.core.items.ItemMaterialBag;
import com.hea3ven.buildingbricks.core.materials.BlockDescription;
import com.hea3ven.buildingbricks.core.materials.Material;
import com.hea3ven.buildingbricks.core.materials.MaterialBlockType;
import com.hea3ven.tools.commonutils.util.ItemStackUtil;

public class MaterialItemStackConsumer {

	private MaterialBlockType blockType;
	private Material mat;
	private IInventory inventory;

	private int[] consumedStacks;
	private int[] consumedMaterialBag;
	private int totalConsumed;

	public MaterialItemStackConsumer(MaterialBlockType blockType, Material mat, IInventory inventory) {
		this.blockType = blockType;
		this.mat = mat;
		this.inventory = inventory;

		consumedStacks = new int[inventory.getSizeInventory()];
		consumedMaterialBag = new int[inventory.getSizeInventory()];
		totalConsumed = 0;

		parseInventory();
	}

	public boolean failed() {
		return totalConsumed < blockType.getVolume();
	}

	public void apply(World world, BlockPos pos) {
		for (int slot = 0; slot < consumedMaterialBag.length; slot++) {
			if (consumedMaterialBag[slot] > 0) {
				ItemStack stack = inventory.getStackInSlot(slot);
				ItemMaterialBag bag = (ItemMaterialBag) stack.getItem();
				bag.setVolume(stack, bag.getVolume(stack) - consumedMaterialBag[slot]);
			}
		}
		for (int slot = 0; slot < consumedStacks.length; slot++) {
			if (consumedStacks[slot] > 0) {
				inventory.decrStackSize(slot, consumedStacks[slot]);
			}
		}
		int extraVol = totalConsumed - blockType.getVolume();
		while (extraVol > 0) {
			MaterialBlockType extraBlockType = MaterialBlockType.getBestForVolume(extraVol);
			ItemStack newStack = mat.getBlock(extraBlockType).getStack();
			addItemStackToInventory(world, pos, newStack.copy());
			extraVol -= extraBlockType.getVolume();
		}
	}

	private void addItemStackToInventory(World world, BlockPos pos, ItemStack newStack) {
		int inventorySize = inventory.getSizeInventory();
		if (inventory instanceof InventoryPlayer) {
			inventorySize -= 4;
		}
		for (int slot = 0; slot < inventorySize; slot++) {
			ItemStack stack = inventory.getStackInSlot(slot);
			if (ItemStack.areItemsEqual(newStack, stack) &&
					ItemStack.areItemStackTagsEqual(newStack, stack)) {
				if (stack.stackSize + 1 <= stack.getMaxStackSize()) {
					stack.stackSize += 1;
					newStack = null;
					break;
				}
			}
		}
		if (newStack != null) {
			for (int slot = 0; slot < inventorySize; slot++) {
				if (inventory.getStackInSlot(slot) == null) {
					inventory.setInventorySlotContents(slot, newStack);
					newStack = null;
					break;
				}
			}
		}
		if (newStack != null) {
			ItemStackUtil.dropFromBlock(world, pos, newStack);
		}
	}

	private void parseInventory() {
		searchMaterialBag();

		searchExactMatch();

		searchMaterialBlocks();
	}

	private void searchMaterialBag() {
		for (int slot = 0; slot < inventory.getSizeInventory(); slot++) {
			ItemStack stack = inventory.getStackInSlot(slot);
			if(stack == null)
				continue;
			if (!(stack.getItem() instanceof ItemMaterialBag))
				continue;

			ItemMaterialBag bag = (ItemMaterialBag) stack.getItem();
			Material bagMat = bag.getMaterial(stack);
			if (mat != bagMat)
				continue;

			int bagVolume = bag.getVolume(stack);
			if (bagVolume > (blockType.getVolume() - totalConsumed)) {
				consumedMaterialBag[slot] = (blockType.getVolume() - totalConsumed);
				totalConsumed += (blockType.getVolume() - totalConsumed);
			} else {
				consumedMaterialBag[slot] = bagVolume;
				totalConsumed += bagVolume;
			}
		}
	}

	private void searchExactMatch() {
		if (totalConsumed != 0)
			return;
		for (int slot = 0; slot < inventory.getSizeInventory(); slot++) {
			ItemStack stack = inventory.getStackInSlot(slot);
			BlockDescription blockDesc = mat.getBlock(stack);
			if (blockDesc == null)
				continue;

			if (blockType == blockDesc.getType()) {
				consumedStacks[slot] = 1;
				totalConsumed = blockType.getVolume();
				return;
			}
		}
	}

	private void searchMaterialBlocks() {
		boolean found = true;
		while (found && totalConsumed < blockType.getVolume()) {
			found = false;

			int minVol = Integer.MAX_VALUE;
			int minVolSlot = -1;

			for (int slot = 0; slot < inventory.getSizeInventory(); slot++) {
				ItemStack stack = inventory.getStackInSlot(slot);
				BlockDescription blockDesc = mat.getBlock(stack);
				if (blockDesc == null)
					continue;

				if (stack.stackSize <= consumedStacks[slot])
					continue;

				if (blockDesc.getType().getVolume() < minVol) {
					minVol = blockDesc.getType().getVolume();
					minVolSlot = slot;
				}
			}

			if (minVolSlot != -1) {
				found = true;
				totalConsumed += minVol;
				consumedStacks[minVolSlot]++;
			}
		}
	}
}
