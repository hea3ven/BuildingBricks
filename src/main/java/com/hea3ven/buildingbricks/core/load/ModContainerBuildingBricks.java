package com.hea3ven.buildingbricks.core.load;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;

import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.versioning.ArtifactVersion;
import net.minecraftforge.fml.common.versioning.VersionParser;

import com.hea3ven.buildingbricks.core.ModBuildingBricks;

public class ModContainerBuildingBricks extends DummyModContainer {
	private final ModBuildingBricks instance;
	private final File source;

	public ModContainerBuildingBricks() {
		super(new ModMetadata());

		getMetadata().modId = ModBuildingBricks.MODID;
		getMetadata().name = "Building Bricks";
		getMetadata().description = "A mod about building blocks and building tools.";
		getMetadata().version = ModBuildingBricks.VERSION;
		getMetadata().url = "https://github.com/hea3ven/BuildingBricks";
		getMetadata().authorList = Lists.newArrayList("Hea3veN");
		getMetadata().updateJSON =
				"https://raw.githubusercontent.com/hea3ven/BuildingBricks/master/media/update.json";

		instance = new ModBuildingBricks();

		URL sourceUrl = ModContainerBuildingBricks.class.getProtectionDomain().getCodeSource().getLocation();
		if ("file".equals(sourceUrl.getProtocol()))
			source = new File(sourceUrl.getFile()
					.replace("com/hea3ven/buildingbricks/core/load/ModContainerBuildingBricks.class", ""));
		else if ("jar".equals(sourceUrl.getProtocol())) {
			try {
				String fileUrl = sourceUrl.getFile().replace("file:", "").replaceFirst("!.*$", "");
				source = new File(URLDecoder.decode(fileUrl, "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			}
		} else
			source = null;
	}

	@Override
	public Set<ArtifactVersion> getRequirements() {
		return Sets.newHashSet(
				VersionParser.parseVersionReference(ModBuildingBricks.FORGE_DEPENDENCY));
	}

	@Override
	public List<ArtifactVersion> getDependencies() {
		return Lists.newArrayList(
				VersionParser.parseVersionReference(ModBuildingBricks.FORGE_DEPENDENCY));
	}

	@Override
	public File getSource() {
		return source;
	}

	@Override
	public String getGuiClassName() {
		return "com.hea3ven.buildingbricks.core.config.BuildingBricksConfigGuiFactory";
	}

	@Override
	public Class<?> getCustomResourcePackClass() {
		try {
			return Class.forName("net.minecraftforge.fml.client.FMLFileResourcePack", true,
					getClass().getClassLoader());
		} catch (ClassNotFoundException e) {
			return null;
		}
	}

	@Override
	public URL getUpdateUrl() {
		try {
			return new URL(getMetadata().updateJSON);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean registerBus(EventBus bus, LoadController controller) {
		bus.register(instance);
		return true;
	}
}
