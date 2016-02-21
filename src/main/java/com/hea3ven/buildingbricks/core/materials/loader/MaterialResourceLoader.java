package com.hea3ven.buildingbricks.core.materials.loader;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import com.google.common.base.Throwables;

import net.minecraft.util.ResourceLocation;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.LoaderState;
import net.minecraftforge.fml.common.ModContainer;

import com.hea3ven.tools.commonutils.resources.ResourceScanner;

public class MaterialResourceLoader {
	private static Set<String> domains = new HashSet<>();

	public static void loadResources(ResourceScanner scanner) {
		for (ResourceLocation materialResLoc : scanner.scan("materials")) {
			if (domains.contains(materialResLoc.getResourceDomain()) ||
					isModLoaded(materialResLoc.getResourceDomain())) {
				try (InputStream matStream = scanner.getResource(materialResLoc)) {
					MaterialParser.loadMaterialFromStream(matStream);
				} catch (IOException e) {
					Throwables.propagate(e);
				}
			}
		}
	}

	public static void addDomain(String domain) {
		domains.add(domain);
	}

	protected static boolean isModLoaded(String name) {
		for (ModContainer mod : Loader.instance().getActiveModList()) {
			if (mod.getModId().toLowerCase().equals(name)) {
				return Loader.instance().getModState(mod) != LoaderState.ModState.DISABLED;
			}
		}
		return false;
	}
}
