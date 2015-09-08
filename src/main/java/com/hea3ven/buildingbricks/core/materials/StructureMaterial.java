package com.hea3ven.buildingbricks.core.materials;

import net.minecraft.block.Block;
import net.minecraft.block.Block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.EnumWorldBlockLayer;

public enum StructureMaterial {
	ROCK(Material.rock, EnumWorldBlockLayer.SOLID, false, Block.soundTypePiston,
			new MaterialBlockType[] {MaterialBlockType.FULL, MaterialBlockType.STAIRS,
					MaterialBlockType.SLAB, MaterialBlockType.VERTICAL_SLAB, MaterialBlockType.STEP,
					MaterialBlockType.CORNER, MaterialBlockType.WALL}),
	METAL(Material.iron, EnumWorldBlockLayer.SOLID, false, Block.soundTypeMetal,
			new MaterialBlockType[] {MaterialBlockType.FULL, MaterialBlockType.STAIRS,
					MaterialBlockType.SLAB, MaterialBlockType.VERTICAL_SLAB, MaterialBlockType.STEP,
					MaterialBlockType.CORNER, MaterialBlockType.WALL}),
	WOOD(Material.wood, EnumWorldBlockLayer.SOLID, false, Block.soundTypeWood,
			new MaterialBlockType[] {MaterialBlockType.FULL, MaterialBlockType.STAIRS,
					MaterialBlockType.SLAB, MaterialBlockType.VERTICAL_SLAB, MaterialBlockType.STEP,
					MaterialBlockType.CORNER}),
	DIRT(Material.ground, EnumWorldBlockLayer.SOLID, false, Block.soundTypeGravel,
			new MaterialBlockType[] {MaterialBlockType.FULL, MaterialBlockType.STAIRS,
					MaterialBlockType.SLAB, MaterialBlockType.VERTICAL_SLAB, MaterialBlockType.STEP,
					MaterialBlockType.CORNER}),
	CLOTH(Material.cloth, EnumWorldBlockLayer.SOLID, false, Block.soundTypeCloth,
			new MaterialBlockType[] {MaterialBlockType.FULL, MaterialBlockType.STAIRS,
					MaterialBlockType.SLAB, MaterialBlockType.VERTICAL_SLAB, MaterialBlockType.STEP,
					MaterialBlockType.CORNER}),
	GRASS(Material.grass, EnumWorldBlockLayer.CUTOUT_MIPPED, true, Block.soundTypeGrass,
			new MaterialBlockType[] {MaterialBlockType.FULL, MaterialBlockType.STAIRS,
					MaterialBlockType.SLAB, MaterialBlockType.VERTICAL_SLAB, MaterialBlockType.STEP,
					MaterialBlockType.CORNER}),
	GLASS(Material.glass, EnumWorldBlockLayer.CUTOUT, false, Block.soundTypeGlass,
			new MaterialBlockType[] {MaterialBlockType.FULL, MaterialBlockType.STAIRS,
					MaterialBlockType.SLAB, MaterialBlockType.VERTICAL_SLAB, MaterialBlockType.STEP,
					MaterialBlockType.CORNER, MaterialBlockType.WALL}),
	ICE(Material.ice, EnumWorldBlockLayer.SOLID, false, Block.soundTypeGlass,
			new MaterialBlockType[] {MaterialBlockType.FULL, MaterialBlockType.STAIRS,
					MaterialBlockType.SLAB, MaterialBlockType.VERTICAL_SLAB, MaterialBlockType.STEP,
					MaterialBlockType.CORNER, MaterialBlockType.WALL}),
	PACKED_ICE(Material.packedIce, EnumWorldBlockLayer.SOLID, false, Block.soundTypeGlass,
			new MaterialBlockType[] {MaterialBlockType.FULL, MaterialBlockType.STAIRS,
					MaterialBlockType.SLAB, MaterialBlockType.VERTICAL_SLAB, MaterialBlockType.STEP,
					MaterialBlockType.CORNER, MaterialBlockType.WALL}),
	SNOW(Material.craftedSnow, EnumWorldBlockLayer.SOLID, false, Block.soundTypeSnow,
			new MaterialBlockType[] {MaterialBlockType.FULL, MaterialBlockType.STAIRS,
					MaterialBlockType.SLAB, MaterialBlockType.VERTICAL_SLAB, MaterialBlockType.STEP,
					MaterialBlockType.CORNER}),
	CLAY(Material.clay, EnumWorldBlockLayer.SOLID, false, Block.soundTypeGravel,
			new MaterialBlockType[] {MaterialBlockType.FULL, MaterialBlockType.STAIRS,
					MaterialBlockType.SLAB, MaterialBlockType.VERTICAL_SLAB, MaterialBlockType.STEP,
					MaterialBlockType.CORNER, MaterialBlockType.WALL});

	private Material mcMaterial;
	private EnumWorldBlockLayer blockLayer;
	private boolean color;
	private SoundType sound;
	private MaterialBlockType[] blockTypes;

	private StructureMaterial(Material mcMaterial, EnumWorldBlockLayer blockLayer, boolean color,
			Block.SoundType sound, MaterialBlockType[] blockTypes) {
		this.mcMaterial = mcMaterial;
		this.blockLayer = blockLayer;
		this.color = color;
		this.sound = sound;
		this.blockTypes = blockTypes;
	}

	public Material getMcMaterial() {
		return mcMaterial;
	}

	public String getName() {
		return name().toLowerCase();
	}

	public EnumWorldBlockLayer getBlockLayer() {
		return blockLayer;
	}

	public MaterialBlockType[] getBlockTypes() {
		return blockTypes;
	}

	public boolean getColor() {
		return color;
	}

	public SoundType getSoundType() {
		return sound;
	}
}
