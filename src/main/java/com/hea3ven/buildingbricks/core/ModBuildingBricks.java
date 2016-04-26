package com.hea3ven.buildingbricks.core;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.common.eventbus.Subscribe;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.event.FMLMissingMappingsEvent.MissingMapping;
import net.minecraftforge.fml.common.registry.GameRegistry.Type;
import net.minecraftforge.fml.relauncher.Side;

import com.hea3ven.buildingbricks.compat.vanilla.ProxyModBuildingBricksCompatVanilla;
import com.hea3ven.buildingbricks.core.blocks.BlockPortableLadder;
import com.hea3ven.buildingbricks.core.items.ItemMaterialBag;
import com.hea3ven.buildingbricks.core.items.ItemTrowel;
import com.hea3ven.tools.commonutils.resources.ResourceScanner;
import com.hea3ven.tools.commonutils.resources.ResourceScannerClient;
import com.hea3ven.tools.commonutils.resources.ResourceScannerServer;

public class ModBuildingBricks {

	public static final String MODID = "buildingbricks";
	public static final String VERSION = "PROJECTVERSION";
	public static final String FORGE_DEPENDENCY = "Forge@[FORGEVERSION,)";

	public static final Logger logger = LogManager.getLogger("BuildingBricks");

	public static ProxyCommonBuildingBricks proxy;

	static ResourceScanner resScanner;

	public static ItemTrowel trowel;
	public static ItemMaterialBag materialBag;
	public static BlockPortableLadder portableLadder;

	@Subscribe
	public void construction(FMLConstructionEvent event) {
		proxy = new ProxyCommonBuildingBricks();

		Path resourcesDir;
		if (event.getSide() == Side.CLIENT) {
			resScanner = new ResourceScannerClient();
			resourcesDir = Minecraft.getMinecraft().mcDataDir.toPath();
		} else {
			resScanner = new ResourceScannerServer();
			resourcesDir =
					Paths.get(FMLCommonHandler.instance().getSavesDirectory().getAbsoluteFile().getParent());
		}
		resourcesDir = resourcesDir.resolve("config").resolve("BuildingBricks").resolve("resources");
		if (!Files.exists(resourcesDir)) {
			try {
				Files.createDirectories(resourcesDir);
			} catch (IOException e) {
				logger.error("Could not create resources directory", e);
				return;
			}
		}
		resScanner.addModDirectory(resourcesDir);
	}

	@Subscribe
	public void preInit(FMLPreInitializationEvent event) {
		proxy.onPreInitEvent(event);
	}

	@Subscribe
	public void init(FMLInitializationEvent event) {
		proxy.onInitEvent(event);
	}

	@Subscribe
	public void postInit(FMLPostInitializationEvent event) {
		proxy.onPostInitEvent(event);
	}

	@Subscribe
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
