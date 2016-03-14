package com.hea3ven.buildingbricks.compat.vanilla;

import net.minecraft.item.Item;

import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.hea3ven.buildingbricks.compat.vanilla.items.ItemBlockGrassSlab;
import com.hea3ven.buildingbricks.core.ModBuildingBricks;
import com.hea3ven.tools.commonutils.client.renderer.SimpleItemMeshDefinition;
import com.hea3ven.tools.commonutils.util.SidedCall;
import com.hea3ven.tools.commonutils.util.SidedCall.SidedRunnable;

@Mod(modid = "buildingbrickscompatvanilla", version = ModBuildingBricks.VERSION,
		dependencies = "required-before:buildingbricks;" + ModBuildingBricks.DEPENDENCIES)
public class ModBuildingBricksCompatVanilla {
	@Mod.EventHandler
	public void onPreInitEvent(FMLPreInitializationEvent event) {
		GameRegistry.registerBlock(ProxyModBuildingBricksCompatVanilla.grassSlab, ItemBlockGrassSlab.class,
				"grass_slab");

		SidedCall.run(Side.CLIENT, new SidedRunnable() {
			@Override
			@SideOnly(Side.CLIENT)
			public void run() {
				ModelLoader.setCustomMeshDefinition(
						Item.getItemFromBlock(ProxyModBuildingBricksCompatVanilla.grassSlab),
						new SimpleItemMeshDefinition("buildingbrickscompatvanilla:grass_slab"));
			}
		});
	}
}
