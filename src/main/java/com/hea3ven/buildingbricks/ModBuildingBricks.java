package com.hea3ven.buildingbricks;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.item.ItemStack;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;

import net.minecraftforge.oredict.ShapedOreRecipe;

import com.hea3ven.buildingbricks.core.ProxyCommonBuildingBricks;
import com.hea3ven.buildingbricks.core.config.Config;
import com.hea3ven.buildingbricks.core.items.ItemTrowel;
import com.hea3ven.buildingbricks.core.materials.MaterialBlockRegistry;
import com.hea3ven.buildingbricks.core.materials.MaterialRegistry;
import com.hea3ven.buildingbricks.core.materials.MaterialResourceLoader;
import com.hea3ven.buildingbricks.core.network.TrowelRotateBlockTypeMessage;
import com.hea3ven.buildingbricks.core.tileentity.TileMaterial;

@Mod(modid = ModBuildingBricks.MODID, name = "Building Bricks", version = ModBuildingBricks.VERSION,
		dependencies = "required-after:Forge@[10.13.4.1490,)",
		guiFactory = "com.hea3ven.buildingbricks.core.config.BuildingBricksConfigGuiFactory")
public class ModBuildingBricks {

	public static final String MODID = "buildingbricks";
	public static final String VERSION = "1.0.0";

	public static final Logger logger = LogManager.getLogger("BuildingBricks");

	@SidedProxy(serverSide = "com.hea3ven.buildingbricks.core.ProxyCommonBuildingBricks",
			clientSide = "com.hea3ven.buildingbricks.core.ProxyClientBuildingBricks")
	private static ProxyCommonBuildingBricks proxy;

	public static SimpleNetworkWrapper netChannel;

	public static ItemTrowel trowel;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger.info("Initializing config");
		Config.init(event.getModConfigurationDirectory());

		netChannel = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);
		netChannel.registerMessage(TrowelRotateBlockTypeMessage.Handler.class,
				TrowelRotateBlockTypeMessage.class, 0, Side.SERVER);

		GameRegistry.registerTileEntity(TileMaterial.class, "tile.material");

		logger.info("Registering materials from resources");
		MaterialResourceLoader.discoverMaterials();

		trowel = new ItemTrowel();
		GameRegistry.registerItem(trowel, "trowel");

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
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(trowel), " is", "ii ", 's',
				"stickWood", 'i', "ingotIron"));

		proxy.postInit();
	}
}
