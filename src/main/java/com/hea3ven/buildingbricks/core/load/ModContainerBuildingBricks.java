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
	public ModContainerBuildingBricks() {
		super(new ModMetadata());

		getMetadata().modId = ModBuildingBricks.MODID + "hooks";
		getMetadata().name = "Building Bricks Hooks";
		getMetadata().description = "A mod about building blocks and building tools (internal hooks).";
		getMetadata().version = ModBuildingBricks.VERSION;
		getMetadata().url = "https://github.com/hea3ven/BuildingBricks";
		getMetadata().authorList = Lists.newArrayList("Hea3veN");
		getMetadata().parent = ModBuildingBricks.MODID;
	}

	@Override
	public boolean registerBus(EventBus bus, LoadController controller) {
		return true;
	}
}
