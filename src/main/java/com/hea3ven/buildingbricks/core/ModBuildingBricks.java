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
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.event.FMLMissingMappingsEvent.MissingMapping;
import net.minecraftforge.fml.common.registry.GameRegistry.Type;
import net.minecraftforge.fml.relauncher.Side;

import com.hea3ven.buildingbricks.compat.vanilla.ProxyModBuildingBricksCompatVanilla;
import com.hea3ven.buildingbricks.core.blocks.BlockPortableLadder;
import com.hea3ven.buildingbricks.core.items.ItemMaterialBag;
import com.hea3ven.buildingbricks.core.items.ItemTrowel;
import com.hea3ven.buildingbricks.core.materials.mapping.IdMappingLoader;
import com.hea3ven.tools.bootstrap.Bootstrap;
import com.hea3ven.tools.commonutils.resources.ResourceScanner;

@Mod(modid = ModBuildingBricks.MODID, name = "Building Bricks", version = ModBuildingBricks.VERSION,
		dependencies = ModBuildingBricks.DEPENDENCIES,
		guiFactory = "com.hea3ven.buildingbricks.core.config.BuildingBricksConfigGuiFactory",
		updateJSON = "https://raw.githubusercontent.com/hea3ven/BuildingBricks/master/media/update.json")
public class ModBuildingBricks {

	static {
		Bootstrap.init();
	}

	public static final String MODID = "buildingbricks";
	public static final String VERSION = "PROJECTVERSION";
	public static final String DEPENDENCIES = "required-after:Forge@[FORGEVERSION,)";

	public static final Logger logger = LogManager.getLogger("BuildingBricks");

	public static ProxyCommonBuildingBricks proxy = new ProxyCommonBuildingBricks();

	@SidedProxy(serverSide = "com.hea3ven.tools.commonutils.resources.ResourceScannerServer",
			clientSide = "com.hea3ven.tools.commonutils.resources.ResourceScannerClient")
	static ResourceScanner resScanner;

	public static ItemTrowel trowel;
	public static ItemMaterialBag materialBag;
	public static BlockPortableLadder portableLadder;

	@EventHandler
	public void construction(FMLConstructionEvent event) {
		Path resourcesDir;
		if (event.getSide() == Side.CLIENT)
			resourcesDir = Minecraft.getMinecraft().mcDataDir.toPath();
		else
			resourcesDir =
					Paths.get(FMLCommonHandler.instance().getSavesDirectory().getAbsoluteFile().getParent());
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

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		proxy.onPreInitEvent(event);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		IdMappingLoader.save();

		proxy.onInitEvent(event);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.onPostInitEvent(event);
	}

	@Mod.EventHandler
	public void onRemap(FMLMissingMappingsEvent event) {
		for (MissingMapping missingMapping : event.getAll()) {
			if (missingMapping.name.toString().equals("buildingbrickscompatvanilla:grass_slab")) {
				if (missingMapping.type == Type.BLOCK)
					missingMapping.remap(ProxyModBuildingBricksCompatVanilla.grassSlab);
				else
					missingMapping.remap(
							Item.getItemFromBlock(ProxyModBuildingBricksCompatVanilla.grassSlab));
			}
		}
	}
}
