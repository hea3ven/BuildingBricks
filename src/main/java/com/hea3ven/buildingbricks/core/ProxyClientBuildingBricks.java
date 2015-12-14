package com.hea3ven.buildingbricks.core;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.client.model.ModelLoader;
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

		for (final Block block : MaterialBlockRegistry.instance.getAllBlocks()) {
			ModelLoader.setCustomMeshDefinition(Item.getItemFromBlock(block), new ItemMeshDefinition() {
				@Override
				public ModelResourceLocation getModelLocation(ItemStack stack) {
					return new ModelResourceLocation(Block.blockRegistry.getNameForObject(block),
							"inventory");
				}
			});
		}
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBuildingBricks.portableLadder), 0,
				new ModelResourceLocation(Properties.MODID + ":portable_ladder", "inventory"));
		ModelLoader.setCustomMeshDefinition(ModBuildingBricks.trowel, new ItemMeshDefinition() {
			@Override
			public ModelResourceLocation getModelLocation(ItemStack stack) {
				return new ModelResourceLocation(Properties.MODID + ":trowel", "inventory");
			}
		});
	}

	@Override
	public void init() {
		super.init();

		MinecraftForge.EVENT_BUS.register(new EventHandlerTrowelOverlay());

		TrowelKeyBindings.init();
	}
}
