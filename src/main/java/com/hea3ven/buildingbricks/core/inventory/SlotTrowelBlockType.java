package com.hea3ven.buildingbricks.core.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.hea3ven.buildingbricks.core.ModBuildingBricks;
import com.hea3ven.buildingbricks.core.materials.BlockDescription;
import com.hea3ven.buildingbricks.core.materials.Material;
import com.hea3ven.buildingbricks.core.materials.MaterialStack;

public class SlotTrowelBlockType extends Slot {
	private EntityPlayer player;
	private ItemStack trowel;

	public SlotTrowelBlockType(EntityPlayer player, int index, int xPosition, int yPosition) {
		super(null, index, xPosition, yPosition);

		this.player = player;
		this.trowel = player.getCurrentEquippedItem();
	}

	@Override
	public ItemStack getStack() {
		Material mat = MaterialStack.get(trowel);
		if (mat == null)
			return null;
		BlockDescription desc = mat.getBlockRotation().get(getSlotIndex());
		return (desc != null) ? desc.getStack() : null;
	}

	@Override
	public boolean canTakeStack(EntityPlayer player) {
		return true;
	}

	@Override
	public void putStack(ItemStack stack) {
	}

	@Override
	public void onPickupFromSlot(EntityPlayer player, ItemStack stack) {
	}

	@Override
	public int getSlotStackLimit() {
		return 1;
	}

	@Override
	public ItemStack decrStackSize(int amount) {
		Material mat = MaterialStack.get(trowel);
		if (mat == null)
			return null;
		BlockDescription desc = mat.getBlockRotation().get(getSlotIndex());
		if (desc == null)
			return null;
		ModBuildingBricks.trowel.setCurrentBlockType(trowel, desc.getType());
		player.setCurrentItemOrArmor(0, trowel);
		return null;
	}

	@Override
	public void onSlotChanged() {
	}
}
