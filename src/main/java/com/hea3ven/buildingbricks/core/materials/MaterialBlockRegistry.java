package com.hea3ven.buildingbricks.core.materials;

import java.util.*;
import java.util.Map.Entry;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Table;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagString;

import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.hea3ven.buildingbricks.core.ModBuildingBricks;
import com.hea3ven.buildingbricks.core.ProxyCommonBuildingBricks;
import com.hea3ven.buildingbricks.core.block.*;
import com.hea3ven.buildingbricks.core.item.ItemMaterialBlock;
import com.hea3ven.tools.commonutils.client.renderer.SimpleItemMeshDefinition;
import com.hea3ven.tools.commonutils.util.SidedCall;
import com.hea3ven.tools.commonutils.util.SidedCall.SidedRunnable;

public class MaterialBlockRegistry {

	public static MaterialBlockRegistry instance = new MaterialBlockRegistry();

	private static final Logger logger = LogManager.getLogger("BuildingBricks.MaterialBlockRegistry");

	public boolean enableGenerateBlocks = true;
	public Map<MaterialBlockType, Boolean> enabledBlocks = new HashMap<>();

	private Table<MaterialBlockType, StructureMaterial, Block> blocks = HashBasedTable.create();
	private HashMap<Block, Set<Material>> blocksMaterials = new HashMap<>();

	private MaterialBlockRegistry() {
	}

	public void generateBlocks(ProxyCommonBuildingBricks proxy) {
		for (Material mat : MaterialRegistry.getAll()) {
			for (BlockDescription blockDesc : new ArrayList<>(mat.getBlockRotation().getAll().values())) {
				if (blockDesc.isBlockTemplate()) {
					if (enableGenerateBlocks && enabledBlocks.get(blockDesc.getType()))
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

	void initBlockDescTmp(ProxyCommonBuildingBricks proxy, Material mat, BlockDescription blockDesc) {
		initBlockDesc(proxy, mat, blockDesc);
	}

	private void initBlock(ProxyCommonBuildingBricks proxy, MaterialBlockType blockType, Material mat) {
		StructureMaterial structMat = mat.getStructureMaterial();
		final Block block = createBlock(blockType, structMat);

		final String blockName = structMat.getName() + "_" + blockType.getName();
		block.setRegistryName(ModBuildingBricks.MODID, blockName)
				.setUnlocalizedName(ModBuildingBricks.MODID + "." + blockName)
				.setCreativeTab(proxy.getCreativeTab("buildingBricks"));

		blocks.put(blockType, structMat, block);
		if (!blocksMaterials.containsKey(block))
			blocksMaterials.put(block, new HashSet<>());

		GameRegistry.register(block);
		if (blockType.isStackType()) {
			final Item item =
					new ItemMaterialBlock(block).setRegistryName(ModBuildingBricks.MODID, blockName);
			GameRegistry.register(item);
			SidedCall.run(Side.CLIENT, new SidedRunnable() {
				@Override
				@SideOnly(Side.CLIENT)
				public void run() {
					ModelLoader.setCustomMeshDefinition(item,
							new SimpleItemMeshDefinition("buildingbricks:" + blockName));
				}
			});
		}
	}

	private Block createBlock(MaterialBlockType blockType, StructureMaterial structMat) {
		switch (blockType) {
			default:
			case FULL:
				return new BlockMaterialBlock(structMat);
			case SLAB:
				return new BlockMaterialSlab(structMat);
			case VERTICAL_SLAB:
				return new BlockMaterialVerticalSlab(structMat);
			case STEP:
				return new BlockMaterialStep(structMat);
			case CORNER:
				return new BlockMaterialCorner(structMat);
			case WALL:
				return new BlockMaterialWall(structMat);
			case FENCE:
				return new BlockMaterialFence(structMat);
			case FENCE_GATE:
				return new BlockMaterialFenceGate(structMat);
			case STAIRS:
				return new BlockMaterialStairs(structMat);
			case PANE:
				return new BlockMaterialPane(structMat);
		}
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

	void removeMaterial(Material mat) {
		for (Entry<Block, Set<Material>> entry : blocksMaterials.entrySet()) {
			if (entry.getValue().contains(mat)) {
				entry.getValue().remove(mat);
			}
		}
	}
}
