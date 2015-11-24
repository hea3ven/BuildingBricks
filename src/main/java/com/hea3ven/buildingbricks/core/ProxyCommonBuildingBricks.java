package com.hea3ven.buildingbricks.core;

import java.util.Map.Entry;

import net.minecraft.item.ItemStack;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.hea3ven.buildingbricks.core.eventhandlers.EventHandlerOverrideBlockPlacing;
import com.hea3ven.buildingbricks.core.gui.BuildingBricksGuiHandler;
import com.hea3ven.buildingbricks.core.materials.BlockDescription;
import com.hea3ven.buildingbricks.core.materials.Material;
import com.hea3ven.buildingbricks.core.materials.MaterialBlockType;
import com.hea3ven.buildingbricks.core.materials.MaterialRegistry;
import com.hea3ven.buildingbricks.core.materials.mapping.MaterialIdMapping;

public class ProxyCommonBuildingBricks {

	public void preInit() {
	}

	public void init() {
		MinecraftForge.EVENT_BUS.register(new EventHandlerOverrideBlockPlacing());

		NetworkRegistry.INSTANCE.registerGuiHandler(Properties.MODID, new BuildingBricksGuiHandler());
	}

	public void postInit() {
		addTrowelRecipes();

		addMaterialBlocksRecipes();
	}

	private void addTrowelRecipes() {
		ModBuildingBricks.logger.info("Registering trowel's recipes");
		for (Material mat : MaterialRegistry.getAll()) {
			for (BlockDescription blockDesc : mat.getBlockRotation().getAll().values()) {
				ItemStack trowelStack = new ItemStack(ModBuildingBricks.trowel, 1);
				ItemStack bindedTrowelStack = new ItemStack(ModBuildingBricks.trowel, 1,
						MaterialIdMapping.get().getIdForMaterial(mat));
				GameRegistry.addShapelessRecipe(bindedTrowelStack, trowelStack, blockDesc.getStack());
			}
		}
	}

	private void addMaterialBlocksRecipes() {
		ModBuildingBricks.logger.info("Registering materials recipes");
		for (Material mat : MaterialRegistry.getAll()) {
			for (Entry<MaterialBlockType, BlockDescription> entry : mat.getBlockRotation()
					.getAll()
					.entrySet()) {
				entry.getKey().registerRecipes(mat);
			}
		}
	}
}
