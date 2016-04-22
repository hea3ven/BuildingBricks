package com.hea3ven.buildingbricks.core.materials;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

import com.hea3ven.buildingbricks.core.items.crafting.RecipeBlockMaterial;
import com.hea3ven.tools.commonutils.item.crafting.RecipeBuilder;

public class MaterialBlockRecipes {
	public static List<MaterialBlockRecipeBuilder> getForType(StructureMaterial structMat,
			MaterialBlockType blockType) {
		switch (blockType) {
			case FULL:
				return Lists.newArrayList(new MaterialBlockRecipeBuilder().outputAmount(2)
						.ingredients("xx", "xx", "x", "SLAB"));
			case STAIRS:
				return Lists.newArrayList(
						new MaterialBlockRecipeBuilder().outputAmount(4)
								.ingredients("x  ", "xx ", "xxx", "x", "FULL"));
			case SLAB:
				return Lists.newArrayList(
						new MaterialBlockRecipeBuilder().outputAmount(6).ingredients("xxx", "x", "FULL"),
						new MaterialBlockRecipeBuilder().outputAmount(1).ingredients("xx", "x", "STEP"));
			case VERTICAL_SLAB:
				return Lists.newArrayList();
			case STEP:
				return Lists.newArrayList(
						new MaterialBlockRecipeBuilder().outputAmount(6).ingredients("xxx", "x", "SLAB"),
						new MaterialBlockRecipeBuilder().outputAmount(1).ingredients("xx", "x", "CORNER"));
			case CORNER:
				return Lists.newArrayList(
						new MaterialBlockRecipeBuilder().outputAmount(6).ingredients("xxx", "x", "STEP"));
			case WALL:
				switch (structMat) {
					default:
						return Lists.newArrayList(new MaterialBlockRecipeBuilder().outputAmount(6)
								.ingredients("xxx", "xxx", "x", "FULL"));
					case GLASS:
					case ICE:
					case PACKED_ICE:
						return Lists.newArrayList(new MaterialBlockRecipeBuilder().outputAmount(4)
								.ingredients("xyx", "xyx", "x", "SLAB", "y", "FULL"));
				}
			case FENCE:
				return Lists.newArrayList(new MaterialBlockRecipeBuilder().outputAmount(6)
						.ingredients("xyx", "xyx", "x", "FULL", "y", "stickWood"));
			case FENCE_GATE:
				return Lists.newArrayList(
						new MaterialBlockRecipeBuilder().ingredients("xyx", "xyx", "x", "stickWood", "y",
								"FULL"));
			case PANE:
				return Lists.newArrayList(
						new MaterialBlockRecipeBuilder().ingredients("xxx", "xxx", "x", "FULL"));
			default:
				throw new UnsupportedOperationException("Missing recipes for block type " + blockType);
		}
	}

	public static class MaterialBlockRecipeBuilder extends RecipeBuilder<MaterialBlockRecipeBuilder> {

		private Material mat;
		private MaterialBlockType type;

		public MaterialBlockRecipeBuilder bind(Material mat) {
			this.mat = mat;
			return this;
		}

		public MaterialBlockRecipeBuilder output(MaterialBlockType type) {
			this.type = type;
			return this;
		}

		@Override
		public IRecipe build() {
			output(mat.getBlock(type).getStack());
			return super.build();
		}

		@Override
		protected Object parseIngredient(String ingredient) {
			for (MaterialBlockType blockType : MaterialBlockType.values()) {
				if (blockType.toString().equals(ingredient))
					return mat.getBlock(blockType).getStack();
			}
			return super.parseIngredient(ingredient);
		}

		@Override
		protected IRecipe createShapedRecipe(ItemStack result, Object[] inputs) {
			return new RecipeBlockMaterial(result, inputs);
		}
	}
}
