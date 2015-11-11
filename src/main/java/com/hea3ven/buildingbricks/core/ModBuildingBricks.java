package com.hea3ven.buildingbricks.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.item.ItemStack;
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
import net.minecraftforge.oredict.ShapedOreRecipe;

import com.hea3ven.buildingbricks.core.blocks.BlockPortableLadder;
import com.hea3ven.buildingbricks.core.config.Config;
import com.hea3ven.buildingbricks.core.items.ItemTrowel;
import com.hea3ven.buildingbricks.core.materials.MaterialBlockRegistry;
import com.hea3ven.buildingbricks.core.materials.MaterialRegistry;
import com.hea3ven.buildingbricks.core.materials.loader.MaterialResourceLoader;
import com.hea3ven.buildingbricks.core.network.TrowelRotateBlockTypeMessage;
import com.hea3ven.buildingbricks.core.tileentity.TileMaterial;
import com.hea3ven.tools.commonutils.resources.ResourceScanner;

@Mod(modid = Properties.MODID, name = "Building Bricks", version = Properties.VERSION,
		dependencies = Properties.DEPENDENCIES,
		guiFactory = "com.hea3ven.buildingbricks.core.config.BuildingBricksConfigGuiFactory")
public class ModBuildingBricks {

	public static final Logger logger = LogManager.getLogger("BuildingBricks");

	@SidedProxy(serverSide = "com.hea3ven.buildingbricks.core.ProxyCommonBuildingBricks",
			clientSide = "com.hea3ven.buildingbricks.core.ProxyClientBuildingBricks")
	private static ProxyCommonBuildingBricks proxy;

	@SidedProxy(serverSide = "com.hea3ven.tools.commonutils.resources.ResourceScannerServer",
			clientSide = "com.hea3ven.tools.commonutils.resources.ResourceScannerClient")
	private static ResourceScanner resScanner;

	public static SimpleNetworkWrapper netChannel;

	public static ItemTrowel trowel;
	public static BlockPortableLadder portableLadder;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger.info("Initializing config");
		Config.init(event.getModConfigurationDirectory());

		netChannel = NetworkRegistry.INSTANCE.newSimpleChannel(Properties.MODID);
		netChannel.registerMessage(TrowelRotateBlockTypeMessage.Handler.class,
				TrowelRotateBlockTypeMessage.class, 0, Side.SERVER);

		GameRegistry.registerTileEntity(TileMaterial.class, "tile.material");

		trowel = new ItemTrowel();
		GameRegistry.registerItem(trowel, "trowel");

		portableLadder = new BlockPortableLadder();
		GameRegistry.registerBlock(portableLadder, BlockPortableLadder.ItemPortableLadder.class,
				"portable_ladder").setUnlocalizedName("portableLadder");

		logger.info("Registering materials from resources");
		MaterialResourceLoader.loadResources(resScanner, Properties.MODID);

		proxy.preInit();
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		MaterialBlockRegistry.instance.logStats();

		MaterialRegistry.logStats();

		proxy.init();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		GameRegistry.addRecipe(
				new ShapedOreRecipe(new ItemStack(trowel), " is", "ii ", 's', "stickWood", 'i', "ingotIron"));

		proxy.postInit();
	}
}
