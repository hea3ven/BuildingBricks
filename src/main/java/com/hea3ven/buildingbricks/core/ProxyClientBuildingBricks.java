package com.hea3ven.buildingbricks.core;

import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.common.MinecraftForge;

import com.hea3ven.buildingbricks.ModBuildingBricks;
import com.hea3ven.buildingbricks.core.blocks.BlockBuildingBricksBase;
import com.hea3ven.buildingbricks.core.materials.MaterialBlockRegistry;
import com.hea3ven.buildingbricks.core.materials.StructureMaterial;

public class ProxyClientBuildingBricks extends ProxyCommonBuildingBricks {

	@Override
	public void preInit() {
		super.preInit();
		MinecraftForge.EVENT_BUS.register(BakeEventHandler.instance);
	}

	@Override
	public void init() {
		super.init();
		MinecraftForge.EVENT_BUS.register(new KeyInputEventHandler());
		ItemModelMesher mesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
		for (HashMap<StructureMaterial, BlockBuildingBricksBase> blocks : MaterialBlockRegistry.instance.getBlocks().values()) {
			for (Block block : blocks.values()) {
				ModelResourceLocation location = new ModelResourceLocation(
						(ResourceLocation) Block.blockRegistry.getNameForObject(block), "inventory");
				mesher.register(Item.getItemFromBlock(block), 0, location);
			}
		}
		mesher.register(ModBuildingBricks.trowel, 0, new ModelResourceLocation(ModBuildingBricks.MODID+":trowel", "inventory"));
	}
}
