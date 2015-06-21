package com.hea3ven.buildingbricks.core;

import net.minecraftforge.common.MinecraftForge;

import com.hea3ven.buildingbricks.core.eventhandlers.EventHandlerOverrideBlockPlacing;
import com.hea3ven.buildingbricks.core.eventhandlers.EventHandlerTrowelOverlay;

public class ProxyCommonBuildingBricks {

	public void preInit() {
	}

	public void init() {
		MinecraftForge.EVENT_BUS.register(new EventHandlerOverrideBlockPlacing());
		MinecraftForge.EVENT_BUS.register(new EventHandlerTrowelOverlay());
	}

}
