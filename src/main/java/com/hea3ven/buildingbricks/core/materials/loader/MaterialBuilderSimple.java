package com.hea3ven.buildingbricks.core.materials.loader;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.ImmutableMap;

import net.minecraft.nbt.NBTBase;

import com.hea3ven.buildingbricks.core.materials.BlockDescription;
import com.hea3ven.buildingbricks.core.materials.Material;
import com.hea3ven.buildingbricks.core.materials.MaterialBlockRecipes.MaterialBlockRecipeBuilder;
import com.hea3ven.buildingbricks.core.materials.MaterialBlockType;
import com.hea3ven.buildingbricks.core.materials.StructureMaterial;

public class MaterialBuilderSimple extends MaterialBuilder {
	private Material mat;

	public MaterialBuilderSimple(String id) {
		mat = new Material(id);
	}

	@Override
	public MaterialBuilder setStructureMaterial(StructureMaterial type) {
		mat.setStructureMaterial(type);
		return this;
	}

	@Override
	public MaterialBuilder setHardness(float hardness) {
		mat.setHardness(hardness);
		return this;
	}

	@Override
	public MaterialBuilder setResistance(float resistance) {
		mat.setResistance(resistance);
		return this;
	}

	@Override
	public MaterialBuilder setNormalHarvestMaterial(String normalHarvest) {
		mat.setNormalHarvestMaterial(normalHarvest);
		return this;
	}

	@Override
	public MaterialBuilder setSilkHarvestMaterial(String silkHarvest) {
		mat.setSilkHarvestMaterial(silkHarvest);
		return this;
	}

	@Override
	public void setTextures(ImmutableMap<String, String> textures) {
		String baseTexture = textures.containsKey("all") ? textures.get("all") : textures.get("side");
		for (Entry<String, String> entry : textures.entrySet()) {
			mat.setTexture(entry.getKey(), entry.getValue());
		}
		if (!textures.containsKey("all"))
			mat.setTexture("all", baseTexture);
		if (!textures.containsKey("side"))
			mat.setTexture("side", baseTexture);
		if (!textures.containsKey("top"))
			mat.setTexture("top", baseTexture);
		if (!textures.containsKey("bottom"))
			mat.setTexture("bottom", baseTexture);
		if (!textures.containsKey("north"))
			mat.setTexture("north", baseTexture);
		if (!textures.containsKey("south"))
			mat.setTexture("south", baseTexture);
		if (!textures.containsKey("east"))
			mat.setTexture("east", baseTexture);
		if (!textures.containsKey("west"))
			mat.setTexture("west", baseTexture);
		if (!textures.containsKey("particle"))
			mat.setTexture("particle", baseTexture);
		if (!textures.containsKey("wall"))
			mat.setTexture("wall", baseTexture);
		if (!textures.containsKey("texture"))
			mat.setTexture("texture", baseTexture);
		if (!textures.containsKey("pane"))
			mat.setTexture("pane", baseTexture);
		if (!textures.containsKey("edge"))
			mat.setTexture("edge", baseTexture);
		if (!textures.containsKey("layer0"))
			mat.setTexture("layer0", baseTexture);
	}

	@Override
	public void addBlock(MaterialBlockType type, String blockName, int metadata, Map<String, NBTBase> tags,
			List<MaterialBlockRecipeBuilder> recipes) {
		mat.addBlock(new BlockDescription(type, blockName, metadata, tags, recipes));
	}

	@Override
	public void addBlock(MaterialBlockType type, List<MaterialBlockRecipeBuilder> recipes) {
		mat.addBlock(new BlockDescription(type, recipes));
	}

	public Material build() {
		return mat;
	}
}
