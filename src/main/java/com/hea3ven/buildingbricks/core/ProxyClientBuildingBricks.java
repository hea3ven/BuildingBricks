package com.hea3ven.buildingbricks.core;

import javax.vecmath.Vector3f;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

import com.hea3ven.buildingbricks.core.client.ModelBakerBlockMaterial;
import com.hea3ven.buildingbricks.core.client.ModelBakerItemMaterial;
import com.hea3ven.buildingbricks.core.eventhandlers.EventHandlerTrowelOverlay;

public class ProxyClientBuildingBricks extends ProxyCommonBuildingBricks {

	public ProxyClientBuildingBricks() {
		super();

		addModelBaker(new ModelBakerBlockMaterial());
		addModelBaker(
				new ModelBakerItemMaterial("buildingbricks:item/trowel", "buildingbricks:trowel#inventory",
						new Vector3f(0.3f, 0.0625f, 0.125f), new Vector3f(0.4f, 0.4f, 0.4f)));
		addModelBaker(new ModelBakerItemMaterial("buildingbricks:item/material_bag",
				"buildingbricks:material_bag#inventory", new Vector3f(0.25f, 0.3f, 0.4375f /* 0.125f*/),
				new Vector3f(0.5f, 0.5f, 0.125001f)));
	}

	@Override
	public void onInitEvent(FMLInitializationEvent event) {
		super.onInitEvent(event);

		MinecraftForge.EVENT_BUS.register(new EventHandlerTrowelOverlay());
	}
}
