package com.hea3ven.buildingbricks.compat.jei.material;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import net.minecraftforge.fluids.FluidStack;

import com.hea3ven.buildingbricks.compat.jei.material.MaterialRecipeHandler.MaterialRecipe;
import com.hea3ven.buildingbricks.core.materials.BlockDescription;
import com.hea3ven.buildingbricks.core.materials.Material;
import com.hea3ven.buildingbricks.core.materials.MaterialRegistry;

public class MaterialRecipeHandler implements IRecipeHandler<MaterialRecipe> {
	public static String uid = "buildingbricks.material";

	public static List<MaterialRecipe> createRecipes() {
		return MaterialRegistry.getAll().stream().map(MaterialRecipe::new).collect(Collectors.toList());
	}

	@Nonnull
	@Override
	public Class getRecipeClass() {
		return MaterialRecipe.class;
	}

	@Nonnull
	@Override
	public String getRecipeCategoryUid() {
		return uid;
	}

	@Nonnull
	@Override
	public String getRecipeCategoryUid(@Nonnull MaterialRecipe recipe) {
		return uid;
	}

	@Nonnull
	@Override
	public IRecipeWrapper getRecipeWrapper(@Nonnull MaterialRecipe recipe) {
		return recipe;
	}

	@Override
	public boolean isRecipeValid(@Nonnull MaterialRecipe recipe) {
		return true;
	}

	static class MaterialRecipe implements IRecipeWrapper {

		private final Material mat;

		public MaterialRecipe(Material mat) {
			this.mat = mat;
		}

		public Material getMaterial() {
			return mat;
		}

		public ItemStack getMain() {
			return mat.getFirstBlock().getStack();
		}

		@Override
		public List getInputs() {
			return Lists.transform(new ArrayList<>(mat.getBlockRotation().getAll().values()),
					new Function<BlockDescription, ItemStack>() {
						@Nullable
						@Override
						public ItemStack apply(BlockDescription input) {
							return input.getStack();
						}
					});
		}

		@Override
		public List getOutputs() {
			return Lists.transform(new ArrayList<>(mat.getBlockRotation().getAll().values()),
					new Function<BlockDescription, ItemStack>() {
						@Nullable
						@Override
						public ItemStack apply(BlockDescription input) {
							return input.getStack();
						}
					});
		}

		@Override
		public List<FluidStack> getFluidInputs() {
			return null;
		}

		@Override
		public List<FluidStack> getFluidOutputs() {
			return null;
		}

		@Override
		public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX,
				int mouseY) {

		}

		@Override
		public void drawAnimations(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight) {

		}

		@Nullable
		@Override
		public List<String> getTooltipStrings(int mouseX, int mouseY) {
			return null;
		}

		@Override
		public boolean handleClick(@Nonnull Minecraft minecraft, int mouseX, int mouseY, int mouseButton) {
			return false;
		}
	}
}
