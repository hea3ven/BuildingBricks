package com.hea3ven.buildingbricks.core.materials;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.google.common.base.Throwables;
import com.google.gson.JsonSyntaxException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.AbstractResourcePack;
import net.minecraft.client.resources.FallbackResourceManager;
import net.minecraft.client.resources.FileResourcePack;
import net.minecraft.client.resources.FolderResourcePack;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraft.util.ResourceLocation;

public class MaterialResourceLoaderClient extends MaterialResourceLoader {

	public static void discoverMaterialsClient() {
		Set<ResourceLocation> res = null;
		IResourceManager resourceManager = Minecraft.getMinecraft().getResourceManager();

		res = new HashSet<ResourceLocation>();
		for (IResourceManager mgr : getDomainResourceManagers(resourceManager)) {
			for (IResourcePack resPack : getResourcePackages(mgr)) {
				res.addAll(getMaterials(resPack));
			}
		}

		for (ResourceLocation r : res) {
			InputStream matStream = null;
			try {
				matStream = resourceManager.getResource(r).getInputStream();
				loadMaterialFromStream(matStream);

			} catch (IOException e) {
				Throwables.propagate(e);
			} catch (JsonSyntaxException e) {
				throw new MaterialLoadingException("Could not load material " + r, e);
			} finally {
				IOUtils.closeQuietly(matStream);
			}
		}
	}

	private static List<IResourcePack> getResourcePackages(IResourceManager mgr) {
		return MaterialResourceLoader.<List<IResourcePack>> getField(mgr,
				FallbackResourceManager.class, "field_110540_a", "resourcePacks");
	}

	private static Collection<IResourceManager> getDomainResourceManagers(
			IResourceManager resourceManager) {
		return MaterialResourceLoader
				.<Map<String, IResourceManager>> getField(resourceManager,
						SimpleReloadableResourceManager.class, "field_110548_a",
						"domainResourceManagers")
				.values();
	}

	private static final Pattern materialPattern = Pattern
			.compile("assets/([^/]*)/(materials/[^.]*.json)");

	private static Set<ResourceLocation> getMaterials(IResourcePack resPack) {
		Set<ResourceLocation> materials = new HashSet<ResourceLocation>();
		if (resPack instanceof FolderResourcePack) {
			File rootDir = getField(resPack, AbstractResourcePack.class, "field_110597_b",
					"resourcePackFile");
			for (File subPath : new File(rootDir, "assets").listFiles()) {
				File materialsDir = new File(subPath, "materials");
				File[] materialFiles = materialsDir
						.listFiles((FileFilter) new WildcardFileFilter("*.json"));
				if (materialFiles != null) {
					for (File materialFile : materialFiles) {
						materials.add(new ResourceLocation(subPath.getName(),
								"materials/" + materialFile.getName()));
					}
				}
			}
		} else if (resPack instanceof FileResourcePack) {
			ZipFile packFile = getZipFromResPack(resPack);
			for (Enumeration<? extends ZipEntry> entries = packFile.entries(); entries
					.hasMoreElements();) {
				ZipEntry entry = entries.nextElement();
				Matcher match = materialPattern.matcher(entry.getName());
				if (match.matches()) {
					materials.add(new ResourceLocation(match.group(1), match.group(2)));
				}
			}
		}
		return materials;
	}

	private static ZipFile getZipFromResPack(IResourcePack resPack) {
		try {
			Method mthd = null;
			try {
				mthd = FileResourcePack.class.getDeclaredMethod("func_110599_c", new Class<?>[0]);
			} catch (NoSuchMethodException e) {
				mthd = FileResourcePack.class.getDeclaredMethod("getResourcePackZipFile",
						new Class<?>[0]);
			}
			mthd.setAccessible(true);
			return (ZipFile) mthd.invoke(resPack, new Object[0]);
		} catch (Exception e) {
			Throwables.propagate(e);
			return null;
		}
	}

}
