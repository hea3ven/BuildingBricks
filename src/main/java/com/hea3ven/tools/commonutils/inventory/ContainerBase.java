package com.hea3ven.tools.commonutils.inventory;

import java.lang.reflect.Constructor;

import org.apache.commons.lang3.reflect.ConstructorUtils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public abstract class ContainerBase extends Container {

	public void addInventoryGrid(IInventory inv, int slotOff, int xOff, int yOff, int xSize, int ySize) {
		addInventoryGrid(slotOff, xOff, yOff, xSize, ySize, Slot.class, inv);
	}

	public void addInventoryGrid(int slotOff, int xOff, int yOff, int xSize, int ySize,
			Class<? extends Slot> cls, Object... args) {
		try {
			Class<?>[] argsTypes = new Class[args.length + 3];
			int i = 0;
			for (; i < args.length; i++)
				argsTypes[i] = args[i].getClass();
			argsTypes[i++] = Integer.TYPE;
			argsTypes[i++] = Integer.TYPE;
			argsTypes[i] = Integer.TYPE;

			Constructor<? extends Slot> ctor =
					ConstructorUtils.getMatchingAccessibleConstructor(cls, argsTypes);
			for (int y = 0; y < ySize; ++y) {
				for (int x = 0; x < xSize; ++x) {
					Object[] objArgs = new Object[args.length + 3];
					for (i = 0; i < args.length; i++)
						objArgs[i] = args[i];
					objArgs[i++] = slotOff + x + y * xSize;
					objArgs[i++] = xOff + x * 18;
					objArgs[i] = yOff + y * 18;
					this.addSlotToContainer(ctor.newInstance(objArgs));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public ItemStack slotClick(int slotId, int clickedButton, int mode, EntityPlayer playerIn) {
		if (slotId >= 0 && mode == 0) {
			Slot slot = (Slot) this.inventorySlots.get(slotId);
			if (slot instanceof SlotGhost) {
				ItemStack itemstack4 = playerIn.inventory.getItemStack();
				if (itemstack4 != null && slot.isItemValid(itemstack4)) {
					int l1 = clickedButton == 0 ? itemstack4.stackSize : 1;

					if (l1 > slot.getItemStackLimit(itemstack4)) {
						l1 = slot.getItemStackLimit(itemstack4);
					}

					if (itemstack4.stackSize >= l1) {
						slot.putStack(itemstack4.splitStack(l1));
					}

					if (itemstack4.stackSize == 0) {
						playerIn.inventory.setItemStack(null);
					}
				}
				return null;
			}
		}

		return super.slotClick(slotId, clickedButton, mode, playerIn);
	}
}
