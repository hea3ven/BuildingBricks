package com.hea3ven.buildingbricks.core.materials;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.google.common.base.Throwables;

public class MaterialResourceLoaderServer extends MaterialResourceLoader {

	public static void discoverMaterialsServer() {
		// TODO: rewrite this

		URL jarUrl = MaterialResourceLoader.class
				.getProtectionDomain()
				.getCodeSource()
				.getLocation();
		Set<String> matResources = new HashSet<String>();
		if (jarUrl.getProtocol().equals("jar")) {
			String jarFilePath = null;
			try {
				jarFilePath = Paths.get(new URI(jarUrl.getPath())).toString();
			} catch (URISyntaxException e) {
				Throwables.propagate(e);
			}
			if (jarFilePath.lastIndexOf('!') != -1)
				jarFilePath = jarFilePath.substring(0, jarFilePath.lastIndexOf('!'));

			ZipFile jarZip = null;
			try {
				jarZip = new ZipFile(jarFilePath);
			} catch (IOException e) {
				return;
			}
			try {
				for (Enumeration<? extends ZipEntry> e = jarZip.entries(); e.hasMoreElements();) {
					ZipEntry entry = e.nextElement();
					if (entry
							.getName()
							.startsWith("/assets/buildingbrickscompatvanilla/materials/"))
						matResources.add(entry.getName());
				}
			} finally {
				try {
					jarZip.close();
				} catch (IOException e) {
					return;
				}
			}
		} else if (jarUrl.getProtocol().equals("file")) {
			Path matsPath = Paths
					.get(jarUrl.getPath().replace(
							"com/hea3ven/buildingbricks/core/materials/MaterialResourceLoader.class",
							""))
					.resolve("assets")
					.resolve("buildingbrickscompatvanilla")
					.resolve("materials");
			try {
				DirectoryStream<Path> ds = Files.newDirectoryStream(matsPath);
				for (Path entry : ds) {
					matResources.add("/assets/buildingbrickscompatvanilla/materials/"
							+ matsPath.relativize(entry));
				}
			} catch (IOException e) {
				return;
			}
		}
		for (String matRes : matResources) {
			loadMaterialFromStream(MaterialResourceLoader.class.getResourceAsStream(matRes));
		}
	}

}
