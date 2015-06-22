package com.hea3ven.buildingbricks.core.materials;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MaterialRegistry {
	
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

}
