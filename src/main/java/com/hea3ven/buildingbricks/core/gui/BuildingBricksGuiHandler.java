package com.hea3ven.buildingbricks.core.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import net.minecraftforge.fml.common.network.IGuiHandler;

import com.hea3ven.buildingbricks.core.ModBuildingBricks;
import com.hea3ven.buildingbricks.core.client.gui.GuiTrowel;

public class BuildingBricksGuiHandler implements IGuiHandler {

	public static final int GUI_TROWEL = 0;

	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		if (id == GUI_TROWEL) {
			ItemStack trowel = player.getCurrentEquippedItem();
			if (trowel.getItem() == ModBuildingBricks.trowel) {
				return ModBuildingBricks.trowel.getContainer(player, trowel);
			}
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		if (id == GUI_TROWEL) {
			ItemStack trowel = player.getCurrentEquippedItem();
			if (trowel.getItem() == ModBuildingBricks.trowel) {
				return new GuiTrowel(player, trowel);
			}
		}
		return null;
	}
}
