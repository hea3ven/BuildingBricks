package com.hea3ven.buildingbricks.core;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.event.FMLMissingMappingsEvent.MissingMapping;
import net.minecraftforge.fml.common.registry.GameRegistry.Type;
import net.minecraftforge.fml.relauncher.Side;

import com.hea3ven.buildingbricks.compat.vanilla.ProxyModBuildingBricksCompatVanilla;
import com.hea3ven.buildingbricks.core.block.BlockPortableLadder;
import com.hea3ven.buildingbricks.core.item.ItemMaterialBag;
import com.hea3ven.buildingbricks.core.item.ItemTrowel;
import com.hea3ven.tools.commonutils.resources.ResourceScanner;

@Mod(modid = ModBuildingBricks.MODID, version = ModBuildingBricks.VERSION,
		dependencies = ModBuildingBricks.DEPENDENCIES,
		guiFactory = "com.hea3ven.buildingbricks.core.config.BuildingBricksConfigGuiFactory",
		updateJSON = "https://raw.githubusercontent.com/hea3ven/BuildingBricks/master/media/update.json")
public class ModBuildingBricks {

	public static final String MODID = "buildingbricks";
	public static final String VERSION = "PROJECTVERSION";
	public static final String DEPENDENCIES = "required-after:Forge@[FORGEVERSION,);after:Quark;after:BiomesOPlenty";

	public static final Logger logger = LogManager.getLogger("BuildingBricks");

	public static ProxyCommonBuildingBricks proxy;

	@SidedProxy(serverSide = "com.hea3ven.tools.commonutils.resources.ResourceScannerServer",
			clientSide = "com.hea3ven.tools.commonutils.resources.ResourceScannerClient")
	static ResourceScanner resScanner;

	public static ItemTrowel trowel;
	public static ItemMaterialBag materialBag;
	public static BlockPortableLadder portableLadder;

	@Mod.EventHandler
	public void construction(FMLConstructionEvent event) {
		proxy = new ProxyCommonBuildingBricks();

		Path gameDir;
		if (event.getSide() == Side.CLIENT)
			gameDir = Minecraft.getMinecraft().mcDataDir.toPath();
		else
			gameDir =
					Paths.get(FMLCommonHandler.instance().getSavesDirectory().getAbsoluteFile().getParent());
		Path resourcesDir = gameDir.resolve("config").resolve("BuildingBricks").resolve("resources");
		if (!Files.exists(resourcesDir)) {
			try {
				Files.createDirectories(resourcesDir);
			} catch (IOException e) {
				logger.error("Could not get resources directory", e);
				return;
			}
		}
		resScanner.addModDirectory(resourcesDir);
	}

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		proxy.onPreInitEvent(event);
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.onInitEvent(event);
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.onPostInitEvent(event);
	}

	@Mod.EventHandler
	public void onRemap(FMLMissingMappingsEvent event) {
		for (MissingMapping missingMapping : event.getAll()) {
			if (missingMapping.name.equals("buildingbrickscompatvanilla:grass_slab")) {
				if (missingMapping.type == Type.BLOCK)
					missingMapping.remap(ProxyModBuildingBricksCompatVanilla.grassSlab);
				else
					missingMapping.remap(
							Item.getItemFromBlock(ProxyModBuildingBricksCompatVanilla.grassSlab));
			} else if (missingMapping.name.startsWith("buildingbricks:") &&
					missingMapping.name.endsWith("_vertical_slab") && missingMapping.type == Type.ITEM) {
				missingMapping.ignore();
			}
		}
	}
}
