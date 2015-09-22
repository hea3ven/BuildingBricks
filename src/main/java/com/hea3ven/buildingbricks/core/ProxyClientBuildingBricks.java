package com.hea3ven.buildingbricks.core;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;

import com.hea3ven.buildingbricks.ModBuildingBricks;
import com.hea3ven.buildingbricks.core.blocks.rendering.ItemBlockMaterialRenderer;
import com.hea3ven.buildingbricks.core.items.rendering.ItemRendererTrowel;
import com.hea3ven.buildingbricks.core.materials.MaterialBlockRegistry;

public class ProxyClientBuildingBricks extends ProxyCommonBuildingBricks {

	@Override
	public void preInit() {
		super.preInit();
	}

	@Override
	public void init() {
		super.init();
		MinecraftForge.EVENT_BUS.register(new KeyInputEventHandler());

		MinecraftForgeClient.registerItemRenderer(ModBuildingBricks.trowel, new ItemRendererTrowel());
		ItemBlockMaterialRenderer renderer = new ItemBlockMaterialRenderer();
		for (Block block : MaterialBlockRegistry.instance.getBlocks().values()) {
			MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(block), renderer);
		}
	}
}
