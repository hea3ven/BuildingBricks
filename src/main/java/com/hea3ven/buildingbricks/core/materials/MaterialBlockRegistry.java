package com.hea3ven.buildingbricks.core.materials;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.nbt.NBTTagString;

import net.minecraftforge.fml.common.registry.GameRegistry;

import com.hea3ven.buildingbricks.core.blocks.BlockBuildingBricksBase;
import com.hea3ven.buildingbricks.core.blocks.BlockBuildingBricksCorner;
import com.hea3ven.buildingbricks.core.blocks.BlockBuildingBricksSlab;
import com.hea3ven.buildingbricks.core.blocks.BlockBuildingBricksStairs;
import com.hea3ven.buildingbricks.core.blocks.BlockBuildingBricksStep;
import com.hea3ven.buildingbricks.core.blocks.BlockBuildingBricksVerticalSlab;
import com.hea3ven.buildingbricks.core.blocks.BlockBuildingBricksWall;
import com.hea3ven.buildingbricks.core.lib.BlockDescription;

public class MaterialBlockRegistry {

	public static MaterialBlockRegistry instance = new MaterialBlockRegistry();

	private Map<MaterialBlockType, Map<Material, Block>> blocks = new HashMap<MaterialBlockType, Map<Material, Block>>();
	private HashMap<MaterialBlockType, Set<Material>> blocksMaterials = new HashMap<MaterialBlockType, Set<Material>>();

	private MaterialBlockRegistry() {
	}

	public BlockDescription addBlock(MaterialBlockType blockType, Material mat) {
		if (!blocks.containsKey(blockType) || !blocks.get(blockType).containsKey(mat))
			initBlock(blockType, mat);

		blocksMaterials.get(blockType).add(mat);
		Block block = blocks.get(blockType).get(mat);
		return new BlockDescription(blockType, block, 0, "material",
				new NBTTagString(mat.materialId()));
	}

	private void initBlock(MaterialBlockType blockType, Material strMat) {
		Class<? extends Block> cls = null;
		switch (blockType) {
		default:
		case FULL:
			cls = BlockBuildingBricksBase.class;
			break;
		case SLAB:
			cls = BlockBuildingBricksSlab.class;
			break;
		case VERTICAL_SLAB:
			cls = BlockBuildingBricksVerticalSlab.class;
			break;
		case STEP:
			cls = BlockBuildingBricksStep.class;
			break;
		case CORNER:
			cls = BlockBuildingBricksCorner.class;
			break;
		case WALL:
			cls = BlockBuildingBricksWall.class;
			break;
		case STAIRS:
			cls = BlockBuildingBricksStairs.class;
			break;
		}

		createBlock(cls, strMat, blockType);
	}

	private <T extends Block> T createBlock(Class<T> cls, Material strMat,
			MaterialBlockType blockType) {
		T block;
		try {
			block = cls.getConstructor(Material.class).newInstance(strMat);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		block.setUnlocalizedName(strMat.materialId() + "_" + blockType.getName());
		if (!blocks.containsKey(blockType))
			blocks.put(blockType, new HashMap<Material, Block>());
		blocks.get(blockType).put(strMat, block);
		if (!blocksMaterials.containsKey(blockType))
			blocksMaterials.put(blockType, new HashSet<Material>());

		block.setCreativeTab(CreativeTabs.tabBlock);
		GameRegistry.registerBlock(block, strMat.materialId() + "_" + blockType.getName());
		return block;
	}

	public Collection<Block> getAllBlocks() {
		List<Block> allBlocks = new ArrayList<Block>();
		for (Map<Material, Block> entry : blocks.values()) {
			allBlocks.addAll(entry.values());
		}
		return allBlocks;
	}

	public Map<MaterialBlockType, Map<Material, Block>> getBlocks() {
		return blocks;
	}

	public Collection<Block> getBlocks(MaterialBlockType blockType) {
		return blocks.get(blockType).values();
	}
}
