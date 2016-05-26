package com.hea3ven.buildingbricks.core.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import net.minecraftforge.items.IItemHandler;

import com.hea3ven.tools.commonutils.inventory.ContainerBase;
import com.hea3ven.tools.commonutils.inventory.IAdvancedSlot;
import com.hea3ven.tools.commonutils.inventory.SlotItemHandlerBase;

public class SlotMaterialBag extends SlotItemHandlerBase implements IAdvancedSlot {
	public SlotMaterialBag(IItemHandler inv, int slot, int x, int y) {
		super(inv, slot, x, y);
	}

	@Override
	public ItemStack onQuickMove(ContainerBase container, EntityPlayer player, int clickedButton) {
		ItemStack resultStack = null;
		ItemStack extraStack = container.transferStackInSlot(player, slotNumber);

		if (extraStack != null) {
			Item item = extraStack.getItem();
			resultStack = extraStack.copy();

			if (getStack() != null && getStack().getItem() == item) {
				container.retrySlotClick(slotNumber, clickedButton, true, player);
			}
		}
		return resultStack;
	}

	@Override
	public ItemStack onPickUp(EntityPlayer player, int clickedButton) {
		ItemStack playerStack = player.inventory.getItemStack();
		if (playerStack != null) {
			ItemStack newPlayerStack = getItemHandler().insertItem(slotNumber, playerStack.copy(), false);
			if (newPlayerStack != null)
				playerStack.stackSize = newPlayerStack.stackSize;
			else
				player.inventory.setItemStack(null);
		} else {
			ItemStack slotStack = getItemHandler().getStackInSlot(slotNumber);
			if (slotStack != null) {
				int size = slotStack.getMaxStackSize();
				if (clickedButton != 0)
					size = size / 2;
				playerStack = getItemHandler().extractItem(slotNumber, size, false);
				player.inventory.setItemStack(playerStack);
			}
		}
		return null;
	}

	@Override
	public void onSwapPlayerStack(EntityPlayer player, int equipSlot) {

	}

	@Override
	public void onClone(EntityPlayer player) {

	}

	@Override
	public void onThrow(EntityPlayer player, int clickedButton) {

	}

	@Override
	public void onPickUpAll(ContainerBase container, EntityPlayer player, int clickedButton) {

	}

	@Override
	public boolean canDragIntoSlot() {
		return false;
	}

	@Override
	public boolean canTransferFromSlot() {
		return getItemHandler().getStackInSlot(slotNumber) != null;
	}

	@Override
	public boolean transferFrom(IAdvancedSlot slot) {
		ItemStack srcStack = slot.getImmutableStack();
		ItemStack stack = getItemHandler().insertItem(slotNumber, srcStack, false);
		slot.extract(srcStack.stackSize - ((stack != null) ? stack.stackSize : 0));
		return true;
	}

	@Override
	public ItemStack getImmutableStack() {
		return getStack();
	}

	@Override
	public ItemStack extract(int size) {
		return decrStackSize(size);
	}

	@Override
	public void putStack(ItemStack stack) {
	}
}
