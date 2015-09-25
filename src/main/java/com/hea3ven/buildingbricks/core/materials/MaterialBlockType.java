package com.hea3ven.buildingbricks.core.materials;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.hea3ven.buildingbricks.core.items.crafting.RecipeBlockMaterial;
import com.hea3ven.buildingbricks.core.materials.rendering.IRenderDefinition;
import com.hea3ven.buildingbricks.core.materials.rendering.RenderDefinitionRotHalf;
import com.hea3ven.buildingbricks.core.materials.rendering.RenderDefinitionSimple;
import com.hea3ven.buildingbricks.core.materials.rendering.RenderDefinitionSlab;
import com.hea3ven.buildingbricks.core.materials.rendering.RenderDefinitionStairs;
import com.hea3ven.buildingbricks.core.materials.rendering.RenderDefinitionStep;
import com.hea3ven.buildingbricks.core.materials.rendering.RenderDefinitionWall;

public enum MaterialBlockType {
	FULL("block", 1000),
	STAIRS("stairs", 750),
	SLAB("slab", 500),
	VERTICAL_SLAB("vertical_slab", 500),
	STEP("step", 250),
	CORNER("corner", 125),
	WALL("wall", 1000);

	static {
		FULL.addRecipe(true, MaterialRecipeBuilder
				.create()
				.outputAmount(2)
				.pattern("xx", "xx")
				.map('x', SLAB)
				.validate());
		STAIRS.addRecipe(false, MaterialRecipeBuilder
				.create()
				.outputAmount(2)
				.pattern("x  ", "xx ", "xxx")
				.map('x', FULL)
				.validate());
		SLAB.addRecipe(false, MaterialRecipeBuilder
				.create()
				.outputAmount(6)
				.pattern("xxx")
				.map('x', FULL)
				.validate());
		SLAB.addRecipe(true, MaterialRecipeBuilder
				.create()
				.outputAmount(1)
				.pattern("xx")
				.map('x', STEP)
				.validate());
		SLAB.addRecipe(true, MaterialRecipeBuilder
				.create()
				.outputAmount(3)
				.pattern("xxx")
				.map('x', VERTICAL_SLAB)
				.validate());
		VERTICAL_SLAB.addRecipe(true, MaterialRecipeBuilder
				.create()
				.outputAmount(3)
				.pattern("x", "x", "x")
				.map('x', SLAB)
				.validate());
		VERTICAL_SLAB.addRecipe(true, MaterialRecipeBuilder
				.create()
				.outputAmount(6)
				.pattern("x", "x", "x")
				.map('x', FULL)
				.validate());
		STEP.addRecipe(true, MaterialRecipeBuilder
				.create()
				.outputAmount(6)
				.pattern("xxx")
				.map('x', SLAB)
				.validate());
		STEP.addRecipe(true, MaterialRecipeBuilder
				.create()
				.outputAmount(1)
				.pattern("xx")
				.map('x', CORNER)
				.validate());
		CORNER.addRecipe(true, MaterialRecipeBuilder
				.create()
				.outputAmount(6)
				.pattern("xxx")
				.map('x', STEP)
				.validate());
		WALL.addRecipe(false, MaterialRecipeBuilder
				.create()
				.outputAmount(6)
				.pattern("xxx", "xxx")
				.map('x', FULL)
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

	@SideOnly(Side.CLIENT)
	public static void initRendering() {
		FULL.setRenderDefinition(new RenderDefinitionSimple("minecraft:block/cube_bottom_top"));
		STAIRS.setRenderDefinition(new RenderDefinitionStairs());
		SLAB.setRenderDefinition(new RenderDefinitionSlab());
		VERTICAL_SLAB.setRenderDefinition(new RenderDefinitionSlab(true));
		STEP.setRenderDefinition(new RenderDefinitionStep());
		CORNER.setRenderDefinition(
				new RenderDefinitionRotHalf("buildingbricks:block/corner_bottom"));
		WALL.setRenderDefinition(new RenderDefinitionWall());
	}

	private String name;
	@SideOnly(Side.CLIENT)
	private IRenderDefinition renderDefinition;
	private List<MaterialRecipeBuilder> allwaysRecipes;
	private List<MaterialRecipeBuilder> materialRecipes;
	private int volume = 0;

	private MaterialBlockType(String name, int volume) {
		this.name = name;
		allwaysRecipes = Lists.newArrayList();
		materialRecipes = Lists.newArrayList();
		this.volume = volume;
	}

	@SideOnly(Side.CLIENT)
	private void setRenderDefinition(IRenderDefinition renderDefinition) {
		this.renderDefinition = renderDefinition;
	}

	@SideOnly(Side.CLIENT)
	public IRenderDefinition getRenderDefinition() {
		return renderDefinition;
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

	public void registerRecipes(Material mat) {
		for (MaterialRecipeBuilder recipeDesc : allwaysRecipes) {
			Object[] recipe = recipeDesc.build(mat);
			if (recipe != null)
				GameRegistry.addRecipe(RecipeBlockMaterial
						.createRecipe(recipeDesc.buildOutput(mat, this), recipe));
		}
		if (MaterialBlockRegistry.instance.getAllBlocks().contains(mat.getBlock(this).getBlock())) {
			for (MaterialRecipeBuilder recipeDesc : materialRecipes) {
				Object[] recipe = recipeDesc.build(mat);
				if (recipe != null)
					GameRegistry.addRecipe(RecipeBlockMaterial
							.createRecipe(recipeDesc.buildOutput(mat, this), recipe));

			}
		}
	}

	public int getVolume() {
		return volume;
	}

	public String getTranslationKey() {
		String name = getName().toLowerCase();
		while (name.indexOf('_') != -1) {
			int i = name.indexOf('_');
			name = name.substring(0, i) + Character.toUpperCase(name.charAt(i + 1))
					+ name.substring(i + 2);
		}
		return "blockType." + name;
	}

}
