package com.hea3ven.tools.commonutils.resources;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import org.apache.commons.io.filefilter.WildcardFileFilter;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ResourceScannerClient implements ResourceScanner {
	@Override
	public Iterable<InputStream> scan(String name) {
		Set<ResourceLocation> res = null;
		IResourceManager resourceManager = Minecraft.getMinecraft().getResourceManager();

		res = new HashSet<ResourceLocation>();
		for (IResourceManager mgr : getDomainResourceManagers(resourceManager).values()) {
			for (IResourcePack resPack : getResourcePackages(mgr)) {
				res.addAll(getMaterials(resPack, name));
			}
		}
		return new ResourceLocationIterable(res);
	}

	private static List<IResourcePack> getResourcePackages(IResourceManager mgr) {
		return ReflectionHelper.getPrivateValue(FallbackResourceManager.class, (FallbackResourceManager) mgr,
				"field_110540_a", "resourcePacks");
	}

	private static Map<String, IResourceManager> getDomainResourceManagers(IResourceManager resourceManager) {
		return ReflectionHelper.getPrivateValue(SimpleReloadableResourceManager.class,
				(SimpleReloadableResourceManager) resourceManager, "field_110548_a",
				"domainResourceManagers");
	}

	private static Set<ResourceLocation> getMaterials(IResourcePack resPack, String name) {
		Set<ResourceLocation> materials = new HashSet<ResourceLocation>();
		if (resPack instanceof FolderResourcePack) {
			File rootDir = ReflectionHelper.getPrivateValue(AbstractResourcePack.class,
					(AbstractResourcePack) resPack, "field_110597_b", "resourcePackFile");
			try {
				DirectoryStream<Path> modDirs =
						Files.newDirectoryStream(Paths.get(rootDir.toString(), "assets"));
				for (Path modDir : modDirs) {
					String modName = modDir.getFileName().toString();
					if (!Loader.isModLoaded(modName))
						continue;
					Path targetDir = modDir.resolve(name);
					if (!Files.exists(targetDir))
						continue;

					DirectoryStream<Path> entries = Files.newDirectoryStream(targetDir);
					for (Path entry : entries) {
						if (!entry.getFileName().toString().endsWith(".json"))
							continue;
						materials.add(
								new ResourceLocation(modName, name + "/" + entry.getFileName().toString()));
					}
				}
			} catch (IOException e) {
				return materials;
			}
		} else if (resPack instanceof FileResourcePack) {
			ZipFile packFile = getZipFromResPack((FileResourcePack) resPack);
			for (Enumeration<? extends ZipEntry> entries = packFile.entries(); entries.hasMoreElements(); ) {
				ZipEntry entry = entries.nextElement();
				Path entryPath = Paths.get(entry.getName());
				if (!entryPath.getName(0).getFileName().equals("assets"))
					continue;
				if (!Loader.isModLoaded(entryPath.getName(1).getFileName().toString()))
					continue;
				if (!entryPath.getName(2).getFileName().equals(name))
					continue;
				if (!entryPath.getFileName().toString().endsWith(".json"))
					continue;

				materials.add(new ResourceLocation(entryPath.getName(1).getFileName().toString(),
						entryPath.getName(3).getFileName().toString()));
			}
		}
		return materials;
	}

	private static ZipFile getZipFromResPack(FileResourcePack resPack) {
		Method mthd = ReflectionHelper.findMethod(FileResourcePack.class, resPack,
				new String[] {"getResourcePackZipFile", "func_110599_c"}, new Class<?>[0]);
		try {
			return (ZipFile) mthd.invoke(resPack, new Object[0]);
		} catch (Exception e) {
			Throwables.propagate(e);
			return null;
		}
	}

	private class ResourceLocationIterable implements Iterable<InputStream> {
		private final Set<ResourceLocation> resources;

		public ResourceLocationIterable(Set<ResourceLocation> resources) {
			this.resources = resources;
		}

		@Override
		public Iterator<InputStream> iterator() {

			return new Iterator<InputStream>() {
				private List<ResourceLocation> resources =
						Lists.newArrayList(ResourceLocationIterable.this.resources);

				@Override
				public boolean hasNext() {
					return resources.size() > 0;
				}

				@Override
				public InputStream next() {
					IResourceManager resourceManager = Minecraft.getMinecraft().getResourceManager();
					try {
						return resourceManager.getResource(resources.remove(0)).getInputStream();
					} catch (IOException e) {
						Throwables.propagate(e);
						return null;
					}
				}
			};
		}
	}
}
