package com.hea3ven.buildingbricks.core.materials;

import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.item.ItemStack;

import com.hea3ven.buildingbricks.core.ModBuildingBricks;

public class MaterialRegistry {

	private static final Logger logger = LogManager.getLogger("BuildingBricks.MaterialRegistry");

	private static Set<Material> materials;
	private static boolean isFrozen = false;

	static {
		materials = new HashSet<>();
	}

	private static Map<String, Material> materialsById = new HashMap<>();

	private static int nextMeta = 1;
	private static Map<Material, Integer> materialsMeta = new HashMap<>();

	public static void registerMaterial(Material material) {
		if (isFrozen)
			throw new RuntimeException(
					String.format("Trying to register the %s material when the reigstry is frozen",
							material.getMaterialId()));
		materials.add(material);
		materialsById.put(material.getMaterialId(), material);
		materialsMeta.put(material, nextMeta++);
	}

	public static Set<Material> getAll() {
		return materials;
	}

	public static Material get(String materialId) {
		return materialsById.get(materialId);
	}

	public static Material getMaterialForStack(ItemStack stack) {
		Material matStack = MaterialStack.get(stack);
		if (matStack != null)
			return matStack;

		// TODO: optimize
		for (Material mat : materials) {
			for (BlockDescription blockDesc : mat.getBlockRotation().getAll().values()) {
				if (ItemStack.areItemsEqual(stack, blockDesc.getStack()) &&
						ItemStack.areItemStackTagsEqual(stack, blockDesc.getStack())) {
					return mat;
				}
			}
		}
		return null;
	}

	/**
	 * Get a numeric id for the material, WARNING, these are not synced between the client and the server.
	 *
	 * @param mat the material
	 * @return the id
	 */
	public static int getMeta(Material mat) {
		return mat != null ? materialsMeta.get(mat) : 0;
	}

	public static void logStats() {
		logger.info("Registered {} material(s)", materials.size());
	}

	public static void freeze() {
		if (isFrozen)
			return;

		isFrozen = true;
		for (Material mat : new ArrayList<>(materials)) {
			boolean hasRealBlocks = false;
			boolean hasExistingRealBlocks = false;
			for (BlockDescription blockDesc : new ArrayList<>(mat.getBlockRotation().getAll().values())) {
				if (blockDesc.getBlock() == null) {
					hasRealBlocks = true;
					mat.removeBlock(blockDesc);
					if (MaterialBlockRegistry.instance.enableGenerateBlocks &&
							MaterialBlockRegistry.instance.enabledBlocks.get(blockDesc.getType())) {
						mat.addBlock(new BlockDescription(blockDesc.getType(),
								MaterialBlockRecipes.getForType(mat.getStructureMaterial(),
										blockDesc.getType())));
						MaterialBlockRegistry.instance.initBlockDescTmp(ModBuildingBricks.proxy, mat,
								mat.getBlock(blockDesc.getType()));
					}
				} else if (!MaterialBlockRegistry.instance.getAllBlocks().contains(blockDesc.getBlock())) {
					hasRealBlocks = true;
					hasExistingRealBlocks = true;
				}
			}
			if (!hasExistingRealBlocks && hasRealBlocks) {
				materials.remove(mat);
				MaterialBlockRegistry.instance.removeMaterial(mat);
			}
		}
	}
}
