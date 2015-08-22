package com.hea3ven.buildingbricks.core.materials;

import net.minecraft.block.material.Material;

public enum StructureMaterial {
	ROCK(Material.rock,
			new MaterialBlockType[] {MaterialBlockType.FULL, MaterialBlockType.STAIRS,
					MaterialBlockType.SLAB, MaterialBlockType.VERTICAL_SLAB, MaterialBlockType.STEP,
					MaterialBlockType.CORNER, MaterialBlockType.WALL}),
	WOOD(Material.wood,
			new MaterialBlockType[] {MaterialBlockType.FULL, MaterialBlockType.STAIRS,
					MaterialBlockType.SLAB, MaterialBlockType.VERTICAL_SLAB, MaterialBlockType.STEP,
					MaterialBlockType.CORNER}),
	GRASS(Material.grass,
			new MaterialBlockType[] {MaterialBlockType.FULL, MaterialBlockType.STAIRS,
					MaterialBlockType.SLAB, MaterialBlockType.VERTICAL_SLAB, MaterialBlockType.STEP,
					MaterialBlockType.CORNER});

	private Material mcMaterial;
	private MaterialBlockType[] blockTypes;

	private StructureMaterial(Material mcMaterial, MaterialBlockType[] blockTypes) {
		this.mcMaterial = mcMaterial;
		this.blockTypes = blockTypes;
	}

	public Material getMcMaterial() {
		return mcMaterial;
	}

	public String getName() {
		return name().toLowerCase();
	}

	public MaterialBlockType[] getBlockTypes() {
		return blockTypes;
	}
}
