package com.hea3ven.buildingbricks.core.materials;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockRenderLayer;

public enum StructureMaterial {
	ROCK(Material.ROCK, BlockRenderLayer.SOLID, false, SoundType.STONE, 1.5f, 10.0f, 0.6f, "pickaxe",
			new MaterialBlockType[] {MaterialBlockType.FULL, MaterialBlockType.STAIRS, MaterialBlockType.SLAB,
					MaterialBlockType.VERTICAL_SLAB, MaterialBlockType.STEP, MaterialBlockType.CORNER,
					MaterialBlockType.WALL}),
	METAL(Material.IRON, BlockRenderLayer.SOLID, false, SoundType.METAL, 5.0f, 10.0f, 0.6f, "pickaxe",
			new MaterialBlockType[] {MaterialBlockType.FULL, MaterialBlockType.STAIRS, MaterialBlockType.SLAB,
					MaterialBlockType.VERTICAL_SLAB, MaterialBlockType.STEP, MaterialBlockType.CORNER,
					MaterialBlockType.WALL}),
	WOOD(Material.WOOD, BlockRenderLayer.SOLID, false, SoundType.WOOD, 2.0f, 5.0f, 0.6f, "axe",
			new MaterialBlockType[] {MaterialBlockType.FULL, MaterialBlockType.STAIRS, MaterialBlockType.SLAB,
					MaterialBlockType.VERTICAL_SLAB, MaterialBlockType.STEP, MaterialBlockType.CORNER,
					MaterialBlockType.FENCE, MaterialBlockType.FENCE_GATE}),
	DIRT(Material.GROUND, BlockRenderLayer.SOLID, false, SoundType.GROUND, 0.5f, -1, 0.6f, "shovel",
			new MaterialBlockType[] {MaterialBlockType.FULL, MaterialBlockType.STAIRS, MaterialBlockType.SLAB,
					MaterialBlockType.VERTICAL_SLAB, MaterialBlockType.STEP, MaterialBlockType.CORNER}),
	CLOTH(Material.CLOTH, BlockRenderLayer.SOLID, false, SoundType.CLOTH, 0.8f, -1, 0.6f, null,
			new MaterialBlockType[] {MaterialBlockType.FULL, MaterialBlockType.STAIRS, MaterialBlockType.SLAB,
					MaterialBlockType.VERTICAL_SLAB, MaterialBlockType.STEP, MaterialBlockType.CORNER}),
	GRASS(Material.GRASS, BlockRenderLayer.CUTOUT_MIPPED, true, SoundType.PLANT, 0.6f, -1, 0.6f, "shovel",
			new MaterialBlockType[] {MaterialBlockType.FULL, MaterialBlockType.STAIRS, MaterialBlockType.SLAB,
					MaterialBlockType.VERTICAL_SLAB, MaterialBlockType.STEP, MaterialBlockType.CORNER}),
	GLASS(Material.GLASS, BlockRenderLayer.TRANSLUCENT, false, SoundType.GLASS, 0.3f, -1, 0.6f, null,
			new MaterialBlockType[] {MaterialBlockType.FULL, MaterialBlockType.STAIRS, MaterialBlockType.SLAB,
					MaterialBlockType.VERTICAL_SLAB, MaterialBlockType.STEP, MaterialBlockType.CORNER,
					MaterialBlockType.WALL, MaterialBlockType.PANE}),
	ICE(Material.ICE, BlockRenderLayer.TRANSLUCENT, false, SoundType.GLASS, 0.5f, -1, 0.98f, null,
			new MaterialBlockType[] {MaterialBlockType.FULL, MaterialBlockType.STAIRS, MaterialBlockType.SLAB,
					MaterialBlockType.VERTICAL_SLAB, MaterialBlockType.STEP, MaterialBlockType.CORNER,
					MaterialBlockType.WALL, MaterialBlockType.PANE}),
	PACKED_ICE(Material.PACKED_ICE, BlockRenderLayer.SOLID, false, SoundType.GLASS, 0.5f, -1, 0.98f, null,
			new MaterialBlockType[] {MaterialBlockType.FULL, MaterialBlockType.STAIRS, MaterialBlockType.SLAB,
					MaterialBlockType.VERTICAL_SLAB, MaterialBlockType.STEP, MaterialBlockType.CORNER,
					MaterialBlockType.WALL, MaterialBlockType.PANE}),
	SNOW(Material.CRAFTED_SNOW, BlockRenderLayer.SOLID, false, SoundType.SNOW, 0.2f, -1, 0.6f, "shovel",
			new MaterialBlockType[] {MaterialBlockType.FULL, MaterialBlockType.STAIRS, MaterialBlockType.SLAB,
					MaterialBlockType.VERTICAL_SLAB, MaterialBlockType.STEP, MaterialBlockType.CORNER}),
	PLANT(Material.PLANTS, BlockRenderLayer.SOLID, true, SoundType.PLANT, 0.6f, -1, 0.6f, null,
			new MaterialBlockType[] {MaterialBlockType.FULL, MaterialBlockType.STAIRS, MaterialBlockType.SLAB,
			MaterialBlockType.VERTICAL_SLAB, MaterialBlockType.STEP, MaterialBlockType.CORNER});

	private Material mcMaterial;
	private BlockRenderLayer blockLayer;
	private boolean color;
	private SoundType sound;
	private float hardness;
	private float resistance;
	private float slipperiness;
	private String tool;
	private MaterialBlockType[] blockTypes;

	StructureMaterial(Material mcMaterial, BlockRenderLayer blockLayer, boolean color,
			SoundType sound, float hardness, float resistance, float slipperiness, String tool,
			MaterialBlockType[] blockTypes) {
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

	public BlockRenderLayer getBlockLayer() {
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
