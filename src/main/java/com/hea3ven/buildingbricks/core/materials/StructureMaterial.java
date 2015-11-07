package com.hea3ven.buildingbricks.core.materials;

import net.minecraft.block.Block;
import net.minecraft.block.Block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.EnumWorldBlockLayer;

public enum StructureMaterial {
	ROCK(Material.rock, EnumWorldBlockLayer.SOLID, false, Block.soundTypePiston, 1.5f, 10.0f, 0.6f,
			"pickaxe",
			new MaterialBlockType[] {MaterialBlockType.FULL, MaterialBlockType.STAIRS,
					MaterialBlockType.SLAB, MaterialBlockType.VERTICAL_SLAB, MaterialBlockType.STEP,
					MaterialBlockType.CORNER, MaterialBlockType.WALL}),
	METAL(Material.iron, EnumWorldBlockLayer.SOLID, false, Block.soundTypeMetal, 5.0f, 10.0f, 0.6f,
			"pickaxe",
			new MaterialBlockType[] {MaterialBlockType.FULL, MaterialBlockType.STAIRS,
					MaterialBlockType.SLAB, MaterialBlockType.VERTICAL_SLAB, MaterialBlockType.STEP,
					MaterialBlockType.CORNER, MaterialBlockType.WALL}),
	WOOD(Material.wood, EnumWorldBlockLayer.SOLID, false, Block.soundTypeWood, 2.0f, 5.0f, 0.6f,
			"axe",
			new MaterialBlockType[] {MaterialBlockType.FULL, MaterialBlockType.STAIRS,
					MaterialBlockType.SLAB, MaterialBlockType.VERTICAL_SLAB, MaterialBlockType.STEP,
					MaterialBlockType.CORNER, MaterialBlockType.FENCE}),
	DIRT(Material.ground, EnumWorldBlockLayer.SOLID, false, Block.soundTypeGravel, 0.5f, -1, 0.6f,
			"shovel",
			new MaterialBlockType[] {MaterialBlockType.FULL, MaterialBlockType.STAIRS,
					MaterialBlockType.SLAB, MaterialBlockType.VERTICAL_SLAB, MaterialBlockType.STEP,
					MaterialBlockType.CORNER}),
	CLOTH(Material.cloth, EnumWorldBlockLayer.SOLID, false, Block.soundTypeCloth, 0.8f, -1, 0.6f,
			null,
			new MaterialBlockType[] {MaterialBlockType.FULL, MaterialBlockType.STAIRS,
					MaterialBlockType.SLAB, MaterialBlockType.VERTICAL_SLAB, MaterialBlockType.STEP,
					MaterialBlockType.CORNER}),
	GRASS(Material.grass, EnumWorldBlockLayer.CUTOUT_MIPPED, true, Block.soundTypeGrass, 0.6f, -1,
			0.6f, "shovel",
			new MaterialBlockType[] {MaterialBlockType.FULL, MaterialBlockType.STAIRS,
					MaterialBlockType.SLAB, MaterialBlockType.VERTICAL_SLAB, MaterialBlockType.STEP,
					MaterialBlockType.CORNER}),
	GLASS(Material.glass, EnumWorldBlockLayer.CUTOUT, false, Block.soundTypeGlass, 0.3f, -1, 0.6f,
			null,
			new MaterialBlockType[] {MaterialBlockType.FULL, MaterialBlockType.STAIRS,
					MaterialBlockType.SLAB, MaterialBlockType.VERTICAL_SLAB, MaterialBlockType.STEP,
					MaterialBlockType.CORNER, MaterialBlockType.WALL}),
	ICE(Material.ice, EnumWorldBlockLayer.TRANSLUCENT, false, Block.soundTypeGlass, 0.5f, -1, 0.98f,
			null,
			new MaterialBlockType[] {MaterialBlockType.FULL, MaterialBlockType.STAIRS,
					MaterialBlockType.SLAB, MaterialBlockType.VERTICAL_SLAB, MaterialBlockType.STEP,
					MaterialBlockType.CORNER, MaterialBlockType.WALL}),
	PACKED_ICE(Material.packedIce, EnumWorldBlockLayer.SOLID, false, Block.soundTypeGlass, 0.5f, -1,
			0.98f, null,
			new MaterialBlockType[] {MaterialBlockType.FULL, MaterialBlockType.STAIRS,
					MaterialBlockType.SLAB, MaterialBlockType.VERTICAL_SLAB, MaterialBlockType.STEP,
					MaterialBlockType.CORNER, MaterialBlockType.WALL}),
	SNOW(Material.craftedSnow, EnumWorldBlockLayer.SOLID, false, Block.soundTypeSnow, 0.2f, -1,
			0.6f, "shovel",
			new MaterialBlockType[] {MaterialBlockType.FULL, MaterialBlockType.STAIRS,
					MaterialBlockType.SLAB, MaterialBlockType.VERTICAL_SLAB, MaterialBlockType.STEP,
					MaterialBlockType.CORNER});

	private Material mcMaterial;
	private EnumWorldBlockLayer blockLayer;
	private boolean color;
	private SoundType sound;
	private float hardness;
	private float resistance;
	private float slipperiness;
	private String tool;
	private MaterialBlockType[] blockTypes;

	private StructureMaterial(Material mcMaterial, EnumWorldBlockLayer blockLayer, boolean color,
			Block.SoundType sound, float hardness, float resistance, float slipperiness,
			String tool, MaterialBlockType[] blockTypes) {
		this.mcMaterial = mcMaterial;
		this.blockLayer = blockLayer;
		this.color = color;
		this.sound = sound;
		this.hardness = hardness;
		this.resistance = resistance;
		this.slipperiness = slipperiness;
		this.tool = tool;
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

	public float getHardness() {
		return hardness;
	}

	public float getResistance() {
		return resistance;
	}

	public float getSlipperiness() {
		return slipperiness;
	}

	public String getTool() {
		return tool;
	}
}
