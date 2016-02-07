package com.hea3ven.buildingbricks.compat.vanilla;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStoneSlab;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

import com.hea3ven.buildingbricks.compat.vanilla.blocks.BlockGrassSlab;
import com.hea3ven.buildingbricks.compat.vanilla.items.ItemBlockGrassSlab;
import com.hea3ven.buildingbricks.core.ModBuildingBricks;
import com.hea3ven.buildingbricks.core.materials.Material;
import com.hea3ven.buildingbricks.core.materials.MaterialBlockType;
import com.hea3ven.buildingbricks.core.materials.MaterialRegistry;

@Mod(modid = ModBuildingBricks.MODID_COMP_VANILLA, name = "Building Bricks Vanilla Compatibilty",
		version = ModBuildingBricks.VERSION)
public class ModBuildingBricksCompatVanilla {

	private static final Logger logger = LogManager.getLogger("BuildingBricks.CompatVanilla");

	public static Block grassSlab;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {

		logger.info("Registering the grass slab block");
		grassSlab = new BlockGrassSlab().setUnlocalizedName("grass_slab");
		GameRegistry.registerBlock(grassSlab, ItemBlockGrassSlab.class, "grass_slab");
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		grassSlab.setCreativeTab(ModBuildingBricks.proxy.getCreativeTab("buildingBricks"));

		replaceStoneSlabRecipe();

		if (event.getSide() == Side.CLIENT) {
			ItemModelMesher modelMesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
			modelMesher.register(Item.getItemFromBlock(grassSlab), 0,
					new ModelResourceLocation("buildingbrickscompatvanilla:grass_slab", "inventory"));
		}
	}

	private void replaceStoneSlabRecipe() {
		Material mat = MaterialRegistry.get("buildingbrickscompatvanilla:stone");
		if (mat == null || mat.getBlock(MaterialBlockType.SLAB) == null)
			return;

		logger.info("Replacing default stone slab recipe");
		List<IRecipe> recipes = CraftingManager.getInstance().getRecipeList();
		for (int i = 0; i < recipes.size(); i++) {
			IRecipe recipe = recipes.get(i);
			ItemStack result = recipe.getRecipeOutput();
			if (result != null && result.getItem() instanceof ItemBlock &&
					((ItemBlock) result.getItem()).getBlock() == Blocks.stone_slab &&
					result.getMetadata() == BlockStoneSlab.EnumType.STONE.getMetadata()) {
				logger.debug("Found original slab recipe");
				recipes.remove(i);
			}
		}

		ItemStack stoneSlab = mat.getBlock(MaterialBlockType.SLAB).getStack();
		ItemStack stoneSlabSlab =
				new ItemStack(Blocks.stone_slab, 2, BlockStoneSlab.EnumType.STONE.getMetadata());
		GameRegistry.addShapedRecipe(stoneSlabSlab, "x", "x", 'x', stoneSlab);
	}
}
