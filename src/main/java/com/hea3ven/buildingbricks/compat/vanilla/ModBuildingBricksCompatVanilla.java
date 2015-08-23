package com.hea3ven.buildingbricks.compat.vanilla;

import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockRedSandstone;
import net.minecraft.block.BlockStone;
import net.minecraft.block.BlockStone.EnumType;
import net.minecraft.block.BlockStoneSlab;
import net.minecraft.block.BlockStoneSlabNew;
import net.minecraft.block.BlockWall;
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
import com.hea3ven.buildingbricks.core.blocks.BlockBuildingBricksSlab;
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

	public static BlockGrassStairs grassStairs;
	public static BlockBuildingBricksSlab grassSlab;
	public static BlockGrassStep grassStep;
	public static BlockGrassCorner grassCorner;
	public static BlockGrassVerticalSlab grassVertSlab;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		grassStairs = new BlockGrassStairs(Blocks.grass.getDefaultState());
		grassStairs.setUnlocalizedName("grass_stairs");
		GameRegistry.registerBlock(grassStairs, ItemColoredWrapper.class, "grass_stairs", false);
		grassSlab = new BlockGrassSlab(StructureMaterial.GRASS);
		grassSlab.setUnlocalizedName("grass_slab");
		GameRegistry.registerBlock(grassSlab, ItemColoredWrapper.class, "grass_slab", false);
		grassVertSlab = new BlockGrassVerticalSlab(StructureMaterial.GRASS);
		grassVertSlab.setUnlocalizedName("grass_vertical_slab");
		GameRegistry.registerBlock(grassVertSlab, ItemColoredWrapper.class, "grass_vertical_slab",
				false);
		grassStep = new BlockGrassStep(StructureMaterial.GRASS);
		grassStep.setUnlocalizedName("grass_step");
		GameRegistry.registerBlock(grassStep, ItemColoredWrapper.class, "grass_step", false);
		grassCorner = new BlockGrassCorner(StructureMaterial.GRASS);
		grassCorner.setUnlocalizedName("grass_corner");
		GameRegistry.registerBlock(grassCorner, ItemColoredWrapper.class, "grass_corner", false);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		createMaterials();

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

	private void createMaterials() {
		Material mat = new Material("stone");
		mat.setTexture("blocks/stone");
		mat.setStructureMaterial(StructureMaterial.ROCK);
		mat.addBlock(new BlockDescription(MaterialBlockType.FULL, Blocks.stone,
				EnumType.STONE.getMetadata()));
		mat.addBlock(MaterialBlockType.STAIRS);
		mat.addBlock(MaterialBlockType.SLAB);
		mat.addBlock(MaterialBlockType.VERTICAL_SLAB);
		mat.addBlock(MaterialBlockType.STEP);
		mat.addBlock(MaterialBlockType.CORNER);
		MaterialRegistry.registerMaterial(mat);

		mat = new Material("cobblestone");
		mat.setTexture("blocks/cobblestone");
		mat.setStructureMaterial(StructureMaterial.ROCK);
		mat.addBlock(new BlockDescription(MaterialBlockType.FULL, Blocks.cobblestone));
		mat.addBlock(new BlockDescription(MaterialBlockType.SLAB, Blocks.stone_slab,
				BlockStoneSlab.EnumType.COBBLESTONE.getMetadata()));
		mat.addBlock(MaterialBlockType.VERTICAL_SLAB);
		mat.addBlock(MaterialBlockType.STEP);
		mat.addBlock(MaterialBlockType.CORNER);
		mat.addBlock(new BlockDescription(MaterialBlockType.WALL, Blocks.cobblestone_wall,
				BlockWall.EnumType.NORMAL.getMetadata()));
		MaterialRegistry.registerMaterial(mat);

		mat = new Material("andesite");
		mat.setTexture("blocks/stone_andesite");
		mat.setStructureMaterial(StructureMaterial.ROCK);
		mat.addBlock(new BlockDescription(MaterialBlockType.FULL, Blocks.stone,
				BlockStone.EnumType.ANDESITE.getMetadata()));
		mat.addBlock(MaterialBlockType.SLAB);
		mat.addBlock(MaterialBlockType.STEP);
		mat.addBlock(MaterialBlockType.CORNER);
		mat.addBlock(MaterialBlockType.WALL);
		MaterialRegistry.registerMaterial(mat);

		mat = new Material("red_sandstone");
		mat.setTexture("blocks/red_sandstone_top", "blocks/red_sandstone_bottom",
				"blocks/red_sandstone_normal");
		mat.setStructureMaterial(StructureMaterial.ROCK);
		mat.addBlock(new BlockDescription(MaterialBlockType.FULL, Blocks.red_sandstone,
				BlockRedSandstone.EnumType.DEFAULT.getMetadata()));
		mat.addBlock(new BlockDescription(MaterialBlockType.SLAB, Blocks.stone_slab2,
				BlockStoneSlabNew.EnumType.RED_SANDSTONE.getMetadata()));
		mat.addBlock(MaterialBlockType.VERTICAL_SLAB);
		mat.addBlock(MaterialBlockType.STEP);
		mat.addBlock(MaterialBlockType.CORNER);
		MaterialRegistry.registerMaterial(mat);

		mat = new Material("planks_oak");
		mat.setTexture("blocks/planks_oak");
		mat.setStructureMaterial(StructureMaterial.WOOD);
		mat.addBlock(new BlockDescription(MaterialBlockType.FULL, Blocks.planks,
				BlockPlanks.EnumType.OAK.getMetadata()));
		mat.addBlock(new BlockDescription(MaterialBlockType.SLAB, Blocks.wooden_slab,
				BlockPlanks.EnumType.OAK.getMetadata()));
		mat.addBlock(MaterialBlockType.VERTICAL_SLAB);
		mat.addBlock(MaterialBlockType.STEP);
		mat.addBlock(MaterialBlockType.CORNER);
		MaterialRegistry.registerMaterial(mat);
	}
}
