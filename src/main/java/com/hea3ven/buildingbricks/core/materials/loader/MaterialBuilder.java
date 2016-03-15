package com.hea3ven.buildingbricks.core.materials.loader;

import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import net.minecraft.nbt.NBTBase;

import com.hea3ven.buildingbricks.core.materials.Material;
import com.hea3ven.buildingbricks.core.materials.MaterialBlockRecipes.MaterialBlockRecipeBuilder;
import com.hea3ven.buildingbricks.core.materials.MaterialBlockType;
import com.hea3ven.buildingbricks.core.materials.StructureMaterial;

public abstract class MaterialBuilder {

	public abstract MaterialBuilder setStructureMaterial(StructureMaterial type);

	public abstract MaterialBuilder setHardness(float hardness);

	public abstract MaterialBuilder setResistance(float resistance);

	public abstract MaterialBuilder setNormalHarvestMaterial(String normalHarvest);

	public abstract MaterialBuilder setSilkHarvestMaterial(String silkHarvest);

	public abstract void setTextures(ImmutableMap<String, String> textures);

	public abstract void addBlock(MaterialBlockType type, String blockName, int metadata,
			Map<String, NBTBase> tags, List<MaterialBlockRecipeBuilder> recipes);

	public abstract void addBlock(MaterialBlockType type, List<MaterialBlockRecipeBuilder> recipes);

	public abstract Material[] build();
}
