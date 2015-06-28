package com.hea3ven.buildingbricks.core.materials;

import net.minecraft.block.material.Material;

public enum StructureMaterial {
	ROCK(Material.rock);

	private Material mcMaterial;

	private StructureMaterial(net.minecraft.block.material.Material mcMaterial) {
		this.mcMaterial = mcMaterial;
	}

	public Material getMcMaterial() {
		return mcMaterial;
	}
}
