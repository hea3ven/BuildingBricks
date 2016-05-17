package com.hea3ven.buildingbricks.core.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import com.hea3ven.buildingbricks.core.ModBuildingBricks;
import com.hea3ven.buildingbricks.core.item.ItemMaterialBag;
import com.hea3ven.buildingbricks.core.materials.*;
import com.hea3ven.tools.commonutils.util.PlayerUtil;
import com.hea3ven.tools.commonutils.util.PlayerUtil.HeldEquipment;

public class ItemHandlerMaterialBag implements IItemHandler {
	private final HeldEquipment equipment;
	private final ItemStack origStack;
	private Material mat;

	public ItemHandlerMaterialBag(EntityPlayer player) {
		equipment = PlayerUtil.getHeldEquipment(player, ModBuildingBricks.materialBag);
		if (equipment == null)
			throw new RuntimeException("Player is not holding a material bag");
		origStack = initStack(equipment.getStack());
	}

	public ItemHandlerMaterialBag(ItemStack stack) {
		equipment = null;
		origStack = initStack(stack);
	}

	private ItemStack initStack(ItemStack stack) {
		ModBuildingBricks.materialBag.initUuid(stack);
		mat = MaterialStack.get(stack);
		return stack;
	}

	private ItemStack getBagStack() {
		if (equipment != null) {
			ItemStack playerStack = equipment.getStack();

			if (ModBuildingBricks.materialBag.areSameStack(equipment.getStack(), playerStack)) {
				return playerStack;
			}
		}
		return origStack;
	}

	public float getCurrentVolume() {
		return ModBuildingBricks.materialBag.getVolume(getBagStack());
	}

	@Override
	public int getSlots() {
		return 1;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		if (slot == 0) {
			if (mat == null)
				return null;
			else
				return mat.getFirstBlock().getStack();
		}
		return null;
	}

	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
		if (slot == 0) {
			Material stackMat = MaterialRegistry.getMaterialForStack(stack);
			if (stackMat == null) // Input stack doesn't have a material
				return stack;

			BlockDescription desc = stackMat.getBlock(stack);

			if (mat == null) {
				if (!simulate) {
					mat = stackMat;
					ItemStack bagStack = getBagStack();
					ModBuildingBricks.materialBag.setMaterial(bagStack, mat);
					ModBuildingBricks.materialBag.setVolume(bagStack,
							stack.stackSize * desc.getType().getVolume());
				}
				return null;
			} else {
				if (mat != stackMat) // Materials don't match
					return stack;

				ItemStack bagStack = getBagStack();
				int volume = ModBuildingBricks.materialBag.getVolume(bagStack);
				int capacity = (ItemMaterialBag.BAG_VOLUME - volume) / desc.getType().getVolume();
				if (stack.stackSize < capacity) {
					if (!simulate) {
						volume += stack.stackSize * desc.getType().getVolume();
						ModBuildingBricks.materialBag.setVolume(bagStack, volume);
					}
					return null;
				} else {
					stack.stackSize -= capacity;
					if (!simulate) {
						volume += capacity * desc.getType().getVolume();
						ModBuildingBricks.materialBag.setVolume(bagStack, volume);
					}
					return stack;
				}
			}
		}
		return stack;
	}

	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		if (slot == 0) {
			if (mat == null)
				return null;

			ItemStack bagStack = getBagStack();
			int volume = ModBuildingBricks.materialBag.getVolume(bagStack);
			MaterialBlockType type = MaterialBlockType.getBestForVolume(mat, volume);
			if (type == null)
				return null;
			ItemStack stack = ItemHandlerHelper.copyStackWithSize(mat.getBlock(type).getStack(),
					volume / type.getVolume());
			if (stack.stackSize > stack.getMaxStackSize())
				stack.stackSize = stack.getMaxStackSize();
			if (stack.stackSize > amount)
				stack.stackSize = amount;
			if (!simulate) {
				volume -= type.getVolume() * stack.stackSize;
				if (volume <= 0) {
					mat = null;
					ModBuildingBricks.materialBag.setMaterial(bagStack, null);
					ModBuildingBricks.materialBag.setVolume(bagStack, 0);
				} else {
					ModBuildingBricks.materialBag.setVolume(bagStack, volume);
				}
			}
			return stack;
		}
		return null;
	}
}
