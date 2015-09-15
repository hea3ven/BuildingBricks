package com.hea3ven.buildingbricks.core.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.client.config.IConfigElement;

import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class Config {

	public static boolean generateGrassSlabs;

	private static Config INSTANCE;

	public static void init(File configDir) {
		INSTANCE = new Config(configDir);
		INSTANCE.reload();
	}

	private Configuration conf;
	private Property generateGrassSlabsProp;

	private Config(File configDir) {
		conf = new Configuration(new File(configDir, "buildingbricks.cfg"));

		conf.getCategory("world").setLanguageKey("buildingbricks.config.world.cat");

		generateGrassSlabsProp = conf
				.get("world", "generateGrassSlabs", true,
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
		List<IConfigElement> elems = new ArrayList<IConfigElement>();
		elems.add(new ConfigElement(INSTANCE.conf.getCategory("world")));
		return elems;
	}
}
