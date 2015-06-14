package com.hea3ven.buildingbricks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSandStone;
import net.minecraft.block.BlockStone;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.OreDictionary;

import com.hea3ven.buildingbricks.core.ProxyCommonBuildingBricks;
import com.hea3ven.buildingbricks.core.blocks.BlockMaterialCorner;
import com.hea3ven.buildingbricks.core.blocks.BlockMaterialSlab;
import com.hea3ven.buildingbricks.core.blocks.BlockMaterialStep;
import com.hea3ven.buildingbricks.core.items.ItemTrowel;
import com.hea3ven.buildingbricks.core.materials.Material;
import com.hea3ven.buildingbricks.core.materials.MaterialRegistry;
import com.hea3ven.buildingbricks.core.network.TrowelRotateBlockTypeMessage;

@Mod(modid = ModBuildingBricks.MODID, name = "Building Bricks", version = ModBuildingBricks.VERSION)
public class ModBuildingBricks
{
	public static final String MODID = "buildingbricks";
	public static final String VERSION = "1.0.0";

	@SidedProxy(serverSide = "com.hea3ven.buildingbricks.core.ProxyCommonBuildingBricks",
			clientSide = "com.hea3ven.buildingbricks.core.ProxyClientBuildingBricks")
	private static ProxyCommonBuildingBricks proxy;

	public static SimpleNetworkWrapper netChannel;

	public static Block andesiteSlab;
	public static Block redSandstoneSlab;
	public static Block andesiteStep;
	public static Block redSandstoneStep;
	public static Block andesiteCorner;
	public static Block redSandstoneCorner;
	public static ItemTrowel trowel;


	private Material andesiteMaterial;
	private Material redSandstoneMaterial;
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		netChannel = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);
		netChannel.registerMessage(TrowelRotateBlockTypeMessage.Handler.class, TrowelRotateBlockTypeMessage.class, 0, Side.SERVER);

		andesiteMaterial = new Material("andesite");
		andesiteMaterial.setTextureName("blocks/stone_andesite");
		MaterialRegistry.registerMaterial(andesiteMaterial);
		redSandstoneMaterial = new Material("red_sandstone");
		redSandstoneMaterial.topTextureLocation = "blocks/red_sandstone_top";
		redSandstoneMaterial.bottomTextureLocation = "blocks/red_sandstone_bottom";
		redSandstoneMaterial.sideTextureLocation = "blocks/red_sandstone_normal";
		MaterialRegistry.registerMaterial(redSandstoneMaterial);
		andesiteSlab = new BlockMaterialSlab("andesite_slab");
		redSandstoneSlab = new BlockMaterialSlab("red_sandstone_slab");
		andesiteStep = new BlockMaterialStep("andesite_step");
		redSandstoneStep = new BlockMaterialStep("red_sandstone_step");
		andesiteCorner = new BlockMaterialCorner("andesite_corner");
		redSandstoneCorner = new BlockMaterialCorner("red_sandstone_corner");

		GameRegistry.registerBlock(andesiteSlab, "andesite_slab");
		GameRegistry.registerBlock(redSandstoneSlab, "red_sandstone_slab");
//		try {
//			GameRegistry.addSubstitutionAlias("minecraft:red_sandstone_slab", Type.BLOCK, redSandstoneSlab);
//		} catch (ExistingSubstitutionException e) {
//			e.printStackTrace();
//		}
		GameRegistry.registerBlock(andesiteStep, "andesite_step");
		GameRegistry.registerBlock(redSandstoneStep, "red_sandstone_step");
		GameRegistry.registerBlock(andesiteCorner, "andesite_corner");
		GameRegistry.registerBlock(redSandstoneCorner, "red_sandstone_corner");
		andesiteMaterial.setFullBlock(Blocks.stone, BlockStone.EnumType.ANDESITE.getMetadata());
		redSandstoneMaterial.setFullBlock(Blocks.sandstone, BlockSandStone.EnumType.DEFAULT.getMetadata());
		andesiteMaterial.setSlabBlock(andesiteSlab);
		redSandstoneMaterial.setSlabBlock(redSandstoneSlab);
		andesiteMaterial.setStepBlock(andesiteStep);
		redSandstoneMaterial.setStepBlock(redSandstoneStep);
		andesiteMaterial.setCornerBlock(andesiteCorner);
		redSandstoneMaterial.setCornerBlock(redSandstoneCorner);

		trowel = new ItemTrowel();
		GameRegistry.registerItem(trowel, "trowel");
		for (Material mat : MaterialRegistry.getAll()) {
			ItemStack blockStack = new ItemStack(mat.getSlabBlock());
			ItemStack trowelStack = new ItemStack(trowel, 1, OreDictionary.WILDCARD_VALUE);
			ItemStack bindedTrowelStack = new ItemStack(trowel);
			trowel.setBindedMaterial(bindedTrowelStack, mat);
			GameRegistry.addShapelessRecipe(bindedTrowelStack, trowelStack, blockStack);
		}

		proxy.preInit();
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		proxy.init();
		// some example code
		System.out.println("DIRT BLOCK >> " + Blocks.dirt.getUnlocalizedName());
	}
}
