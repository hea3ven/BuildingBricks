package com.hea3ven.tools.commonutils.resources;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import net.minecraft.launchwrapper.Launch;

import net.minecraftforge.fml.common.Loader;

public class ResourceScannerServer extends ResourceScanner {

	@Override
	public Iterable<InputStream> scan(String modid, String name) {
		Set<String> resources = Sets.newHashSet();
		for (final URL element : Launch.classLoader.getSources()) {
			if (!element.getProtocol().equals("file"))
				continue;
			try {
				Path elemPath = Paths.get(element.toURI());
				if (Files.isDirectory(elemPath)) {
					resources.addAll(scanDirectory(elemPath, modid, name));
				} else {
					resources.addAll(scanZip(elemPath, modid, name));
				}
			} catch (URISyntaxException e) {
			}
		}
		return Iterables.concat(new ResourceIterable(resources));
	}

	private Set<String> scanDirectory(Path dir, String modid, String name) {
		Path assetsDir = dir.resolve("assets");
		if (!Files.exists(assetsDir))
			return Sets.newHashSet();

		try {
			DirectoryStream<Path> modDirs = Files.newDirectoryStream(assetsDir);
			Set<String> resources = Sets.newHashSet();
			for (Path modDir : modDirs) {
				String modName = modDir.getFileName().toString();
				if (!modName.equals(modid))
					continue;
				Path targetDir = modDir.resolve(name);
				if (!Files.exists(targetDir))
					continue;

				DirectoryStream<Path> modResDirs = Files.newDirectoryStream(targetDir);
				for (Path modResDir : modResDirs) {
					if (!Files.isDirectory(modResDir))
						continue;
					if (!isModLoaded(modResDir.getFileName().toString()))
						continue;
					DirectoryStream<Path> entries = Files.newDirectoryStream(modResDir);
					for (Path entry : entries) {
						if (!entry.getFileName().toString().endsWith(".json"))
							continue;
						resources.add("/" + dir.relativize(entry).toString().replace('\\', '/'));
					}
				}
			}
			return resources;
		} catch (IOException e) {
			return Sets.newHashSet();
		}
	}

	private Set<String> scanZip(Path zip, String modid, String name) {
		Set<String> resources = Sets.newHashSet();
		try (ZipFile jarZip = new ZipFile(zip.toFile())) {
			logger.info(jarZip.getName());
			for (Enumeration<? extends ZipEntry> e = jarZip.entries(); e.hasMoreElements(); ) {
				ZipEntry entry = e.nextElement();
				if (entry.isDirectory())
					continue;
				Path entryPath = Paths.get(entry.getName());
				logger.info(entryPath.toString());
				if (entryPath.getNameCount() < 5)
					continue;
				if (!entryPath.getName(0).getFileName().toString().equals("assets"))
					continue;
				if (!entryPath.getName(1).getFileName().toString().equals(modid))
					continue;
				if (!entryPath.getName(2).getFileName().toString().equals(name))
					continue;
				if (!isModLoaded(entryPath.getName(3).getFileName().toString()))
					continue;
				if (!entryPath.getFileName().toString().endsWith(".json"))
					continue;
				resources.add("/" + entry.getName().replace('\\', '/'));
			}
		} catch (IOException e) {
			logger.error("Could not open the jar file", e);
			return resources;
		}
		return resources;
	}

	private class ResourceIterable implements Iterable<InputStream> {
		private final Set<String> resources;

		public ResourceIterable(Set<String> resources) {
			this.resources = resources;
		}

		@Override
		public Iterator<InputStream> iterator() {
			return new Iterator<InputStream>() {

				private List<String> resources = Lists.newArrayList(ResourceIterable.this.resources);

				@Override
				public boolean hasNext() {
					return resources.size() > 0;
				}

				@Override
				public InputStream next() {
					return ResourceScannerServer.class.getResourceAsStream(resources.remove(0));
				}
			};
		}
	}
}
