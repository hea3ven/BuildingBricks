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

	private HashMap<MaterialBlockType, HashMap<net.minecraft.block.material.Material, Block>> blocks = new HashMap<MaterialBlockType, HashMap<net.minecraft.block.material.Material, Block>>();
	private HashMap<MaterialBlockType, HashMap<net.minecraft.block.material.Material, Set<Material>>> blocksMaterials = new HashMap<MaterialBlockType, HashMap<net.minecraft.block.material.Material, Set<Material>>>();

	public BlockMaterialStep materialRockStep;
	public BlockMaterialCorner materialRockCorner;

	private MaterialBlockRegistry() {
		materialRockStep = new BlockMaterialStep(net.minecraft.block.material.Material.rock,
				"material_rock_step");
		MaterialBlockType.STEP.setBlock(materialRockStep);
		blocks.put(MaterialBlockType.STEP,
				new HashMap<net.minecraft.block.material.Material, Block>());
		blocks.get(MaterialBlockType.STEP).put(net.minecraft.block.material.Material.rock,
				materialRockStep);
		blocksMaterials.put(MaterialBlockType.STEP,
				new HashMap<net.minecraft.block.material.Material, Set<Material>>());
		blocksMaterials.get(MaterialBlockType.STEP).put(net.minecraft.block.material.Material.rock,
				new HashSet<Material>());
		materialRockCorner = new BlockMaterialCorner(net.minecraft.block.material.Material.rock,
				"material_rock_corner");
		MaterialBlockType.CORNER.setBlock(materialRockCorner);
		blocks.put(MaterialBlockType.CORNER,
				new HashMap<net.minecraft.block.material.Material, Block>());
		blocks.get(MaterialBlockType.CORNER).put(net.minecraft.block.material.Material.rock,
				materialRockCorner);
		blocksMaterials.put(MaterialBlockType.CORNER,
				new HashMap<net.minecraft.block.material.Material, Set<Material>>());
		blocksMaterials.get(MaterialBlockType.CORNER).put(
				net.minecraft.block.material.Material.rock, new HashSet<Material>());
	}

	public void init() {
		GameRegistry.registerBlock(materialRockStep, "material_rock_step");
		GameRegistry.registerBlock(materialRockCorner, "material_rock_corner");
	}

	public HashMap<MaterialBlockType, HashMap<net.minecraft.block.material.Material, Block>> getBlocks() {
		return blocks;
	}

	public HashMap<MaterialBlockType, HashMap<net.minecraft.block.material.Material, Set<Material>>> getBlocksMaterials() {
		return blocksMaterials;
	}

	public BlockDescription addBlock(MaterialBlockType blockType, Material mat) {
		blocksMaterials.get(blockType).get(mat.getStructureMaterial()).add(mat);
		return new BlockDescription(blocks.get(blockType).get(mat.getStructureMaterial()), 0,
				"material", new NBTTagString(mat.materialId()));
	}

}
