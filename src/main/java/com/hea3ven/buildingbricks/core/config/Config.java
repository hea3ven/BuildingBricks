package com.hea3ven.buildingbricks.core.config;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Throwables;

import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.config.IConfigElement;

import com.hea3ven.buildingbricks.core.materials.mapping.IdMappingLoader;

public class Config {

	public static boolean generateGrassSlabs;

	private static Config INSTANCE;

	public static void init(File configDir) {
		Path modConfigDir = Paths.get(configDir.toURI()).resolve("BuildingBricks");

		// Update from 1.0.x format
		Path oldConfig = Paths.get(configDir.toURI()).resolve("buildingbricks.cfg");
		if (Files.exists(oldConfig)) {
			try {
				Files.createDirectories(modConfigDir);
				Files.move(oldConfig, modConfigDir.resolve("general.cfg"));
			} catch (IOException e) {
				Throwables.propagate(e);
			}
		}

		INSTANCE = new Config(modConfigDir);
		INSTANCE.reload();
		IdMappingLoader.init(modConfigDir.resolve("material_ids.nbt"));
	}

	private Configuration conf;
	private Property generateGrassSlabsProp;

	private Config(Path configDir) {
		conf = new Configuration(configDir.resolve("general.cfg").toFile());

		conf.getCategory("world").setLanguageKey("buildingbricks.config.world.cat");

		generateGrassSlabsProp = conf.get("world", "generateGrassSlabs", true,
				"Enable to generate grass slabs in the world to smooth out the surface")
				.setLanguageKey("buildingbricks.config.world.generateGrassSlabs")
				.setRequiresMcRestart(true);
	}

	private void reload() {
		if (conf.hasChanged())
			conf.save();

		Config.generateGrassSlabs = generateGrassSlabsProp.getBoolean();
	}

	public static List<IConfigElement> getConfigElements() {
		List<IConfigElement> elems = new ArrayList<>();
		elems.add(new ConfigElement(INSTANCE.conf.getCategory("world")));
		return elems;
	}
}
