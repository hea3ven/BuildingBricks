package com.hea3ven.buildingbricks.core.materials;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraftforge.fml.common.registry.GameRegistry;

import com.hea3ven.buildingbricks.core.materials.rendering.IRenderDefinition;
import com.hea3ven.buildingbricks.core.materials.rendering.RenderDefinitionRotHalf;
import com.hea3ven.buildingbricks.core.materials.rendering.RenderDefinitionSimple;
import com.hea3ven.buildingbricks.core.materials.rendering.RenderDefinitionSlab;
import com.hea3ven.buildingbricks.core.materials.rendering.RenderDefinitionStairs;
import com.hea3ven.buildingbricks.core.materials.rendering.RenderDefinitionStep;
import com.hea3ven.buildingbricks.core.materials.rendering.RenderDefinitionWall;

public enum MaterialBlockType {
	FULL("block", 1000),
	STAIRS_FIXED_INNER_CORNER("stair_inner_corner", 875),
	STAIRS_FIXED_SIDE("stair_side", 750),
	STAIRS("stairs", 750),
	STAIRS_FIXED_CORNER("stair_corner", 625),
	SLAB("slab", 500),
	STEP("step", 250),
	CORNER("corner", 125),
	WALL("wall", 1000);

	static {
		FULL.setRenderDefinition(new RenderDefinitionSimple("minecraft:block/cube_bottom_top"));
		FULL.addRecipe(true, MaterialRecipeBuilder
				.create()
				.outputAmount(2)
				.pattern("xx", "xx")
				.map('x', SLAB)
				.validate());
		STAIRS.setRenderDefinition(new RenderDefinitionStairs("minecraft:block/stairs"));
//		STAIRS_FIXED_INNER_CORNER
//				.setRenderDefinition(new RenderDefinitionStairs("minecraft:block/inner_stairs"));
//		STAIRS_FIXED_SIDE.setRenderDefinition(new RenderDefinitionStairs("minecraft:block/stairs"));
//		STAIRS_FIXED_CORNER
//				.setRenderDefinition(new RenderDefinitionStairs("minecraft:block/outer_stairs"));
		SLAB.setRenderDefinition(new RenderDefinitionSlab());
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
		STEP.setRenderDefinition(new RenderDefinitionStep());
		STEP.addRecipe(false, MaterialRecipeBuilder
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
		CORNER.setRenderDefinition(
				new RenderDefinitionRotHalf("buildingbricks:block/corner_bottom"));
		CORNER.addRecipe(false, MaterialRecipeBuilder
				.create()
				.outputAmount(6)
				.pattern("xxx")
				.map('x', STEP)
				.validate());
		WALL.setRenderDefinition(new RenderDefinitionWall());
	}

	public static MaterialBlockType getBlockType(int id) {
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
	private IRenderDefinition renderDefinition;
	private List<MaterialRecipeBuilder> allwaysRecipes;
	private List<MaterialRecipeBuilder> materialRecipes;
	private int volume = 0;

	private MaterialBlockType(String name, int volume) {
		this.name = name;
		this.renderDefinition = null;
		allwaysRecipes = Lists.newArrayList();
		materialRecipes = Lists.newArrayList();
		this.volume = volume;
	}

	private void setRenderDefinition(IRenderDefinition renderDefinition) {
		this.renderDefinition = renderDefinition;
	}

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

	public void addRecipes(Material mat) {
		for (MaterialRecipeBuilder recipeDesc : allwaysRecipes) {
			GameRegistry.addShapedRecipe(recipeDesc.buildOutput(mat, this), recipeDesc.build(mat));
		}
		if (MaterialBlockRegistry.instance.getAllBlocks().contains(mat.getBlock(this).getBlock())) {
			for (MaterialRecipeBuilder recipeDesc : materialRecipes) {
				GameRegistry.addShapedRecipe(recipeDesc.buildOutput(mat, this),
						recipeDesc.build(mat));

			}
		}
	}

	public int getVolume() {
		return volume;
	}

}
