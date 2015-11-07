package com.hea3ven.tools.commonutils.resources;

import java.io.InputStream;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.LoaderState;
import net.minecraftforge.fml.common.ModContainer;

public abstract class ResourceScanner {
	public abstract Iterable<InputStream> scan(String name);

	protected static boolean isModLoaded(String name) {
		for (ModContainer mod : Loader.instance().getActiveModList()) {
			if (mod.getModId().toLowerCase().equals(name)) {
				return Loader.instance().getModState(mod) != LoaderState.ModState.DISABLED;
			}
		}
		return false;
	}
}
