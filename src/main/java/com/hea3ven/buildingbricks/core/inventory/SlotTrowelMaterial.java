package com.hea3ven.buildingbricks.core.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.hea3ven.buildingbricks.core.materials.Material;
import com.hea3ven.buildingbricks.core.materials.MaterialBlockType;
import com.hea3ven.buildingbricks.core.materials.MaterialRegistry;
import com.hea3ven.buildingbricks.core.materials.MaterialStack;
import com.hea3ven.tools.commonutils.inventory.SlotCustom;

public class SlotTrowelMaterial extends Slot implements SlotCustom {
	private EntityPlayer player;
	private ItemStack trowel;

	public SlotTrowelMaterial(EntityPlayer player, int index, int xPosition, int yPosition) {
		super(null, index, xPosition, yPosition);

		this.player = player;
		this.trowel = player.getCurrentEquippedItem();
	}

	@Override
	public ItemStack getStack() {
		Material mat = MaterialStack.get(trowel);
		return mat != null ? mat.getFirstBlock().getStack().copy() : null;
	}

	@Override
	public void putStack(ItemStack stack) {
		Material mat = MaterialRegistry.getMaterialForStack(stack);
		if (mat != null)
			MaterialStack.set(trowel, mat);
		player.setCurrentItemOrArmor(0, trowel);
	}

	@Override
	public void onPickupFromSlot(EntityPlayer player, ItemStack stack) {
		MaterialStack.set(trowel, null);
		this.player.setCurrentItemOrArmor(0, trowel);
	}

	@Override
	public int getSlotStackLimit() {
		return 0;
	}

	@Override
	public int getItemStackLimit(ItemStack stack) {
		return 0;
	}

	@Override
	public ItemStack decrStackSize(int amount) {
		MaterialStack.set(trowel, null);
		player.setCurrentItemOrArmor(0, trowel);
		return null;
	}

	@Override
	public void onSlotChanged() {
	}

	@Override
	public ItemStack provideItemStack() {
		return null;
	}

	@Override
	public boolean receiveItemStack(ItemStack stack) {
		putStack(stack);
		return true;
	}
}
