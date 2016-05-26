package com.hea3ven.buildingbricks.core.inventory;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import com.hea3ven.buildingbricks.core.materials.BlockDescription;
import com.hea3ven.buildingbricks.core.materials.Material;
import com.hea3ven.buildingbricks.core.materials.MaterialBlockType;
import com.hea3ven.tools.commonutils.util.ItemStackUtil;

public class MaterialItemStackConsumer {

	private MaterialBlockType blockType;
	private Material mat;
	private ItemHandlerConsumer itemHandlerConsumer;
	private List<ItemHandlerConsumer> subItemHandlerConsumers;
	private IItemHandler inventory;

	private int totalConsumed;

	public MaterialItemStackConsumer(MaterialBlockType blockType, Material mat, IItemHandler inventory) {
		this.blockType = blockType;
		this.mat = mat;
		this.inventory = inventory;

		itemHandlerConsumer = new ItemHandlerConsumer(inventory);
		subItemHandlerConsumers = new ArrayList<>();
		for (int slot = 0; slot < inventory.getSlots(); slot++) {
			ItemStack stack = inventory.getStackInSlot(slot);
			if (stack != null && stack.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null))
				subItemHandlerConsumers.add(new ItemHandlerConsumer(
						stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)));
		}

		totalConsumed = 0;

		parseInventory();
	}

	public boolean failed() {
		return totalConsumed < blockType.getVolume();
	}

	public void apply(World world, BlockPos pos) {
		int extraVol = totalConsumed - blockType.getVolume();
		for (ItemHandlerConsumer subConsumer : subItemHandlerConsumers)
			extraVol = subConsumer.apply(extraVol);
		extraVol = itemHandlerConsumer.apply(extraVol);

		while (extraVol > 0) {
			MaterialBlockType extraBlockType = MaterialBlockType.getBestForVolume(extraVol);
			if (extraBlockType == null) {
				extraVol = 0;
				continue;
			}
			ItemStack newStack = mat.getBlock(extraBlockType).getStack();
			addItemStackToInventory(world, pos, newStack.copy());
			extraVol -= extraBlockType.getVolume();
		}
	}

	private void addItemStackToInventory(World world, BlockPos pos, ItemStack newStack) {
		// newStack = ItemHandlerHelper.insertItem(inventory, newStack, false);

		// Fix for player inventory
		int inventorySize = inventory.getSlots();
		if (inventory instanceof InvWrapper && ((InvWrapper) inventory).getInv() instanceof InventoryPlayer) {
			inventorySize -= 4;
		}
		for (int i = 0; i < inventorySize; i++) {
			newStack = inventory.insertItem(i, newStack, false);
			if (newStack == null || newStack.stackSize <= 0) {
				break;
			}
		}

		if (newStack != null) {
			ItemStackUtil.dropFromBlock(world, pos, newStack);
		}
	}

	private void parseInventory() {
		for (ItemHandlerConsumer subConsumer : subItemHandlerConsumers) {
			subConsumer.searchExactMatch();
		}
		for (ItemHandlerConsumer subConsumer : subItemHandlerConsumers) {
			subConsumer.searchMaterialBlocks();
		}
		itemHandlerConsumer.searchExactMatch();
		itemHandlerConsumer.searchMaterialBlocks();
	}

	private class ItemHandlerConsumer {
		private IItemHandler inventory;
		private int[] consumedStacks;

		public ItemHandlerConsumer(IItemHandler inventory) {
			this.inventory = inventory;
			consumedStacks = new int[inventory.getSlots()];
		}

		public void searchExactMatch() {
			if (totalConsumed != 0)
				return;
			for (int slot = 0; slot < inventory.getSlots(); slot++) {
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

		public void searchMaterialBlocks() {
			boolean found = true;
			while (found && totalConsumed < blockType.getVolume()) {
				found = false;

				int minVol = Integer.MAX_VALUE;
				int minVolSlot = -1;

				for (int slot = 0; slot < inventory.getSlots(); slot++) {
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

		public int apply(int extraVol) {
			boolean consumed = false;
			for (int slot = 0; slot < consumedStacks.length; slot++) {
				if (consumedStacks[slot] > 0) {
					consumed = true;
					inventory.extractItem(slot, consumedStacks[slot], false);
				}
			}

			if (consumed) {
				while (consumed && extraVol > 0) {
					consumed = false;
					MaterialBlockType extraBlockType = MaterialBlockType.getBestForVolume(extraVol);
					if (extraBlockType == null) {
						extraVol = 0;
						continue;
					}
					ItemStack newStack = mat.getBlock(extraBlockType).getStack().copy();
					for (int i = 0; i < inventory.getSlots(); i++) {
						ItemStack resultStack = inventory.insertItem(i, newStack, false);
						if (resultStack == null) {
							consumed = true;
							extraVol -= extraBlockType.getVolume();
							break;
						}
						if (newStack.stackSize != resultStack.stackSize) {
							consumed = true;
							extraVol -=
									extraBlockType.getVolume() * newStack.stackSize - resultStack.stackSize;
							break;
						}
					}
				}
			}
			return extraVol;
		}
	}
}
