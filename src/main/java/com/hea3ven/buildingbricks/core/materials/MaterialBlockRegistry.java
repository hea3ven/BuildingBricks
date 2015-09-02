package com.hea3ven.buildingbricks.core.materials;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagString;

import net.minecraftforge.fml.common.registry.GameRegistry;

import com.hea3ven.buildingbricks.core.blocks.BlockMaterialBlock;
import com.hea3ven.buildingbricks.core.blocks.BlockMaterialCorner;
import com.hea3ven.buildingbricks.core.blocks.BlockMaterialSlab;
import com.hea3ven.buildingbricks.core.blocks.BlockMaterialStairs;
import com.hea3ven.buildingbricks.core.blocks.BlockMaterialStep;
import com.hea3ven.buildingbricks.core.blocks.BlockMaterialVerticalSlab;
import com.hea3ven.buildingbricks.core.blocks.BlockMaterialWall;
import com.hea3ven.buildingbricks.core.lib.BlockDescription;

public class MaterialBlockRegistry {

	public static MaterialBlockRegistry instance = new MaterialBlockRegistry();

	private HashMap<MaterialBlockType, HashMap<StructureMaterial, Block>> blocks = new HashMap<MaterialBlockType, HashMap<StructureMaterial, Block>>();
	private HashMap<MaterialBlockType, HashMap<StructureMaterial, Set<Material>>> blocksMaterials = new HashMap<MaterialBlockType, HashMap<StructureMaterial, Set<Material>>>();

	private MaterialBlockRegistry() {
	}

	public BlockDescription addBlock(MaterialBlockType blockType, Material mat) {
		if (!blocks.containsKey(blockType)
				|| !blocks.get(blockType).containsKey(mat.getStructureMaterial()))
			initBlock(blockType, mat.getStructureMaterial());

		blocksMaterials.get(blockType).get(mat.getStructureMaterial()).add(mat);
		Block block = blocks.get(blockType).get(mat.getStructureMaterial());
		return new BlockDescription(blockType, block, 0, "material",
				new NBTTagString(mat.materialId()));
	}

	private void initBlock(MaterialBlockType blockType, StructureMaterial strMat) {
		Class<? extends Block> cls = null;
		switch (blockType) {
		default:
		case FULL:
			cls = BlockMaterialBlock.class;
			break;
		case SLAB:
			cls = BlockMaterialSlab.class;
			break;
		case VERTICAL_SLAB:
			cls = BlockMaterialVerticalSlab.class;
			break;
		case STEP:
			cls = BlockMaterialStep.class;
			break;
		case CORNER:
			cls = BlockMaterialCorner.class;
			break;
		case WALL:
			cls = BlockMaterialWall.class;
			break;
		case STAIRS:
			cls = BlockMaterialStairs.class;
			break;
		}

		createBlock(cls, strMat, blockType);
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

		GameRegistry.registerBlock(block,
				"material_" + strMat.getName() + "_" + blockType.getName());
		return block;
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
}
