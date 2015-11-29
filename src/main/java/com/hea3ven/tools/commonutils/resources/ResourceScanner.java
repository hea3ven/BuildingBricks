package com.hea3ven.tools.commonutils.resources;

import java.io.InputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.LoaderState;
import net.minecraftforge.fml.common.ModContainer;

public abstract class ResourceScanner {
	protected static final Logger logger = LogManager.getLogger("BuildingBricks.MaterialResourceLoader");

	public abstract Iterable<InputStream> scan(String modid, String name);

	protected static boolean isModLoaded(String name) {
		for (ModContainer mod : Loader.instance().getActiveModList()) {
			if (mod.getModId().toLowerCase().equals(name)) {
				return Loader.instance().getModState(mod) != LoaderState.ModState.DISABLED;
			}
		}
		return false;
	}
}
