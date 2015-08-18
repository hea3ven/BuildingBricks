package com.hea3ven.buildingbricks.core.materials;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagString;

import net.minecraftforge.fml.common.registry.GameRegistry;

import com.hea3ven.buildingbricks.core.blocks.BlockMaterialBlock;
import com.hea3ven.buildingbricks.core.blocks.BlockMaterialCorner;
import com.hea3ven.buildingbricks.core.blocks.BlockMaterialSlab;
import com.hea3ven.buildingbricks.core.blocks.BlockMaterialStairs;
import com.hea3ven.buildingbricks.core.blocks.BlockMaterialStep;
import com.hea3ven.buildingbricks.core.blocks.BlockMaterialWall;
import com.hea3ven.buildingbricks.core.lib.BlockDescription;

public class MaterialBlockRegistry {

	public static MaterialBlockRegistry instance = new MaterialBlockRegistry();

	private HashMap<MaterialBlockType, HashMap<StructureMaterial, Block>> blocks = new HashMap<MaterialBlockType, HashMap<StructureMaterial, Block>>();
	private HashMap<MaterialBlockType, HashMap<StructureMaterial, Set<Material>>> blocksMaterials = new HashMap<MaterialBlockType, HashMap<StructureMaterial, Set<Material>>>();

	public BlockMaterialBlock materialRockBlock;
	public BlockMaterialStairs materialRockStairs;
	public BlockMaterialSlab materialRockSlab;
	public BlockMaterialStep materialRockStep;
	public BlockMaterialCorner materialRockCorner;
	public BlockMaterialBlock materialWoodBlock;
	public BlockMaterialStairs materialWoodStairs;
	public BlockMaterialSlab materialWoodSlab;
	public BlockMaterialStep materialWoodStep;
	public BlockMaterialCorner materialWoodCorner;
	public BlockMaterialWall materialRockWall;

	private MaterialBlockRegistry() {
		materialRockBlock = createBlock(BlockMaterialBlock.class, StructureMaterial.ROCK,
				MaterialBlockType.FULL);
		materialRockSlab = createBlock(BlockMaterialSlab.class, StructureMaterial.ROCK,
				MaterialBlockType.SLAB);
		materialRockStep = createBlock(BlockMaterialStep.class, StructureMaterial.ROCK,
				MaterialBlockType.STEP);
		materialRockCorner = createBlock(BlockMaterialCorner.class, StructureMaterial.ROCK,
				MaterialBlockType.CORNER);
		materialRockWall = createBlock(BlockMaterialWall.class, StructureMaterial.ROCK,
				MaterialBlockType.WALL);

		materialRockStairs = new BlockMaterialStairs(materialRockBlock.getDefaultState());
		materialRockStairs.setUnlocalizedName("material_rock_stairs");
		blocks.put(MaterialBlockType.STAIRS, new HashMap<StructureMaterial, Block>());
		blocks.get(MaterialBlockType.STAIRS).put(StructureMaterial.ROCK, materialRockStairs);
		blocksMaterials.put(MaterialBlockType.STAIRS,
				new HashMap<StructureMaterial, Set<Material>>());
		blocksMaterials.get(MaterialBlockType.STAIRS).put(StructureMaterial.ROCK,
				new HashSet<Material>());

		materialWoodBlock = createBlock(BlockMaterialBlock.class, StructureMaterial.WOOD,
				MaterialBlockType.FULL);
		materialWoodSlab = createBlock(BlockMaterialSlab.class, StructureMaterial.WOOD,
				MaterialBlockType.SLAB);
		materialWoodStep = createBlock(BlockMaterialStep.class, StructureMaterial.WOOD,
				MaterialBlockType.STEP);
		materialWoodCorner = createBlock(BlockMaterialCorner.class, StructureMaterial.WOOD,
				MaterialBlockType.CORNER);

		materialWoodStairs = new BlockMaterialStairs(materialWoodBlock.getDefaultState());
		materialWoodStairs.setUnlocalizedName("material_wood_stairs");
		blocks.get(MaterialBlockType.STAIRS).put(StructureMaterial.ROCK, materialWoodStairs);
		blocksMaterials.get(MaterialBlockType.STAIRS).put(StructureMaterial.ROCK,
				new HashSet<Material>());

	}

	private <T extends Block> T createBlock(Class<T> cls, StructureMaterial strMat,
			MaterialBlockType blockType) {
		T block;
		try {
			block = cls.getConstructor(StructureMaterial.class).newInstance(strMat);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		block.setUnlocalizedName("material_" + strMat.getName() + "_" + blockType.getName());
		if (!blocks.containsKey(blockType))
			blocks.put(blockType, new HashMap<StructureMaterial, Block>());
		blocks.get(blockType).put(strMat, block);
		if (!blocksMaterials.containsKey(blockType))
			blocksMaterials.put(blockType, new HashMap<StructureMaterial, Set<Material>>());
		blocksMaterials.get(blockType).put(strMat, new HashSet<Material>());
		return block;
	}

	public void init() {
		for (Entry<MaterialBlockType, HashMap<StructureMaterial, Block>> entry : blocks
				.entrySet()) {
			for (Entry<StructureMaterial, Block> blockEntry : entry.getValue().entrySet()) {
				GameRegistry.registerBlock(blockEntry.getValue(), "material_"
						+ blockEntry.getKey().getName() + "_" + entry.getKey().getName());
			}
		}
	}

	public Collection<Block> getAllBlocks() {
		List<Block> allBlocks = new ArrayList<Block>();
		for (HashMap<StructureMaterial, Block> entry : blocks.values()) {
			allBlocks.addAll(entry.values());
		}
		return allBlocks;
	}

	public HashMap<MaterialBlockType, HashMap<StructureMaterial, Block>> getBlocks() {
		return blocks;
	}

	public Collection<Block> getBlocks(MaterialBlockType blockType) {
		return blocks.get(blockType).values();
	}

	public HashMap<MaterialBlockType, HashMap<StructureMaterial, Set<Material>>> getBlocksMaterials() {
		return blocksMaterials;
	}

	public BlockDescription addBlock(MaterialBlockType blockType, Material mat) {
		blocksMaterials.get(blockType).get(mat.getStructureMaterial()).add(mat);
		Block block = blocks.get(blockType).get(mat.getStructureMaterial());
		return new BlockDescription(blockType, block, 0, "material",
				new NBTTagString(mat.materialId()));
	}

}
