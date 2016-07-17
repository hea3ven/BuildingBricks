package com.hea3ven.buildingbricks.core.materials;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.crafting.IRecipe;

import com.hea3ven.tools.commonutils.item.crafting.RecipeBuilder;

public class MaterialBlockRecipes {
	public static List<MaterialBlockRecipeBuilder> getForType(StructureMaterial structMat,
			MaterialBlockType blockType) {
		List<MaterialBlockRecipeBuilder> recipes = new ArrayList<>();
		switch (blockType) {
			case FULL:
				recipes.add(new MaterialBlockRecipeBuilder().outputAmount(2)
						.ingredients("xx", "xx", "x", "SLAB"));
				break;
			case STAIRS:
				recipes.add(new MaterialBlockRecipeBuilder().outputAmount(4)
						.ingredients("x  ", "xx ", "xxx", "x", "FULL"));
				break;
			case SLAB:
				recipes.add(new MaterialBlockRecipeBuilder().outputAmount(6).ingredients("xxx", "x", "FULL"));
				recipes.add(new MaterialBlockRecipeBuilder().outputAmount(1).ingredients("xx", "x", "STEP"));
				break;
			case VERTICAL_SLAB:
				break;
			case STEP:
				recipes.add(new MaterialBlockRecipeBuilder().outputAmount(6).ingredients("xxx", "x", "SLAB"));
				recipes.add(
						new MaterialBlockRecipeBuilder().outputAmount(1).ingredients("xx", "x", "CORNER"));
				break;
			case CORNER:
				recipes.add(new MaterialBlockRecipeBuilder().outputAmount(6).ingredients("xxx", "x", "STEP"));
				break;
			case WALL:
				switch (structMat) {
					default:
						recipes.add(new MaterialBlockRecipeBuilder().outputAmount(6)
								.ingredients("xxx", "xxx", "x", "FULL"));
						break;
					case GLASS:
					case ICE:
					case PACKED_ICE:
						recipes.add(new MaterialBlockRecipeBuilder().outputAmount(4)
								.ingredients("xyx", "xyx", "x", "SLAB", "y", "FULL"));
						break;
				}
				break;
			case FENCE:
				recipes.add(new MaterialBlockRecipeBuilder().outputAmount(6)
						.ingredients("xyx", "xyx", "x", "FULL", "y", "stickWood"));
				break;
			case FENCE_GATE:
				recipes.add(new MaterialBlockRecipeBuilder().ingredients("xyx", "xyx", "x", "stickWood", "y",
						"FULL"));
				break;
			case PANE:
				recipes.add(new MaterialBlockRecipeBuilder().ingredients("xxx", "xxx", "x", "FULL"));
				break;
			default:
				throw new UnsupportedOperationException("Missing recipes for block type " + blockType);
		}
		return recipes;
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
				if (blockType.toString().equals(ingredient)) {
					if (MaterialBlockRegistry.instance.enabledBlocks.get(blockType)) {
						return mat.getBlock(blockType).getStack();
					} else {
						throw new DisabledMaterialBlockRecipe();
					}
				}
			}
			return super.parseIngredient(ingredient);
		}

		public class DisabledMaterialBlockRecipe extends RuntimeException {
		}
	}
}
