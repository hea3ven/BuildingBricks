package com.hea3ven.buildingbricks.compat.vanilla;

import net.minecraft.block.BlockStone.EnumType;
import net.minecraft.block.BlockStoneSlab;
import net.minecraft.init.Blocks;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import com.hea3ven.buildingbricks.core.lib.BlockDescription;
import com.hea3ven.buildingbricks.core.materials.Material;
import com.hea3ven.buildingbricks.core.materials.MaterialBlockType;
import com.hea3ven.buildingbricks.core.materials.MaterialRegistry;
import com.hea3ven.buildingbricks.core.materials.StructureMaterial;

@Mod(modid = ModBuildingBricksCompatVanilla.MODID, name = "Building Bricks Vanilla Compatibilty", version = ModBuildingBricksCompatVanilla.VERSION)
public class ModBuildingBricksCompatVanilla {
	public static final String MODID = "buildingbricks|compatvanilla";
	public static final String VERSION = "1.0.0";

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		Material mat = new Material("stone");
		mat.setTexture("blocks/stone");
		mat.setStructureMaterial(StructureMaterial.ROCK);
		mat.addBlock(MaterialBlockType.FULL,
				new BlockDescription(Blocks.stone, EnumType.STONE.getMetadata()));
		mat.addBlock(MaterialBlockType.SLAB);
		mat.addBlock(MaterialBlockType.STEP);
		mat.addBlock(MaterialBlockType.CORNER);
		MaterialRegistry.registerMaterial(mat);

		mat = new Material("cobblestone");
		mat.setTexture("blocks/cobblestone");
		mat.setStructureMaterial(StructureMaterial.ROCK);
		mat.addBlock(MaterialBlockType.FULL, new BlockDescription(Blocks.cobblestone));
		mat.addBlock(MaterialBlockType.SLAB, new BlockDescription(Blocks.stone_slab,
				BlockStoneSlab.EnumType.COBBLESTONE.getMetadata()));
		mat.addBlock(MaterialBlockType.STEP);
		mat.addBlock(MaterialBlockType.CORNER);
		MaterialRegistry.registerMaterial(mat);

		// mat.setSlabBlock(new BlockDescription(Blocks.stone_slab,
		// BlockStoneSlab.EnumType.STONE.getMetadata()));
	}
}
