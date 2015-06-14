package com.hea3ven.buildingbricks.core;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import com.hea3ven.buildingbricks.ModBuildingBricks;

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
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(ModBuildingBricks.andesiteSlab), 0, new ModelResourceLocation(ModBuildingBricks.MODID+":andesite_slab", "inventory"));
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(ModBuildingBricks.redSandstoneSlab), 0, new ModelResourceLocation(ModBuildingBricks.MODID+":red_sandstone_slab", "inventory"));
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(ModBuildingBricks.andesiteStep), 0, new ModelResourceLocation(ModBuildingBricks.MODID+":andesite_step", "inventory"));
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(ModBuildingBricks.redSandstoneStep), 0, new ModelResourceLocation(ModBuildingBricks.MODID+":red_sandstone_step", "inventory"));
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(ModBuildingBricks.andesiteCorner), 0, new ModelResourceLocation(ModBuildingBricks.MODID+":andesite_corner", "inventory"));
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(ModBuildingBricks.redSandstoneCorner), 0, new ModelResourceLocation(ModBuildingBricks.MODID+":red_sandstone_corner", "inventory"));
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(ModBuildingBricks.trowel, 0, new ModelResourceLocation(ModBuildingBricks.MODID+":trowel", "inventory"));
	}
}
