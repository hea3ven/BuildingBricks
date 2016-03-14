package com.hea3ven.buildingbricks.core.materials;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Table;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagString;

import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.hea3ven.buildingbricks.core.ProxyCommonBuildingBricks;
import com.hea3ven.buildingbricks.core.blocks.*;
import com.hea3ven.buildingbricks.core.items.ItemMaterialBlock;
import com.hea3ven.tools.commonutils.client.renderer.SimpleItemMeshDefinition;
import com.hea3ven.tools.commonutils.util.SidedCall;
import com.hea3ven.tools.commonutils.util.SidedCall.SidedRunnable;

public class MaterialBlockRegistry {

	public static MaterialBlockRegistry instance = new MaterialBlockRegistry();

	private static final Logger logger = LogManager.getLogger("BuildingBricks.MaterialBlockRegistry");

	public boolean enableGenerateBlocks = true;

	private Table<MaterialBlockType, StructureMaterial, Block> blocks = HashBasedTable.create();
	private HashMap<Block, Set<Material>> blocksMaterials = new HashMap<>();

	private MaterialBlockRegistry() {
	}

	public void generateBlocks(ProxyCommonBuildingBricks proxy) {
		for (Material mat : MaterialRegistry.getAll()) {
			for (BlockDescription blockDesc : mat.getBlockRotation().getAll().values()) {
				if (blockDesc.isBlockTemplate()) {
					if (enableGenerateBlocks)
						initBlockDesc(proxy, mat, blockDesc);
					else
						mat.removeBlock(blockDesc);
				}
			}
		}
	}

	private void initBlockDesc(ProxyCommonBuildingBricks proxy, Material mat, BlockDescription blockDesc) {
		if (!blocks.contains(blockDesc.getType(), mat.getStructureMaterial()))
			initBlock(proxy, blockDesc.getType(), mat);

		Block block = blocks.get(blockDesc.getType(), mat.getStructureMaterial());
		blocksMaterials.get(block).add(mat);
		blockDesc.setBlock(block, 0,
				ImmutableMap.<String, NBTBase>builder()
						.put("material", new NBTTagString(mat.getMaterialId()))
						.build());
	}

	private void initBlock(ProxyCommonBuildingBricks proxy, MaterialBlockType blockType, Material mat) {
		Class<? extends Block> cls;
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
			case FENCE:
				cls = BlockMaterialFence.class;
				break;
			case FENCE_GATE:
				cls = BlockMaterialFenceGate.class;
				break;
			case STAIRS:
				cls = BlockMaterialStairs.class;
				break;
			case PANE:
				cls = BlockMaterialPane.class;
				break;
		}

		createBlock(proxy, cls, mat.getStructureMaterial(), blockType);
	}

	private void createBlock(ProxyCommonBuildingBricks proxy, Class<? extends Block> cls,
			StructureMaterial structMat, MaterialBlockType blockType) {
		final Block block;
		try {
			block = cls.getConstructor(StructureMaterial.class).newInstance(structMat);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		block.setUnlocalizedName(structMat.getName() + "_" + blockType.getName())
				.setCreativeTab(proxy.getCreativeTab("buildingBricks"));

		blocks.put(blockType, structMat, block);
		if (!blocksMaterials.containsKey(block))
			blocksMaterials.put(block, new HashSet<Material>());

		Class<? extends ItemBlock> itemCls = ItemMaterialBlock.class;

		final String blockName = structMat.getName() + "_" + blockType.getName();
		GameRegistry.registerBlock(block, itemCls, blockName);
		SidedCall.run(Side.CLIENT, new SidedRunnable() {
			@Override
			@SideOnly(Side.CLIENT)
			public void run() {
				ModelLoader.setCustomMeshDefinition(Item.getItemFromBlock(block),
						new SimpleItemMeshDefinition("buildingbricks:" + blockName + "#inventory"));
			}
		});
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
