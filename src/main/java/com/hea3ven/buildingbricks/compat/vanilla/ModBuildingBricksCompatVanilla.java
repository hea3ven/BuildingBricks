package com.hea3ven.buildingbricks.compat.vanilla;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

import com.hea3ven.buildingbricks.compat.vanilla.blocks.BlockGrassCorner;
import com.hea3ven.buildingbricks.compat.vanilla.blocks.BlockGrassSlab;
import com.hea3ven.buildingbricks.compat.vanilla.blocks.BlockGrassStairs;
import com.hea3ven.buildingbricks.compat.vanilla.blocks.BlockGrassStep;
import com.hea3ven.buildingbricks.compat.vanilla.blocks.BlockGrassVerticalSlab;
import com.hea3ven.buildingbricks.compat.vanilla.items.ItemColoredWrapper;
import com.hea3ven.buildingbricks.core.lib.BlockDescription;
import com.hea3ven.buildingbricks.core.materials.Material;
import com.hea3ven.buildingbricks.core.materials.MaterialBlockType;
import com.hea3ven.buildingbricks.core.materials.MaterialRegistry;
import com.hea3ven.buildingbricks.core.materials.StructureMaterial;

@Mod(modid = ModBuildingBricksCompatVanilla.MODID, name = "Building Bricks Vanilla Compatibilty",
		version = ModBuildingBricksCompatVanilla.VERSION)
public class ModBuildingBricksCompatVanilla {
	public static final String MODID = "buildingbrickscompatvanilla";
	public static final String VERSION = "1.0.0";

	public static Block grassStairs;
	public static Block grassSlab;
	public static Block grassStep;
	public static Block grassCorner;
	public static Block grassVertSlab;

	static {
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		grassStairs = new BlockGrassStairs(Blocks.grass.getDefaultState())
				.setUnlocalizedName("grass_stairs");
		grassSlab = new BlockGrassSlab(StructureMaterial.GRASS).setUnlocalizedName("grass_slab");
		grassVertSlab = new BlockGrassVerticalSlab(StructureMaterial.GRASS)
				.setUnlocalizedName("grass_vertical_slab");
		grassStep = new BlockGrassStep(StructureMaterial.GRASS).setUnlocalizedName("grass_step");
		grassCorner = new BlockGrassCorner(StructureMaterial.GRASS)
				.setUnlocalizedName("grass_corner");

		GameRegistry.registerBlock(grassStairs, ItemColoredWrapper.class, "grass_stairs", false);
		GameRegistry.registerBlock(grassSlab, ItemColoredWrapper.class, "grass_slab", false);
		GameRegistry.registerBlock(grassVertSlab, ItemColoredWrapper.class, "grass_vertical_slab",
				false);
		GameRegistry.registerBlock(grassStep, ItemColoredWrapper.class, "grass_step", false);
		GameRegistry.registerBlock(grassCorner, ItemColoredWrapper.class, "grass_corner", false);

		Material grassMat = new Material("grass");
		grassMat.setTexture("minecraft:blocks/grass_top", "minecraft:blocks/dirt",
				"minecraft:blocks/grass_side");
		grassMat.setStructureMaterial(StructureMaterial.GRASS);
		grassMat.addBlock(new BlockDescription(MaterialBlockType.FULL, Blocks.grass));
		grassMat.addBlock(new BlockDescription(MaterialBlockType.STAIRS, grassStairs));
		grassMat.addBlock(new BlockDescription(MaterialBlockType.SLAB, grassSlab));
		grassMat.addBlock(new BlockDescription(MaterialBlockType.VERTICAL_SLAB, grassVertSlab));
		grassMat.addBlock(new BlockDescription(MaterialBlockType.STEP, grassStep));
		grassMat.addBlock(new BlockDescription(MaterialBlockType.CORNER, grassCorner));
		MaterialRegistry.registerMaterial(grassMat);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(new GrassSlabWorldGen());

		if (event.getSide() == Side.CLIENT) {
			ItemModelMesher modelMesher = Minecraft
					.getMinecraft()
					.getRenderItem()
					.getItemModelMesher();
			modelMesher.register(Item.getItemFromBlock(grassStairs), 0, new ModelResourceLocation(
					"buildingbrickscompatvanilla:grass_stairs", "inventory"));
			modelMesher.register(Item.getItemFromBlock(grassSlab), 0, new ModelResourceLocation(
					"buildingbrickscompatvanilla:grass_slab", "inventory"));
			modelMesher.register(Item.getItemFromBlock(grassVertSlab), 0, new ModelResourceLocation(
					"buildingbrickscompatvanilla:grass_vertical_slab", "inventory"));
			modelMesher.register(Item.getItemFromBlock(grassStep), 0, new ModelResourceLocation(
					"buildingbrickscompatvanilla:grass_step", "inventory"));
			modelMesher.register(Item.getItemFromBlock(grassCorner), 0, new ModelResourceLocation(
					"buildingbrickscompatvanilla:grass_corner", "inventory"));
		}
	}

}
