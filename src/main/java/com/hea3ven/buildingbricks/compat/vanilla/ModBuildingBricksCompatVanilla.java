package com.hea3ven.buildingbricks.compat.vanilla;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

import net.minecraftforge.common.MinecraftForge;

import com.hea3ven.buildingbricks.compat.vanilla.blocks.BlockGrassSlab;
import com.hea3ven.buildingbricks.core.config.Config;
import com.hea3ven.buildingbricks.core.items.ItemColoredWrapper;

@Mod(modid = ModBuildingBricksCompatVanilla.MODID, name = "Building Bricks Vanilla Compatibilty",
		version = ModBuildingBricksCompatVanilla.VERSION)
public class ModBuildingBricksCompatVanilla {

	public static final String MODID = "buildingbrickscompatvanilla";
	public static final String VERSION = "1.0.0-beta1";

	private static final Logger logger = LogManager.getLogger("BuildingBricks.CompatVanilla");

	public static Block grassSlab;

	static {
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {

		logger.info("Registering the grass slab block");
		grassSlab = new BlockGrassSlab().setBlockName("grass_slab");
		GameRegistry.registerBlock(grassSlab, ItemColoredWrapper.class, "grass_slab");
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		if (Config.generateGrassSlabs)
			MinecraftForge.EVENT_BUS.register(new GrassSlabWorldGen());

		replaceStoneSlabRecipe();
	}

	private void replaceStoneSlabRecipe() {
		logger.info("Replacing default stone slab recipe");
		List<IRecipe> recipes = CraftingManager.getInstance().getRecipeList();
		for (int i = 0; i < recipes.size(); i++) {
			IRecipe recipe = recipes.get(i);
			ItemStack result = recipe.getRecipeOutput();
			if (result != null && result.getItem() instanceof ItemBlock
					&& ((ItemBlock) result.getItem()).field_150939_a == Blocks.stone_slab
					&& result.getItemDamage() == 0) {
				logger.debug("Found original slab recipe");
				recipes.remove(i);
			}
		}

		ItemStack stoneSlab = new ItemStack(Block.getBlockFromName("buildingbricks:stone_slab"));
		ItemStack stoneSlabSlab = new ItemStack(Blocks.stone_slab, 2,
				0);
		GameRegistry.addShapedRecipe(stoneSlabSlab, "x", "x", 'x', stoneSlab);
	}

}
