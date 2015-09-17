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
import net.minecraft.item.ItemBlock;
import net.minecraft.nbt.NBTTagString;

import net.minecraftforge.fml.common.registry.GameRegistry;

import com.hea3ven.buildingbricks.core.blocks.BlockMaterialBlock;
import com.hea3ven.buildingbricks.core.blocks.BlockMaterialCorner;
import com.hea3ven.buildingbricks.core.blocks.BlockMaterialSlab;
import com.hea3ven.buildingbricks.core.blocks.BlockMaterialStairs;
import com.hea3ven.buildingbricks.core.blocks.BlockMaterialStep;
import com.hea3ven.buildingbricks.core.blocks.BlockMaterialVerticalSlab;
import com.hea3ven.buildingbricks.core.blocks.BlockMaterialWall;
import com.hea3ven.buildingbricks.core.items.ItemColoredWrapper;
import com.hea3ven.buildingbricks.core.items.ItemMaterialBlock;
import com.hea3ven.buildingbricks.core.items.creativetab.CreativeTabBuildingBricks;

public class MaterialBlockRegistry {

	public static MaterialBlockRegistry instance = new MaterialBlockRegistry();

	private static final Logger logger = LogManager
			.getLogger("BuildingBricks.MaterialBlockRegistry");

	private Table<MaterialBlockType, StructureMaterial, Block> blocks = HashBasedTable.create();
	private HashMap<Block, Set<Material>> blocksMaterials = new HashMap<Block, Set<Material>>();

	private MaterialBlockRegistry() {
	}

	public BlockDescription addBlock(MaterialBlockType blockType, Material mat) {
		if (!blocks.contains(blockType, mat.getStructureMaterial()))
			initBlock(blockType, mat);

		Block block = blocks.get(blockType, mat.getStructureMaterial());
		blocksMaterials.get(block).add(mat);
		return new BlockDescription(blockType, block, 0, "material",
				new NBTTagString(mat.materialId()));
	}

	private void initBlock(MaterialBlockType blockType, Material mat) {
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

		createBlock(cls, mat.getStructureMaterial(), blockType);
	}

	private <T extends Block> T createBlock(Class<T> cls, StructureMaterial structMat,
			MaterialBlockType blockType) {
		T block;
		try {
			block = cls.getConstructor(StructureMaterial.class).newInstance(structMat);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		block.setUnlocalizedName(structMat.getName() + "_" + blockType.getName());
		block.setCreativeTab(CreativeTabBuildingBricks.get());

		blocks.put(blockType, structMat, block);
		if (!blocksMaterials.containsKey(block))
			blocksMaterials.put(block, new HashSet<Material>());

		Class<? extends ItemBlock> itemCls = !structMat.getColor() ? ItemMaterialBlock.class
				: ItemColoredWrapper.class;
		GameRegistry.registerBlock(block, itemCls, structMat.getName() + "_" + blockType.getName());
		return block;
	}

	public Collection<Block> getAllBlocks() {
		return blocks.values();
	}

	public Table<MaterialBlockType, StructureMaterial, Block> getBlocks() {
		return blocks;
	}

	public Collection<Block> getBlocks(MaterialBlockType blockType) {
		return blocks.row(blockType).values();
	}

	public void logStats() {
		logger.info("Created {} blocks", blocks.size());
	}

	public Set<Material> getBlockMaterials(Block block) {
		return blocksMaterials.get(block);
	}
}
