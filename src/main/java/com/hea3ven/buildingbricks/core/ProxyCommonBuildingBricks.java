package com.hea3ven.buildingbricks.core;

import java.util.Map.Entry;

import net.minecraft.item.ItemStack;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.RecipeSorter.Category;

import com.hea3ven.buildingbricks.core.eventhandlers.EventHandlerOverrideBlockPlacing;
import com.hea3ven.buildingbricks.core.items.crafting.RecipeBindTrowel;
import com.hea3ven.buildingbricks.core.items.crafting.RecipeBlockMaterial;
import com.hea3ven.buildingbricks.core.materials.BlockDescription;
import com.hea3ven.buildingbricks.core.materials.Material;
import com.hea3ven.buildingbricks.core.materials.MaterialBlockType;
import com.hea3ven.buildingbricks.core.materials.MaterialRegistry;

public class ProxyCommonBuildingBricks {

	public void preInit() {
	}

	public void init() {
		MinecraftForge.EVENT_BUS.register(new EventHandlerOverrideBlockPlacing());
	}

	public void postInit() {
		RecipeSorter.register("buildingbricks:bindtrowel", RecipeBindTrowel.class,
				Category.SHAPELESS, "after:minecraft:shapeless");
		RecipeSorter.register("buildingbricks:blockmaterial", RecipeBlockMaterial.class,
				Category.SHAPED, "after:minecraft:shaped");

		addTrowelRecipes();

		addMaterialBlocksRecipes();
	}

	private void addTrowelRecipes() {
		ModBuildingBricks.logger.info("Registering trowel's recipes");
		for (Material mat : MaterialRegistry.getAll()) {
			for (BlockDescription blockDesc : mat.getBlockRotation().getAll().values()) {
				ItemStack bindedTrowelStack = new ItemStack(ModBuildingBricks.trowel);
				ModBuildingBricks.trowel.setBindedMaterial(bindedTrowelStack, mat);
				ItemStack trowelStack = new ItemStack(ModBuildingBricks.trowel, 1);
				GameRegistry.addRecipe(
						new RecipeBindTrowel(bindedTrowelStack, trowelStack, blockDesc.getStack()));
			}
		}
	}

	private void addMaterialBlocksRecipes() {
		ModBuildingBricks.logger.info("Registering materials recipes");
		for (Material mat : MaterialRegistry.getAll()) {
			for (Entry<MaterialBlockType, BlockDescription> entry : mat
					.getBlockRotation()
					.getAll()
					.entrySet()) {
				entry.getKey().registerRecipes(mat);
			}
		}
	}
}
