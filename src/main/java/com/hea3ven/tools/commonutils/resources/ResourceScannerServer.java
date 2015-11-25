package com.hea3ven.tools.commonutils.resources;

import java.io.IOException;
import java.io.InputStream;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ResourceScannerServer extends ResourceScanner {

	private static final Logger logger = LogManager.getLogger("BuildingBricks.MaterialResourceLoader");

	@Override
	public Iterable<InputStream> scan(String modid, String name) {
		Set<String> resources = Sets.newHashSet();
		final String[] classPathParts = System.getProperty("java.class.path", ".").split(":");
		for (final String element : classPathParts) {
			Path elemPath = Paths.get(element);
			if (Files.isDirectory(elemPath)) {
				resources.addAll(scanDirectory(elemPath, name));
			} else {
				resources.addAll(scanZip(elemPath, name));
			}
		}
		return Iterables.concat(new ResourceIterable(resources));
	}

	private Set<String> scanDirectory(Path dir, String name) {
		Path assetsDir = dir.resolve("assets");
		if (!Files.exists(assetsDir))
			return Sets.newHashSet();

		try {
			DirectoryStream<Path> modDirs = Files.newDirectoryStream(assetsDir);
			Set<String> resources = Sets.newHashSet();
			for (Path modDir : modDirs) {
				if (!isModLoaded(modDir.getFileName().toString()))
					continue;
				Path targetDir = modDir.resolve(name);
				if (!Files.exists(targetDir))
					continue;

				DirectoryStream<Path> entries = Files.newDirectoryStream(targetDir);
				for (Path entry : entries) {
					if (!entry.getFileName().toString().endsWith(".json"))
						continue;
					resources.add("/" + dir.relativize(entry).toString());
				}
			}
			return resources;
		} catch (IOException e) {
			return Sets.newHashSet();
		}
	}

	private Set<String> scanZip(Path zip, String name) {
		Set<String> resources = Sets.newHashSet();
		try (ZipFile jarZip = new ZipFile(zip.toFile())) {
			for (Enumeration<? extends ZipEntry> e = jarZip.entries(); e.hasMoreElements(); ) {
				ZipEntry entry = e.nextElement();
				if (entry.isDirectory())
					continue;
				Path entryPath = Paths.get(entry.getName());
				if (!entryPath.getName(0).getFileName().equals("assets"))
					continue;
				if (!isModLoaded(entryPath.getName(1).getFileName().toString()))
					continue;
				if (!entryPath.getName(2).getFileName().equals(name))
					continue;
				if (!entryPath.endsWith(".json"))
					continue;
				resources.add("/" + entry.getName());
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
