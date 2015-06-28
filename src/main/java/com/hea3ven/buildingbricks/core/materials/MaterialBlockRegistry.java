package com.hea3ven.buildingbricks.core.materials;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagString;

import net.minecraftforge.fml.common.registry.GameRegistry;

import com.hea3ven.buildingbricks.core.blocks.BlockMaterialCorner;
import com.hea3ven.buildingbricks.core.blocks.BlockMaterialStep;
import com.hea3ven.buildingbricks.core.lib.BlockDescription;

public class MaterialBlockRegistry {

	public static MaterialBlockRegistry instance = new MaterialBlockRegistry();

	private HashMap<MaterialBlockType, HashMap<StructureMaterial, Block>> blocks = new HashMap<MaterialBlockType, HashMap<StructureMaterial, Block>>();
	private HashMap<MaterialBlockType, HashMap<StructureMaterial, Set<Material>>> blocksMaterials = new HashMap<MaterialBlockType, HashMap<StructureMaterial, Set<Material>>>();

	public BlockMaterialStep materialRockStep;
	public BlockMaterialCorner materialRockCorner;

	private MaterialBlockRegistry() {
		materialRockStep = new BlockMaterialStep(StructureMaterial.ROCK, "material_rock_step");
		MaterialBlockType.STEP.setBlock(materialRockStep);
		blocks.put(MaterialBlockType.STEP, new HashMap<StructureMaterial, Block>());
		blocks.get(MaterialBlockType.STEP).put(StructureMaterial.ROCK, materialRockStep);
		blocksMaterials
				.put(MaterialBlockType.STEP, new HashMap<StructureMaterial, Set<Material>>());
		blocksMaterials.get(MaterialBlockType.STEP).put(StructureMaterial.ROCK,
				new HashSet<Material>());

		materialRockCorner = new BlockMaterialCorner(StructureMaterial.ROCK, "material_rock_corner");
		MaterialBlockType.CORNER.setBlock(materialRockCorner);
		blocks.put(MaterialBlockType.CORNER, new HashMap<StructureMaterial, Block>());
		blocks.get(MaterialBlockType.CORNER).put(StructureMaterial.ROCK, materialRockCorner);
		blocksMaterials.put(MaterialBlockType.CORNER,
				new HashMap<StructureMaterial, Set<Material>>());
		blocksMaterials.get(MaterialBlockType.CORNER).put(StructureMaterial.ROCK,
				new HashSet<Material>());
	}

	public void init() {
		GameRegistry.registerBlock(materialRockStep, "material_rock_step");
		GameRegistry.registerBlock(materialRockCorner, "material_rock_corner");
	}

	public HashMap<MaterialBlockType, HashMap<StructureMaterial, Block>> getBlocks() {
		return blocks;
	}

	public HashMap<MaterialBlockType, HashMap<StructureMaterial, Set<Material>>> getBlocksMaterials() {
		return blocksMaterials;
	}

	public BlockDescription addBlock(MaterialBlockType blockType, Material mat) {
		blocksMaterials.get(blockType).get(mat.getStructureMaterial()).add(mat);
		return new BlockDescription(blocks.get(blockType).get(mat.getStructureMaterial()), 0,
				"material", new NBTTagString(mat.materialId()));
	}

}
