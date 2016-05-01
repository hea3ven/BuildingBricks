package com.hea3ven.buildingbricks.compat.jei.material;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;

import javax.annotation.Nonnull;

import net.minecraft.block.BlockTallGrass;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;

import com.hea3ven.buildingbricks.compat.jei.material.MaterialRecipeHandler.MaterialRecipe;
import com.hea3ven.buildingbricks.core.client.gui.GuiTrowel;
import com.hea3ven.buildingbricks.core.materials.BlockDescription;
import com.hea3ven.buildingbricks.core.materials.BlockRotation;
import com.hea3ven.buildingbricks.core.materials.MaterialBlockType;

public class MaterialRecipeCategory implements IRecipeCategory {

	private final IDrawableStatic background;

	public MaterialRecipeCategory(IGuiHelper guiHelper) {

		background = guiHelper.createDrawable(GuiTrowel.BG_RESOURCE, 43, 8, 126, 72, 0, 0, 0, 0);
	}

	@Nonnull
	@Override
	public String getUid() {
		return MaterialRecipeHandler.uid;
	}

	@Nonnull
	@Override
	public String getTitle() {
		return I18n.translateToLocal("gui.buildingbricks.jei.material");
	}

	@Nonnull
	@Override
	public IDrawable getBackground() {
		return background;
	}

	@Override
	public void drawExtras(@Nonnull Minecraft minecraft) {

	}

	@Override
	public void drawAnimations(@Nonnull Minecraft minecraft) {

	}

	@Override
	public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull IRecipeWrapper recipeWrapper) {

		IGuiItemStackGroup itemStacks = recipeLayout.getItemStacks();
		MaterialRecipe recipe = (MaterialRecipe) recipeWrapper;

		itemStacks.init(0, true, 0, 27);
		itemStacks.setFromRecipe(0, recipe.getMain());
		int xOff = 54;
		int yOff = 0;
		BlockRotation blockRotation = recipe.getMaterial().getBlockRotation();
		MaterialBlockType blockType = blockRotation.getFirst();
		for (int i = 1; i <= blockRotation.getAll().size(); i++) {
			BlockDescription blockDesc = blockRotation.get(blockType);
			if (blockDesc.getStack() != null) {
				itemStacks.init(i, false, xOff + 18 * ((i - 1) % 4), yOff + 18 * ((i - 1) / 4));
				itemStacks.setFromRecipe(i, blockDesc.getStack());
			}
			blockType = blockRotation.getNext(blockType);
		}
	}
}
