package com.hea3ven.buildingbricks.core.materials.loader;

import java.io.IOException;
import java.io.InputStream;

import com.google.common.base.Throwables;

import net.minecraft.util.ResourceLocation;

import com.hea3ven.tools.commonutils.resources.ResourceScanner;

public class MaterialResourceLoader {
	public static void loadResources(ResourceScanner scanner, String modid) {
		for (ResourceLocation materialResLoc : scanner.scan(modid, "materials")) {
			try (InputStream matStream = scanner.getResource(materialResLoc)) {
				MaterialParser.loadMaterialFromStream(matStream);
			} catch (IOException e) {
				Throwables.propagate(e);
			}
		}
	}
}
