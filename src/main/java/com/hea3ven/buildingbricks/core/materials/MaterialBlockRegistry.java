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

import com.hea3ven.buildingbricks.core.blocks.BlockBuildingBricksBase;
import com.hea3ven.buildingbricks.core.blocks.BlockMaterialCorner;
import com.hea3ven.buildingbricks.core.blocks.BlockMaterialSlab;
import com.hea3ven.buildingbricks.core.blocks.BlockMaterialStairs;
import com.hea3ven.buildingbricks.core.blocks.BlockMaterialStairsFixedCorner;
import com.hea3ven.buildingbricks.core.blocks.BlockMaterialStairsFixedInnerCorner;
import com.hea3ven.buildingbricks.core.blocks.BlockMaterialStairsFixedSide;
import com.hea3ven.buildingbricks.core.blocks.BlockMaterialStep;
import com.hea3ven.buildingbricks.core.blocks.BlockMaterialWall;
import com.hea3ven.buildingbricks.core.lib.BlockDescription;

public class MaterialBlockRegistry {

	public static MaterialBlockRegistry instance = new MaterialBlockRegistry();

	private HashMap<MaterialBlockType, HashMap<StructureMaterial, Block>> blocks = new HashMap<MaterialBlockType, HashMap<StructureMaterial, Block>>();
	private HashMap<MaterialBlockType, HashMap<StructureMaterial, Set<Material>>> blocksMaterials = new HashMap<MaterialBlockType, HashMap<StructureMaterial, Set<Material>>>();

	public BlockMaterialStairs materialRockStairs;
	public BlockMaterialStairsFixedInnerCorner materialRockStairsInnerCorner;
	public BlockMaterialStairsFixedSide materialRockStairsSide;
	public BlockMaterialStairsFixedCorner materialRockStairsCorner;
	public BlockMaterialSlab materialRockSlab;
	public BlockMaterialStep materialRockStep;
	public BlockMaterialCorner materialRockCorner;
	public BlockMaterialStairs materialWoodStairs;
	public BlockMaterialStairsFixedInnerCorner materialWoodStairsInnerCorner;
	public BlockMaterialStairsFixedSide materialWoodStairsSide;
	public BlockMaterialStairsFixedCorner materialWoodStairsCorner;
	public BlockMaterialSlab materialWoodSlab;
	public BlockMaterialStep materialWoodStep;
	public BlockMaterialCorner materialWoodCorner;
	public BlockMaterialWall materialRockWall;

	private MaterialBlockRegistry() {
		materialRockStairsInnerCorner = createBlock(BlockMaterialStairsFixedInnerCorner.class,
				StructureMaterial.ROCK, MaterialBlockType.STAIRS_FIXED_INNER_CORNER);
		materialRockStairsSide = createBlock(BlockMaterialStairsFixedSide.class,
				StructureMaterial.ROCK, MaterialBlockType.STAIRS_FIXED_SIDE);
		materialRockStairsCorner = createBlock(BlockMaterialStairsFixedCorner.class,
				StructureMaterial.ROCK, MaterialBlockType.STAIRS_FIXED_CORNER);
		materialRockSlab = createBlock(BlockMaterialSlab.class, StructureMaterial.ROCK,
				MaterialBlockType.SLAB);
		materialRockStep = createBlock(BlockMaterialStep.class, StructureMaterial.ROCK,
				MaterialBlockType.STEP);
		materialRockCorner = createBlock(BlockMaterialCorner.class, StructureMaterial.ROCK,
				MaterialBlockType.CORNER);
		materialRockWall = createBlock(BlockMaterialWall.class, StructureMaterial.ROCK,
				MaterialBlockType.WALL);

		materialRockStairs = new BlockMaterialStairs(materialRockSlab.getDefaultState());
		materialRockStairs.setUnlocalizedName("material_rock_stairs");
		blocks.put(MaterialBlockType.STAIRS, new HashMap<StructureMaterial, Block>());
		blocks.get(MaterialBlockType.STAIRS).put(StructureMaterial.ROCK, materialRockStairs);
		blocksMaterials.put(MaterialBlockType.STAIRS, new HashMap<StructureMaterial, Set<Material>>());
		blocksMaterials.get(MaterialBlockType.STAIRS).put(StructureMaterial.ROCK, new HashSet<Material>());
		
		materialWoodStairsInnerCorner = createBlock(BlockMaterialStairsFixedInnerCorner.class,
				StructureMaterial.WOOD, MaterialBlockType.STAIRS_FIXED_INNER_CORNER);
		materialWoodStairsSide = createBlock(BlockMaterialStairsFixedSide.class,
				StructureMaterial.WOOD, MaterialBlockType.STAIRS_FIXED_SIDE);
		materialWoodStairsCorner = createBlock(BlockMaterialStairsFixedCorner.class,
				StructureMaterial.WOOD, MaterialBlockType.STAIRS_FIXED_CORNER);
		materialWoodSlab = createBlock(BlockMaterialSlab.class, StructureMaterial.WOOD,
				MaterialBlockType.SLAB);
		materialWoodStep = createBlock(BlockMaterialStep.class, StructureMaterial.WOOD,
				MaterialBlockType.STEP);
		materialWoodCorner = createBlock(BlockMaterialCorner.class, StructureMaterial.WOOD,
				MaterialBlockType.CORNER);
	}

	private <T extends BlockBuildingBricksBase> T createBlock(Class<T> cls,
			StructureMaterial strMat, MaterialBlockType blockType) {
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
