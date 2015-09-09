package com.hea3ven.buildingbricks.core.materials;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.item.ItemStack;

import com.hea3ven.buildingbricks.core.lib.BlockDescription;

public class MaterialRegistry {

	private static final Logger logger = LogManager.getLogger("BuildingBricks.MaterialRegistry");

	private static Set<Material> materials = new HashSet<Material>();
	private static Map<String, Material> materialsById = new HashMap<String, Material>();

	public static void registerMaterial(Material material) {
		materials.add(material);
		materialsById.put(material.materialId(), material);
	}

	public static Set<Material> getAll() {
		return materials;
	}

	public static Material get(String materialId) {
		return materialsById.get(materialId);
	}

	public static Material getMaterialForStack(ItemStack stack) {
		for (Material mat : materials) {
			for (BlockDescription blockDesc : mat.getBlockRotation().getAll().values()) {
				if (ItemStack.areItemsEqual(stack, blockDesc.getStack())
						&& ItemStack.areItemStackTagsEqual(stack, blockDesc.getStack())) {
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
