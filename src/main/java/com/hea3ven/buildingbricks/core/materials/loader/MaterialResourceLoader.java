package com.hea3ven.buildingbricks.core.materials.loader;

import java.io.InputStream;

import org.apache.commons.io.IOUtils;

import com.hea3ven.tools.commonutils.resources.ResourceScanner;

public class MaterialResourceLoader {
	public static void loadResources(ResourceScanner scanner, String modid) {
		for (InputStream matStream : scanner.scan(modid, "materials")) {
			MaterialParser.loadMaterialFromStream(matStream);
			IOUtils.closeQuietly(matStream);
		}
	}
}
