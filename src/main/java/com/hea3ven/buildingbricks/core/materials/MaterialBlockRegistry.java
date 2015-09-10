package com.hea3ven.buildingbricks.core.materials;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.nbt.NBTTagString;

import net.minecraftforge.fml.common.registry.GameRegistry;

import com.hea3ven.buildingbricks.core.blocks.BlockBuildingBricksBlock;
import com.hea3ven.buildingbricks.core.blocks.BlockBuildingBricksCorner;
import com.hea3ven.buildingbricks.core.blocks.BlockBuildingBricksSlab;
import com.hea3ven.buildingbricks.core.blocks.BlockBuildingBricksStairs;
import com.hea3ven.buildingbricks.core.blocks.BlockBuildingBricksStep;
import com.hea3ven.buildingbricks.core.blocks.BlockBuildingBricksVerticalSlab;
import com.hea3ven.buildingbricks.core.blocks.BlockBuildingBricksWall;
import com.hea3ven.buildingbricks.core.items.ItemColoredWrapper;
import com.hea3ven.buildingbricks.core.items.ItemMaterialBlock;
import com.hea3ven.buildingbricks.core.lib.BlockDescription;

public class MaterialBlockRegistry {

	public static MaterialBlockRegistry instance = new MaterialBlockRegistry();

	private static final Logger logger = LogManager
			.getLogger("BuildingBricks.MaterialBlockRegistry");

	private Table<MaterialBlockType, Material, Block> blocks = HashBasedTable.create();
	private HashMap<MaterialBlockType, Set<Material>> blocksMaterials = new HashMap<MaterialBlockType, Set<Material>>();

	private MaterialBlockRegistry() {
	}

	public BlockDescription addBlock(MaterialBlockType blockType, Material mat) {
		if (!blocks.contains(blockType, mat))
			initBlock(blockType, mat);

		blocksMaterials.get(blockType).add(mat);
		Block block = blocks.get(blockType, mat);
		return new BlockDescription(blockType, block);
	}

	private void initBlock(MaterialBlockType blockType, Material mat) {
		Class<? extends Block> cls = null;
		switch (blockType) {
		default:
		case FULL:
			cls = BlockBuildingBricksBlock.class;
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

		createBlock(cls, mat, blockType);
	}

	private <T extends Block> T createBlock(Class<T> cls, Material mat,
			MaterialBlockType blockType) {
		T block;
		try {
			block = cls.getConstructor(Material.class).newInstance(mat);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		block.setUnlocalizedName(mat.materialId() + "_" + blockType.getName());
		blocks.put(blockType, mat, block);
		if (!blocksMaterials.containsKey(blockType))
			blocksMaterials.put(blockType, new HashSet<Material>());

		block.setCreativeTab(CreativeTabs.tabBlock);
		Class<? extends ItemBlock> itemCls = !mat.getStructureMaterial().getColor()
				? ItemMaterialBlock.class : ItemColoredWrapper.class;
		GameRegistry.registerBlock(block, itemCls, mat.materialId() + "_" + blockType.getName());
		return block;
	}

	public Collection<Block> getAllBlocks() {
		return blocks.values();
	}

	public Table<MaterialBlockType, Material, Block> getBlocks() {
		return blocks;
	}

	public Collection<Block> getBlocks(MaterialBlockType blockType) {
		return blocks.row(blockType).values();
	}

	public void logStats() {
		logger.info("Created {} blocks", blocks.size());
	}
}
