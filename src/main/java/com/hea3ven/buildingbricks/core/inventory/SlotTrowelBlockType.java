package com.hea3ven.buildingbricks.core.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.hea3ven.buildingbricks.core.ModBuildingBricks;
import com.hea3ven.buildingbricks.core.materials.BlockDescription;
import com.hea3ven.buildingbricks.core.materials.Material;
import com.hea3ven.buildingbricks.core.materials.MaterialStack;
import com.hea3ven.tools.commonutils.inventory.ContainerBase;
import com.hea3ven.tools.commonutils.inventory.IAdvancedSlot;
import com.hea3ven.tools.commonutils.util.PlayerUtil;
import com.hea3ven.tools.commonutils.util.PlayerUtil.HeldEquipment;

public class SlotTrowelBlockType extends Slot implements IAdvancedSlot {
	private HeldEquipment equipment;

	public SlotTrowelBlockType(EntityPlayer player, int index, int xPosition, int yPosition) {
		super(null, index, xPosition, yPosition);

		this.equipment = PlayerUtil.getHeldEquipment(player, ModBuildingBricks.trowel);
	}

	@Override
	public ItemStack onQuickMove(ContainerBase container, EntityPlayer player, int clickedButton) {
		Material mat = MaterialStack.get(equipment.getStack());
		if (mat == null)
			return null;
		BlockDescription desc = mat.getBlockRotation().get(getSlotIndex());
		if (desc == null)
			return null;
		ModBuildingBricks.trowel.setCurrentBlockType(equipment.getStack(), desc.getType());
		return null;
	}

	@Override
	public ItemStack onPickUp(EntityPlayer player, int clickedButton) {
		Material mat = MaterialStack.get(equipment.getStack());
		if (mat == null)
			return null;
		BlockDescription desc = mat.getBlockRotation().get(getSlotIndex());
		if (desc == null)
			return null;
		ModBuildingBricks.trowel.setCurrentBlockType(equipment.getStack(), desc.getType());
		return null;
	}

	@Override
	public void onSwapPlayerStack(EntityPlayer player, int equipSlot) {
// TODO: select the equipSlot blockType
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
		return false;
	}

	@Override
	public boolean transferFrom(IAdvancedSlot slot) {
		return false;
	}

	@Override
	public ItemStack getStack() {
		return getImmutableStack();
	}

	@Override
	public ItemStack decrStackSize(int amount) {
		return extract(amount);
	}

	@Override
	public ItemStack getImmutableStack() {
		Material mat = MaterialStack.get(equipment.getStack());
		if (mat == null)
			return null;
		BlockDescription desc = mat.getBlockRotation().get(getSlotIndex());
		return (desc != null) ? desc.getStack() : null;
	}

	@Override
	public ItemStack extract(int size) {
		return null;
	}
}
