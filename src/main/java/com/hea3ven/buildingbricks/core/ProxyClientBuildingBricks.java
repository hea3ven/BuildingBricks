package com.hea3ven.buildingbricks.core;

import net.minecraftforge.common.MinecraftForge;

public class ProxyClientBuildingBricks extends ProxyCommonBuildingBricks {

	@Override
	public void preInit() {
		super.preInit();
	}

	@Override
	public void init() {
		super.init();
		MinecraftForge.EVENT_BUS.register(new KeyInputEventHandler());
	}
}
