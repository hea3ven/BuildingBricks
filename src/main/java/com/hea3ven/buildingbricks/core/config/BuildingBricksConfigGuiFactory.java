package com.hea3ven.buildingbricks.core.config;

import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

import cpw.mods.fml.client.IModGuiFactory;
import cpw.mods.fml.client.config.GuiConfig;

import com.hea3ven.buildingbricks.ModBuildingBricks;

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

	public static class GuiConfigBuildingBricks extends GuiConfig {

		public GuiConfigBuildingBricks(GuiScreen parentScreen) {
			super(parentScreen, Config.getConfigElements(), ModBuildingBricks.MODID, false, false,
					"Building Bricks Configuration");
		}

	}
}
