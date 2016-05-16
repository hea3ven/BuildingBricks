package com.hea3ven.buildingbricks.core.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.hea3ven.buildingbricks.core.ModBuildingBricks;
import com.hea3ven.buildingbricks.core.materials.Material;
import com.hea3ven.buildingbricks.core.materials.MaterialRegistry;
import com.hea3ven.buildingbricks.core.materials.MaterialStack;
import com.hea3ven.tools.commonutils.inventory.ContainerBase;
import com.hea3ven.tools.commonutils.inventory.IAdvancedSlot;
import com.hea3ven.tools.commonutils.util.PlayerUtil;
import com.hea3ven.tools.commonutils.util.PlayerUtil.HeldEquipment;

public class SlotTrowelMaterial extends Slot implements IAdvancedSlot {
	private HeldEquipment equipment;

	public SlotTrowelMaterial(EntityPlayer player, int index, int xPosition, int yPosition) {
		super(null, index, xPosition, yPosition);

		this.equipment = PlayerUtil.getHeldEquipment(player, ModBuildingBricks.trowel);
	}

	@Override
	public ItemStack getStack() {
		Material mat = MaterialStack.get(equipment.stack);
		return mat != null ? mat.getFirstBlock().getStack().copy() : null;
	}

	@Override
	public ItemStack onQuickMove(ContainerBase container, EntityPlayer player, int clickedButton) {
		MaterialStack.set(equipment.stack, null);
		equipment.updatePlayer();
		return null;
	}

	@Override
	public ItemStack onPickUp(EntityPlayer player, int clickedButton) {
		ItemStack playerStack = player.inventory.getItemStack();
		if (playerStack != null) {
			Material mat = MaterialRegistry.getMaterialForStack(playerStack);
			if (mat != null)
				MaterialStack.set(equipment.stack, mat);
			else
				MaterialStack.set(equipment.stack, null);
		} else {
			MaterialStack.set(equipment.stack, null);
		}
		equipment.updatePlayer();
		return null;
	}

	@Override
	public void onSwapPlayerStack(EntityPlayer player, int equipSlot) {
		ItemStack equipStack = player.inventory.getStackInSlot(equipSlot);
		Material mat = MaterialStack.get(equipStack);
		if (mat != null) {
			MaterialStack.set(equipment.stack, mat);
			equipment.updatePlayer();
		}
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
		return true;
	}

	@Override
	public boolean transferFrom(IAdvancedSlot slot) {
		Material mat = MaterialRegistry.getMaterialForStack(slot.getImmutableStack());
		if (mat != null) {
			MaterialStack.set(equipment.stack, mat);
			equipment.updatePlayer();
		}
		return false;
	}

	@Override
	public ItemStack decrStackSize(int amount) {
		return extract(amount);
	}

	@Override
	public ItemStack extract(int size) {
		MaterialStack.set(equipment.stack, null);
		equipment.updatePlayer();
		return null;
	}

	@Override
	public ItemStack getImmutableStack() {
		return getStack();
	}
}
