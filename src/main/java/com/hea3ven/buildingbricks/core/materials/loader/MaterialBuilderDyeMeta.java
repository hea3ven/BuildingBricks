package com.hea3ven.buildingbricks.core.materials.loader;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.ImmutableMap;

import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.StringUtils;

import com.hea3ven.buildingbricks.core.block.behavior.BlockBehaviorBase;
import com.hea3ven.buildingbricks.core.materials.MaterialBlockRecipes.MaterialBlockRecipeBuilder;
import com.hea3ven.buildingbricks.core.materials.MaterialBlockType;
import com.hea3ven.buildingbricks.core.materials.StructureMaterial;

public class MaterialBuilderDyeMeta extends MaterialBuilder {
	private MaterialBuilderSimple[] mats;

	public MaterialBuilderDyeMeta(Map<String, MaterialBuilderSimple> materials, String id) {
		mats = new MaterialBuilderSimple[16];
		for (int i = 0; i < 16; i++) {
			String subId = id + "_" + EnumDyeColor.byMetadata(i).getName();
			mats[i] = materials.get(subId);
			if (mats[i] == null) {
				mats[i] = new MaterialBuilderSimple(subId);
				materials.put(subId, mats[i]);
			}
		}
	}

	@Override
	public MaterialBuilder setStructureMaterial(StructureMaterial type) {
		for (MaterialBuilder mat : mats) {
			mat.setStructureMaterial(type);
		}
		return this;
	}

	@Override
	public MaterialBuilder setBehavior(BlockBehaviorBase behavior) {
		for (MaterialBuilder mat : mats) {
			mat.setBehavior(behavior);
		}
		return this;
	}

	@Override
	public MaterialBuilder setHardness(float hardness) {
		for (MaterialBuilder mat : mats) {
			mat.setHardness(hardness);
		}
		return this;
	}

	@Override
	public MaterialBuilder setResistance(float resistance) {
		for (MaterialBuilder mat : mats) {
			mat.setResistance(resistance);
		}
		return this;
	}

	@Override
	public MaterialBuilder setNormalHarvestMaterial(String normalHarvest) {
		int i = 0;
		for (MaterialBuilder mat : mats) {
			String postfix = "_" + EnumDyeColor.byMetadata(i++).getName();
			mat.setNormalHarvestMaterial(
					!StringUtils.isNullOrEmpty(normalHarvest) ? normalHarvest + postfix : null);
		}
		return this;
	}

	@Override
	public MaterialBuilder setSilkHarvestMaterial(String silkHarvest) {
		int i = 0;
		for (MaterialBuilder mat : mats) {
			String postfix = "_" + EnumDyeColor.byMetadata(i++).getName();
			mat.setSilkHarvestMaterial(
					!StringUtils.isNullOrEmpty(silkHarvest) ? silkHarvest + postfix : null);
		}
		return this;
	}

	@Override
	public MaterialBuilder setUvlock(boolean uvlock) {
		for (MaterialBuilder mat : mats) {
			mat.setUvlock(uvlock);
		}
		return this;
	}

	@Override
	public void setTextures(ImmutableMap<String, String> textures) {
		int i = 0;
		for (MaterialBuilder mat : mats) {
			String postfix = "_" + EnumDyeColor.byMetadata(i++).getName();
			ImmutableMap.Builder<String, String> subTextures = ImmutableMap.builder();
			for (Entry<String, String> entry : textures.entrySet()) {
				subTextures.put(entry.getKey(), entry.getValue() + postfix);
			}
			mat.setTextures(subTextures.build());
		}
	}

	@Override
	public void addBlock(MaterialBlockType type, String blockName, int metadata, Map<String, NBTBase> tags,
			List<MaterialBlockRecipeBuilder> recipes) {
		int i = 0;
		for (MaterialBuilder mat : mats) {
			mat.addBlock(type, blockName, metadata != -1 ? metadata : i++, tags, recipes);
		}
	}

	@Override
	public void addBlock(MaterialBlockType type, List<MaterialBlockRecipeBuilder> recipes) {
		for (MaterialBuilder mat : mats) {
			mat.addBlock(type, recipes);
		}
	}
}
