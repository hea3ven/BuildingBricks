package com.hea3ven.tools.commonutils.resources;

import java.io.File;
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
import com.google.common.collect.Sets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.*;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ResourceScannerClient extends ResourceScanner {
	@Override
	public Iterable<InputStream> scan(String modid, String name) {
		IResourceManager resourceManager = Minecraft.getMinecraft().getResourceManager();

		Set<ResourceLocation> res = new HashSet<>();
		for (IResourceManager mgr : getDomainResourceManagers(resourceManager).values()) {
			for (IResourcePack resPack : getResourcePackages(mgr)) {
				res.addAll(getMaterials(resPack, modid, name));
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

	private static Set<ResourceLocation> getMaterials(IResourcePack resPack, String modid, String name) {
		if (resPack instanceof FolderResourcePack) {
			return getResourcesFromDir((FolderResourcePack) resPack, modid, name);
		} else if (resPack instanceof FileResourcePack) {
			return getResourcesFromZip((FileResourcePack) resPack, modid, name);
		} else
			return Sets.newHashSet();
	}

	private static Set<ResourceLocation> getResourcesFromDir(FolderResourcePack resPack, String modid,
			String name) {
		Set<ResourceLocation> materials = new HashSet<>();
		File rootDir = ReflectionHelper.getPrivateValue(AbstractResourcePack.class, resPack, "field_110597_b",
				"resourcePackFile");
		try {
			DirectoryStream<Path> modDirs = Files.newDirectoryStream(Paths.get(rootDir.toString(), "assets"));
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
						materials.add(new ResourceLocation(modName, modDir.relativize(entry).toString()));
					}
				}
			}
			return materials;
		} catch (IOException e) {
			return materials;
		}
	}

	private static Set<ResourceLocation> getResourcesFromZip(FileResourcePack resPack, String modid,
			String name) {
		Set<ResourceLocation> materials = new HashSet<ResourceLocation>();
		ZipFile packFile = getZipFromResPack(resPack);
		for (Enumeration<? extends ZipEntry> entries = packFile.entries(); entries.hasMoreElements(); ) {
			ZipEntry entry = entries.nextElement();
			Path entryPath = Paths.get(entry.getName());
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

			materials.add(
					new ResourceLocation(modid, Paths.get("assets", modid).relativize(entryPath).toString()));
		}
		return materials;
	}

	private static ZipFile getZipFromResPack(FileResourcePack resPack) {
		Method mthd = ReflectionHelper.findMethod(FileResourcePack.class, resPack,
				new String[] {"getResourcePackZipFile", "func_110599_c"});
		try {
			return (ZipFile) mthd.invoke(resPack);
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
