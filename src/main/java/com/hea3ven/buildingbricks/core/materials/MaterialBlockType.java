package com.hea3ven.buildingbricks.core.materials;

import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import net.minecraft.item.crafting.IRecipe;

import net.minecraftforge.oredict.ShapedOreRecipe;

public enum MaterialBlockType {
	FULL("block", 1000),
	STAIRS("stairs", 750),
	SLAB("slab", 500),
	VERTICAL_SLAB("vertical_slab", 500),
	STEP("step", 250),
	CORNER("corner", 125),
	WALL("wall", 1000),
	FENCE("fence", 1500),
	FENCE_GATE("fence_gate", 3000);

	static {
		FULL.addRecipe(true,
				MaterialRecipeBuilder.create().outputAmount(2).pattern("xx", "xx").map('x', SLAB).validate());
		STAIRS.addRecipe(false, MaterialRecipeBuilder.create()
				.outputAmount(2)
				.pattern("x  ", "xx ", "xxx")
				.map('x', FULL)
				.validate());
		SLAB.addRecipe(false,
				MaterialRecipeBuilder.create().outputAmount(6).pattern("xxx").map('x', FULL).validate());
		SLAB.addRecipe(true,
				MaterialRecipeBuilder.create().outputAmount(1).pattern("xx").map('x', STEP).validate());
		SLAB.addRecipe(true, MaterialRecipeBuilder.create()
				.outputAmount(3)
				.pattern("xxx")
				.map('x', VERTICAL_SLAB)
				.validate());
		VERTICAL_SLAB.addRecipe(true, MaterialRecipeBuilder.create()
				.outputAmount(3)
				.pattern("x", "x", "x")
				.map('x', SLAB)
				.validate());
		VERTICAL_SLAB.addRecipe(true, MaterialRecipeBuilder.create()
				.outputAmount(6)
				.pattern("x", "x", "x")
				.map('x', FULL)
				.validate());
		STEP.addRecipe(true,
				MaterialRecipeBuilder.create().outputAmount(6).pattern("xxx").map('x', SLAB).validate());
		STEP.addRecipe(true,
				MaterialRecipeBuilder.create().outputAmount(1).pattern("xx").map('x', CORNER).validate());
		CORNER.addRecipe(true,
				MaterialRecipeBuilder.create().outputAmount(6).pattern("xxx").map('x', STEP).validate());
		WALL.addRecipe(false, MaterialRecipeBuilder.create()
				.outputAmount(6)
				.pattern("xxx", "xxx")
				.map('x', FULL)
				.validate());
		FENCE.addRecipe(false, MaterialRecipeBuilder.create()
				.outputAmount(6)
				.pattern("xyx", "xyx")
				.map('x', FULL)
				.map('y', "stickWood")
				.validate());
		FENCE_GATE.addRecipe(false, MaterialRecipeBuilder.create()
				.pattern("xyx", "xyx")
				.map('x', "stickWood")
				.map('y', FULL)
				.validate());
	}

	public static MaterialBlockType getBlockType(int id) {
		if (id >= values().length)
			return FULL;
		return values()[id];
	}

	public static MaterialBlockType getForVolume(int volume) {
		for (MaterialBlockType blockType : values()) {
			if (blockType.getVolume() == volume)
				return blockType;
		}
		return null;
	}

	public static MaterialBlockType getBestForVolume(int volume) {
		for (MaterialBlockType blockType : values()) {
			if (blockType.getVolume() <= volume)
				return blockType;
		}
		return null;
	}

	private String name;
	private List<MaterialRecipeBuilder> allwaysRecipes;
	private List<MaterialRecipeBuilder> materialRecipes;
	private int volume = 0;

	MaterialBlockType(String name, int volume) {
		this.name = name;
		allwaysRecipes = Lists.newArrayList();
		materialRecipes = Lists.newArrayList();
		this.volume = volume;
	}

	public String getName() {
		return name;
	}

	private void addRecipe(boolean allways, MaterialRecipeBuilder recipe) {
		if (allways) {
			allwaysRecipes.add(recipe);
		} else {
			materialRecipes.add(recipe);
		}
	}

	public Set<IRecipe> registerRecipes(Material mat) {
		Set<IRecipe> recipes = Sets.newHashSet();
		for (MaterialRecipeBuilder recipeDesc : allwaysRecipes) {
			Object[] recipe = recipeDesc.build(mat);
			if (recipe != null) {
				recipes.add(new ShapedOreRecipe(recipeDesc.buildOutput(mat, this), recipe));
			}
		}
		if (MaterialBlockRegistry.instance.getAllBlocks().contains(mat.getBlock(this).getBlock())) {
			for (MaterialRecipeBuilder recipeDesc : materialRecipes) {
				Object[] recipe = recipeDesc.build(mat);
				if (recipe != null) {
					recipes.add(new ShapedOreRecipe(recipeDesc.buildOutput(mat, this), recipe));
				}
			}
		}
		return recipes;
	}

	public int getVolume() {
		return volume;
	}

	public String getTranslationKey() {
		String name = getName().toLowerCase();
		while (name.indexOf('_') != -1) {
			int i = name.indexOf('_');
			name = name.substring(0, i) + Character.toUpperCase(name.charAt(i + 1)) + name.substring(i + 2);
		}
		return "blockType." + name;
	}

}
