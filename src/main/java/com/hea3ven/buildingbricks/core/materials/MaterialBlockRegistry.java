package com.hea3ven.buildingbricks.core.materials;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.nbt.NBTTagString;

import net.minecraftforge.fml.common.registry.GameRegistry;

import com.hea3ven.buildingbricks.core.blocks.BlockBuildingBricksBase;
import com.hea3ven.buildingbricks.core.blocks.BlockMaterialCorner;
import com.hea3ven.buildingbricks.core.blocks.BlockMaterialSlab;
import com.hea3ven.buildingbricks.core.blocks.BlockMaterialStep;
import com.hea3ven.buildingbricks.core.lib.BlockDescription;

public class MaterialBlockRegistry {

	public static MaterialBlockRegistry instance = new MaterialBlockRegistry();

	private HashMap<MaterialBlockType, HashMap<StructureMaterial, BlockBuildingBricksBase>> blocks = new HashMap<MaterialBlockType, HashMap<StructureMaterial, BlockBuildingBricksBase>>();
	private HashMap<MaterialBlockType, HashMap<StructureMaterial, Set<Material>>> blocksMaterials = new HashMap<MaterialBlockType, HashMap<StructureMaterial, Set<Material>>>();

	public BlockMaterialSlab materialRockSlab;
	public BlockMaterialStep materialRockStep;
	public BlockMaterialCorner materialRockCorner;
	public BlockMaterialSlab materialWoodSlab;
	public BlockMaterialStep materialWoodStep;
	public BlockMaterialCorner materialWoodCorner;

	private MaterialBlockRegistry() {
		materialRockSlab = createBlock(BlockMaterialSlab.class, StructureMaterial.ROCK,
				MaterialBlockType.SLAB);
		materialRockStep = createBlock(BlockMaterialStep.class, StructureMaterial.ROCK,
				MaterialBlockType.STEP);
		materialRockCorner = createBlock(BlockMaterialCorner.class, StructureMaterial.ROCK,
				MaterialBlockType.CORNER);
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
			block = cls.getConstructor(StructureMaterial.class, String.class).newInstance(strMat,
					"material_" + strMat.getName() + "_" + blockType.getName());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		if (!blocks.containsKey(blockType))
			blocks.put(blockType, new HashMap<StructureMaterial, BlockBuildingBricksBase>());
		blocks.get(blockType).put(strMat, block);
		if (!blocksMaterials.containsKey(blockType))
			blocksMaterials.put(blockType, new HashMap<StructureMaterial, Set<Material>>());
		blocksMaterials.get(blockType).put(strMat, new HashSet<Material>());
		return block;
	}

	public void init() {
		for (HashMap<StructureMaterial, BlockBuildingBricksBase> blockList : blocks.values()) {
			for (BlockBuildingBricksBase block : blockList.values()) {
				GameRegistry.registerBlock(block, block.getName());
			}
		}
	}

	public Collection<BlockBuildingBricksBase> getAllBlocks() {
		List<BlockBuildingBricksBase> allBlocks = new ArrayList<BlockBuildingBricksBase>();
		for (HashMap<StructureMaterial, BlockBuildingBricksBase> entry : blocks.values()) {
			allBlocks.addAll(entry.values());
		}
		return allBlocks;
	}

	public HashMap<MaterialBlockType, HashMap<StructureMaterial, BlockBuildingBricksBase>> getBlocks() {
		return blocks;
	}

	public Collection<BlockBuildingBricksBase> getBlocks(MaterialBlockType blockType) {
		return blocks.get(blockType).values();
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
