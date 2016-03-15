package com.hea3ven.buildingbricks.core.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.hea3ven.buildingbricks.core.ModBuildingBricks;
import com.hea3ven.buildingbricks.core.materials.BlockDescription;
import com.hea3ven.buildingbricks.core.materials.Material;
import com.hea3ven.buildingbricks.core.materials.MaterialStack;
import com.hea3ven.tools.commonutils.util.PlayerUtil;
import com.hea3ven.tools.commonutils.util.PlayerUtil.HeldEquipment;

public class SlotTrowelBlockType extends Slot {
	private EntityPlayer player;
	private HeldEquipment equipment;

	public SlotTrowelBlockType(EntityPlayer player, int index, int xPosition, int yPosition) {
		super(null, index, xPosition, yPosition);

		this.player = player;
		this.equipment = PlayerUtil.getHeldEquipment(player, ModBuildingBricks.trowel);
	}

	@Override
	public ItemStack getStack() {
		Material mat = MaterialStack.get(equipment.stack);
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
		Material mat = MaterialStack.get(equipment.stack);
		if (mat == null)
			return null;
		BlockDescription desc = mat.getBlockRotation().get(getSlotIndex());
		if (desc == null)
			return null;
		ModBuildingBricks.trowel.setCurrentBlockType(equipment.stack, desc.getType());
		equipment.updatePlayer();
		return null;
	}

	@Override
	public void onSlotChanged() {
	}
}
