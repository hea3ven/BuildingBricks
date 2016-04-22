package com.hea3ven.buildingbricks.core.materials;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.item.ItemStack;

public class MaterialRegistry {

	private static final Logger logger = LogManager.getLogger("BuildingBricks.MaterialRegistry");

	private static Set<Material> materials;

	static {
		materials = new HashSet<>();
	}

	private static Map<String, Material> materialsById = new HashMap<>();

	public static void registerMaterial(Material material) {
		materials.add(material);
		materialsById.put(material.getMaterialId(), material);
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

	public static void logStats() {
		logger.info("Registered {} material(s)", materials.size());
	}
}
