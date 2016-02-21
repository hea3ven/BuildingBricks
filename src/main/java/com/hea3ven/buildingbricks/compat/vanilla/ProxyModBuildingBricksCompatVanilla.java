package com.hea3ven.buildingbricks.compat.vanilla;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStoneSlab;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

import com.hea3ven.buildingbricks.compat.vanilla.blocks.BlockGrassSlab;
import com.hea3ven.buildingbricks.compat.vanilla.client.LongGrassTextureGenerator;
import com.hea3ven.buildingbricks.compat.vanilla.items.ItemBlockGrassSlab;
import com.hea3ven.buildingbricks.core.ModBuildingBricks;
import com.hea3ven.buildingbricks.core.materials.Material;
import com.hea3ven.buildingbricks.core.materials.MaterialBlockType;
import com.hea3ven.buildingbricks.core.materials.MaterialRegistry;
import com.hea3ven.buildingbricks.core.materials.loader.MaterialResourceLoader;
import com.hea3ven.tools.commonutils.mod.ProxyModBase;
import com.hea3ven.tools.commonutils.util.SidedCall;

public class ProxyModBuildingBricksCompatVanilla extends ProxyModBase {

	private static final Logger logger = LogManager.getLogger("BuildingBricks.CompatVanilla");

	public static Block grassSlab;

	public ProxyModBuildingBricksCompatVanilla(String modId) {
		super(modId);

		grassSlab = new BlockGrassSlab().setUnlocalizedName("grass_slab");

		MaterialResourceLoader.addDomain("minecraft");
	}

	@Override
	public void onPreInitEvent(FMLPreInitializationEvent event) {
		super.onPreInitEvent(event);
		SidedCall.run(Side.CLIENT, new Runnable() {
			@Override
			public void run() {
				MinecraftForge.EVENT_BUS.register(new LongGrassTextureGenerator());
			}
		});
	}

	@Override
	protected void registerBlocks() {
		addBlock(grassSlab, "grass_slab", ItemBlockGrassSlab.class);
	}

	@Override
	protected void registerCreativeTabs() {
		grassSlab.setCreativeTab(ModBuildingBricks.proxy.getCreativeTab("buildingBricks"));
	}

	@Override
	protected void registerRecipes() {
		replaceStoneSlabRecipe();
	}

	private void replaceStoneSlabRecipe() {
		Material mat = MaterialRegistry.get("minecraft:stone");
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
		addRecipe(stoneSlabSlab, "x", "x", 'x', stoneSlab);
	}
}
