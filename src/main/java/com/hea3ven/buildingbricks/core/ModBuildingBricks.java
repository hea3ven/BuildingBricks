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
import net.minecraftforge.fml.relauncher.SideOnly;

import com.hea3ven.buildingbricks.compat.vanilla.ProxyModBuildingBricksCompatVanilla;
import com.hea3ven.buildingbricks.core.block.BlockPortableLadder;
import com.hea3ven.buildingbricks.core.item.ItemMaterialBag;
import com.hea3ven.buildingbricks.core.item.ItemTrowel;
import com.hea3ven.tools.commonutils.resources.ResourceScanner;
import com.hea3ven.tools.commonutils.resources.ResourceScannerClient;
import com.hea3ven.tools.commonutils.resources.ResourceScannerServer;
import com.hea3ven.tools.commonutils.util.SidedCall;
import com.hea3ven.tools.commonutils.util.SidedCall.SidedRunnable;

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

		final Path[] gameDir = new Path[1];
		SidedCall.run(Side.CLIENT, new SidedRunnable() {
			@Override
			@SideOnly(Side.CLIENT)
			public void run() {
				resScanner = new ResourceScannerClient();
				gameDir[0] = Minecraft.getMinecraft().mcDataDir.toPath();
			}
		});
		SidedCall.run(Side.SERVER, new SidedRunnable() {
			@Override
			@SideOnly(Side.SERVER)
			public void run() {
				resScanner = new ResourceScannerServer();
				gameDir[0] =
						Paths.get(FMLCommonHandler.instance()
								.getSavesDirectory()
								.getAbsoluteFile()
								.getParent());
			}
		});
		Path resourcesDir = gameDir[0].resolve("config").resolve("BuildingBricks").resolve("resources");
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
