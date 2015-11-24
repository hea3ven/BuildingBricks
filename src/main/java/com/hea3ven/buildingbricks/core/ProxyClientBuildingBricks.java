package com.hea3ven.buildingbricks.core;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.common.MinecraftForge;

import com.hea3ven.buildingbricks.core.client.ModelBakerBlockMaterial;
import com.hea3ven.buildingbricks.core.client.ModelBakerItemTrowel;
import com.hea3ven.buildingbricks.core.client.settings.TrowelKeyBindings;
import com.hea3ven.buildingbricks.core.eventhandlers.EventHandlerTrowelOverlay;
import com.hea3ven.buildingbricks.core.materials.MaterialBlockRegistry;

public class ProxyClientBuildingBricks extends ProxyCommonBuildingBricks {

	@Override
	public void preInit() {
		super.preInit();

		ModelBakerBlockMaterial.init();
		ModelBakerItemTrowel.init();
	}

	@Override
	public void init() {
		super.init();

		MinecraftForge.EVENT_BUS.register(new EventHandlerTrowelOverlay());

		TrowelKeyBindings.init();

		ItemModelMesher mesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
		for (Block block : MaterialBlockRegistry.instance.getAllBlocks()) {
			ModelResourceLocation location =
					new ModelResourceLocation((ResourceLocation) Block.blockRegistry.getNameForObject(block),
							"inventory");
			for (int i = 0; i < 1000; i++) {
				mesher.register(Item.getItemFromBlock(block), i, location);
			}
		}
		mesher.register(Item.getItemFromBlock(ModBuildingBricks.portableLadder), 0,
				new ModelResourceLocation(Properties.MODID + ":portable_ladder", "inventory"));
		for (int i = 0; i < 1000; i++) {
			mesher.register(ModBuildingBricks.trowel, i,
					new ModelResourceLocation(Properties.MODID + ":trowel", "inventory"));
		}
	}
}
