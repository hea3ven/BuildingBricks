package com.hea3ven.buildingbricks.core.materials;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MaterialRegistry {
	
	private static int nextGlobalId = 1;
	private static Set<Material> materials = new HashSet<Material>();
	private static Map<Integer, Material> materialsById = new HashMap<Integer, Material>();

	public static void registerMaterial(Material material) {
		materials.add(material);
		material.globalId = nextGlobalId++;
		materialsById.put(material.globalId, material);
	}

	public static Set<Material> getAll() {
		return materials;
	}

	public static Material get(int materialId) {
		return materialsById.get(materialId);
	}

}
