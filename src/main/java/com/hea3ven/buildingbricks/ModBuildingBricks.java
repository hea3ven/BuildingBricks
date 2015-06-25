package com.hea3ven.buildingbricks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSandStone;
import net.minecraft.block.BlockStone;
import net.minecraft.init.Blocks;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

import com.hea3ven.buildingbricks.core.ProxyCommonBuildingBricks;
import com.hea3ven.buildingbricks.core.blocks.BlockMaterialStep;
import com.hea3ven.buildingbricks.core.blocks.BlockVerticalSlab;
import com.hea3ven.buildingbricks.core.items.ItemTrowel;
import com.hea3ven.buildingbricks.core.lib.BlockDescription;
import com.hea3ven.buildingbricks.core.materials.Material;
import com.hea3ven.buildingbricks.core.materials.MaterialBlockRegistry;
import com.hea3ven.buildingbricks.core.materials.MaterialBlockType;
import com.hea3ven.buildingbricks.core.materials.MaterialRegistry;
import com.hea3ven.buildingbricks.core.network.TrowelRotateBlockTypeMessage;
import com.hea3ven.buildingbricks.core.tileentity.TileEntityMaterial;

@Mod(modid = ModBuildingBricks.MODID, name = "Building Bricks", version = ModBuildingBricks.VERSION)
public class ModBuildingBricks {
	public static final String MODID = "buildingbricks";
	public static final String VERSION = "1.0.0";

	@SidedProxy(serverSide = "com.hea3ven.buildingbricks.core.ProxyCommonBuildingBricks",
			clientSide = "com.hea3ven.buildingbricks.core.ProxyClientBuildingBricks")
	private static ProxyCommonBuildingBricks proxy;

	public static SimpleNetworkWrapper netChannel;

	public static BlockVerticalSlab andesiteSlab;
	public static Block redSandstoneSlab;
	public static Block andesiteStep;
	public static Block redSandstoneStep;
	public static ItemTrowel trowel;

	private Material andesiteMaterial;
	private Material redSandstoneMaterial;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		netChannel = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);
		netChannel.registerMessage(TrowelRotateBlockTypeMessage.Handler.class,
				TrowelRotateBlockTypeMessage.class, 0, Side.SERVER);

		GameRegistry.registerTileEntity(TileEntityMaterial.class, "tile.material");

		MaterialBlockRegistry.instance.init();

		andesiteMaterial = new Material("andesite");
		andesiteMaterial.setTexture("blocks/stone_andesite");
		andesiteMaterial.setStructureMaterial(net.minecraft.block.material.Material.rock);
		MaterialRegistry.registerMaterial(andesiteMaterial);
		redSandstoneMaterial = new Material("red_sandstone");
		redSandstoneMaterial.setTexture("blocks/red_sandstone_top", "blocks/red_sandstone_bottom",
				"blocks/red_sandstone_normal");
		redSandstoneMaterial.setStructureMaterial(net.minecraft.block.material.Material.rock);
		MaterialRegistry.registerMaterial(redSandstoneMaterial);
		andesiteSlab = new BlockVerticalSlab("andesite_slab");
		redSandstoneSlab = new BlockVerticalSlab("red_sandstone_slab");
		andesiteStep = new BlockMaterialStep("andesite_step");
		redSandstoneStep = new BlockMaterialStep("red_sandstone_step");

		GameRegistry.registerBlock(andesiteSlab, "andesite_slab");
		GameRegistry.registerBlock(redSandstoneSlab, "red_sandstone_slab");
		GameRegistry.registerBlock(andesiteStep, "andesite_step");
		GameRegistry.registerBlock(redSandstoneStep, "red_sandstone_step");
		andesiteMaterial.addBlock(MaterialBlockType.FULL, new BlockDescription(Blocks.stone, BlockStone.EnumType.ANDESITE.getMetadata()));
		redSandstoneMaterial.addBlock(MaterialBlockType.FULL, new BlockDescription(Blocks.sandstone, BlockSandStone.EnumType.DEFAULT.getMetadata()));
		andesiteMaterial.addBlock(MaterialBlockType.SLAB, new BlockDescription(andesiteSlab));
		redSandstoneMaterial.addBlock(MaterialBlockType.SLAB, new BlockDescription(redSandstoneSlab));
		andesiteMaterial.addBlock(MaterialBlockType.STEP, new BlockDescription(andesiteStep));
		redSandstoneMaterial.addBlock(MaterialBlockType.STEP, new BlockDescription(redSandstoneStep));
		andesiteMaterial.addBlock(MaterialBlockType.CORNER);
		redSandstoneMaterial.addBlock(MaterialBlockType.CORNER);

		trowel = new ItemTrowel();
		GameRegistry.registerItem(trowel, "trowel");

		proxy.preInit();
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init();
		System.out.println("DIRT BLOCK >> " + Blocks.dirt.getUnlocalizedName());
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit();
	}
}
