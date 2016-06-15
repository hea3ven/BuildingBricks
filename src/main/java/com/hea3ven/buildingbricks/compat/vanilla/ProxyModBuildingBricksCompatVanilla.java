package com.hea3ven.buildingbricks.compat.vanilla;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.BlockStoneSlab;
import net.minecraft.block.BlockStoneSlab.EnumType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Property.Type;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.hea3ven.buildingbricks.compat.vanilla.block.BlockGrassSlab;
import com.hea3ven.buildingbricks.compat.vanilla.client.LongGrassTextureGenerator;
import com.hea3ven.buildingbricks.compat.vanilla.item.ItemBlockGrassSlab;
import com.hea3ven.buildingbricks.core.ModBuildingBricks;
import com.hea3ven.buildingbricks.core.block.base.BlockBuildingBricks;
import com.hea3ven.buildingbricks.core.materials.Material;
import com.hea3ven.buildingbricks.core.materials.MaterialBlockType;
import com.hea3ven.buildingbricks.core.materials.MaterialRegistry;
import com.hea3ven.buildingbricks.core.materials.MaterialStack;
import com.hea3ven.buildingbricks.core.materials.loader.MaterialResourceLoader;
import com.hea3ven.tools.commonutils.client.renderer.color.IColorHandler;
import com.hea3ven.tools.commonutils.mod.ProxyModModule;
import com.hea3ven.tools.commonutils.mod.config.FileConfigManagerBuilder.CategoryConfigManagerBuilder;
import com.hea3ven.tools.commonutils.util.SidedCall;
import com.hea3ven.tools.commonutils.util.SidedCall.SidedRunnable;
import static net.minecraft.block.BlockStoneSlab.*;

public class ProxyModBuildingBricksCompatVanilla extends ProxyModModule {

	private static final Logger logger = LogManager.getLogger("BuildingBricks.CompatVanilla");

	public static Block grassSlab;

	public ProxyModBuildingBricksCompatVanilla() {
		grassSlab = new BlockGrassSlab().setUnlocalizedName("grass_slab");

		MaterialResourceLoader.addDomain("minecraft");
	}

	@Override
	public void onPreInitEvent(FMLPreInitializationEvent event) {
		super.onPreInitEvent(event);
		SidedCall.run(Side.CLIENT, new SidedRunnable() {
			@Override
			@SideOnly(Side.CLIENT)
			public void run() {
				MinecraftForge.EVENT_BUS.register(new LongGrassTextureGenerator());
			}
		});
	}

	@Override
	public CategoryConfigManagerBuilder getConfig() {
		return new CategoryConfigManagerBuilder("vanilla").addValue("replaceGrassTexture", "true",
				Type.BOOLEAN, "Enable to replace the grass texture with a long grass texture if the " +
						"generateGrassSlabs is enabled", property -> {
					SidedCall.run(Side.CLIENT, new SidedRunnable() {
						@Override
						@SideOnly(Side.CLIENT)
						public void run() {
							LongGrassTextureGenerator.enabled = property.getBoolean();
						}
					});
				})
				.addValue("replaceGrassTextureForce", "false", Type.BOOLEAN,
						"Enable to replace the grass texture with a long grass texture always", property -> {
							SidedCall.run(Side.CLIENT, new SidedRunnable() {
								@Override
								@SideOnly(Side.CLIENT)
								public void run() {
									LongGrassTextureGenerator.forceEnabled = property.getBoolean();
								}
							});
						})
				.addValue("generateGrassSlabs", "true", Type.BOOLEAN,
						"Enable to generate grass slabs in the world to smooth out the surface",
						GrassSlabWorldGen.get());
	}

	@Override
	protected void registerBlocks() {
		addBlock(grassSlab, "grass_slab", new ItemBlockGrassSlab(grassSlab));
	}

	@Override
	protected void registerCreativeTabs() {
		grassSlab.setCreativeTab(ModBuildingBricks.proxy.getCreativeTab("buildingBricks"));
	}

	@Override
	@SideOnly(Side.CLIENT)
	protected void registerColors() {
		addColors(new IColorHandler() {
			@Override
			public int colorMultiplier(IBlockState state, IBlockAccess world, BlockPos pos, int tintIndex) {
				return ((BlockBuildingBricks) state.getBlock()).getBlockLogic()
						.colorMultiplier(world, pos, tintIndex);
			}

			@Override
			public int getColorFromItemstack(ItemStack stack, int tintIndex) {
				Material mat = MaterialStack.get(stack);
				if (mat == null)
					return 0;
				else
					return Minecraft.getMinecraft()
							.getItemColors()
							.getColorFromItemstack(mat.getFirstBlock().getStack(), tintIndex);
			}
		}, grassSlab);
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
			if (result != null && result.getItem() instanceof ItemBlock) {
				if (((ItemBlock) result.getItem()).getBlock() == Blocks.STONE_SLAB) {
					if (result.getMetadata() == EnumType.STONE.getMetadata()) {
						logger.debug("Found original slab recipe");
						recipes.remove(i);
					} else if (result.getMetadata() == EnumType.SMOOTHBRICK.getMetadata()) {
						logger.debug("Found original stone bricks slab recipe");
						recipes.remove(i);
					}
				} else if (((ItemBlock) result.getItem()).getBlock() == Blocks.STONE_BRICK_STAIRS) {
					logger.debug("Found original stone bricks stairs recipe");
					recipes.remove(i);
				}
			}
		}

		ItemStack stoneSlab = mat.getBlock(MaterialBlockType.SLAB).getStack();
		ItemStack stoneSlabSlab =
				new ItemStack(Blocks.STONE_SLAB, 2, BlockStoneSlab.EnumType.STONE.getMetadata());
		addRecipe(stoneSlabSlab, "x", "x", 'x', stoneSlab);
		ItemStack stoneBrick =
				new ItemStack(Blocks.STONEBRICK, 1, BlockStoneBrick.EnumType.DEFAULT.getMetadata());
		ItemStack stoneBrickSlab =
				new ItemStack(Blocks.STONE_SLAB, 6, EnumType.SMOOTHBRICK.getMetadata());
		addRecipe(stoneBrickSlab, "###", '#', stoneBrick);
		this.addRecipe(new ItemStack(Blocks.STONE_BRICK_STAIRS, 4), "#  ", "## ", "###", '#', stoneBrick);
	}
}
