package com.hea3ven.buildingbricks.core.materials.loader;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import net.minecraft.util.ResourceLocation;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.LoaderState;
import net.minecraftforge.fml.common.ModContainer;

import com.hea3ven.buildingbricks.core.materials.*;
import com.hea3ven.tools.commonutils.resources.ResourceScanner;

public class MaterialResourceLoader {
	private static Set<String> domains = new HashSet<>();

	public static void loadResources(ResourceScanner scanner) {
		for (ResourceLocation materialResLoc : scanner.scan("materials")) {
			if (domains.contains(materialResLoc.getResourceDomain()) ||
					isModLoaded(materialResLoc.getResourceDomain())) {
				try {
					for (InputStream matStream : scanner.getAllResources(materialResLoc)) {
						try (InputStream stream = matStream) {
							MaterialParser.loadMaterialFromStream(stream);
						}
					}
				} catch (Exception e) {
					throw new RuntimeException("Could not load material from " + materialResLoc, e);
				}
			}
		}
		for (MaterialBuilderSimple builder : MaterialParser.materials.values()) {
			Material mat = builder.build();
			for (MaterialBlockType blockType : mat.getStructureMaterial().getBlockTypes()) {
				if (mat.getBlock(blockType) == null) {
					mat.addBlock(new BlockDescription(blockType,
							MaterialBlockRecipes.getForType(mat.getStructureMaterial(), blockType)));
				}
			}
			MaterialRegistry.registerMaterial(mat);
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
