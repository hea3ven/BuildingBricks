package com.hea3ven.buildingbricks.core.config;

import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

import net.minecraftforge.fml.client.IModGuiFactory;

import com.hea3ven.buildingbricks.core.ModBuildingBricks;
import com.hea3ven.tools.commonutils.mod.config.GuiConfigAutomatic;

public class BuildingBricksConfigGuiFactory implements IModGuiFactory {

	@Override
	public void initialize(Minecraft mc) {
	}

	@Override
	public Class<? extends GuiScreen> mainConfigGuiClass() {
		return GuiConfigBuildingBricks.class;
	}

	@Override
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
		return null;
	}

	@Override
	public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element) {
		return null;
	}

	public static class GuiConfigBuildingBricks extends GuiConfigAutomatic {

		public GuiConfigBuildingBricks(GuiScreen parentScreen) {
			super(parentScreen, ModBuildingBricks.proxy);
		}
	}
}
