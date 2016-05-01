package com.hea3ven.buildingbricks.compat.jei;

import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;

import javax.annotation.Nonnull;

import com.hea3ven.buildingbricks.compat.jei.material.MaterialRecipeCategory;
import com.hea3ven.buildingbricks.compat.jei.material.MaterialRecipeHandler;

@JEIPlugin
public class JeiModPluginBuildingBricks implements IModPlugin {
	@Override
	public void register(@Nonnull IModRegistry registry) {
		registry.addRecipeCategories(new MaterialRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
		registry.addRecipeHandlers(new MaterialRecipeHandler());
		registry.addRecipes(MaterialRecipeHandler.createRecipes());
	}

	@Override
	public void onRuntimeAvailable(@Nonnull IJeiRuntime jeiRuntime) {

	}
}
