package com.hea3ven.buildingbricks.core;

import java.util.Map.Entry;

import net.minecraft.item.ItemStack;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

import com.hea3ven.buildingbricks.ModBuildingBricks;
import com.hea3ven.buildingbricks.core.eventhandlers.EventHandlerOverrideBlockPlacing;
import com.hea3ven.buildingbricks.core.eventhandlers.EventHandlerTrowelOverlay;
import com.hea3ven.buildingbricks.core.lib.BlockDescription;
import com.hea3ven.buildingbricks.core.materials.Material;
import com.hea3ven.buildingbricks.core.materials.MaterialBlockType;
import com.hea3ven.buildingbricks.core.materials.MaterialRegistry;

public class ProxyCommonBuildingBricks {

	public void preInit() {
	}

	public void init() {
		MinecraftForge.EVENT_BUS.register(new EventHandlerOverrideBlockPlacing());
		MinecraftForge.EVENT_BUS.register(new EventHandlerTrowelOverlay());
	}

	public void postInit() {
		addTrowelRecipes();

		addMaterialBlocksRecipes();
	}

	private void addTrowelRecipes() {
		ItemStack trowelStack = new ItemStack(ModBuildingBricks.trowel, 1, OreDictionary.WILDCARD_VALUE);
		for (Material mat : MaterialRegistry.getAll()) {
			ItemStack bindedTrowelStack = new ItemStack(ModBuildingBricks.trowel);
			ModBuildingBricks.trowel.setBindedMaterial(bindedTrowelStack, mat);
			for (BlockDescription blockDesc : mat.getBlockRotation().getAll().values()) {
				GameRegistry.addShapelessRecipe(bindedTrowelStack, trowelStack, blockDesc.getStack());
			}
		}
	}

	private void addMaterialBlocksRecipes() {
		for (Material mat : MaterialRegistry.getAll()) {
			for (Entry<MaterialBlockType, BlockDescription> entry : mat.getBlockRotation().getAll().entrySet()) {
				entry.getKey().addRecipes(mat);
			}
		}
	}
}
