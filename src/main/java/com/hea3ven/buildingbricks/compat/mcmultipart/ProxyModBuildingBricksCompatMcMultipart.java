package com.hea3ven.buildingbricks.compat.mcmultipart;

import mcmultipart.client.multipart.MultipartRegistryClient;
import mcmultipart.multipart.MultipartRegistry;

import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.BlockStateMapper;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.hea3ven.buildingbricks.compat.mcmultipart.blocks.placement.SlabMultipartPlacementManager;
import com.hea3ven.buildingbricks.compat.mcmultipart.item.ItemMaterialBlockMultipart;
import com.hea3ven.buildingbricks.compat.mcmultipart.multipart.MultipartBlockMaterialConverter;
import com.hea3ven.buildingbricks.compat.mcmultipart.multipart.MultipartBlockMaterialFactory;
import com.hea3ven.buildingbricks.core.blocks.placement.BlockPlacementManager;
import com.hea3ven.buildingbricks.core.materials.MaterialBlockRegistry;
import com.hea3ven.tools.commonutils.client.ModelBakerBase;
import com.hea3ven.tools.commonutils.mod.ProxyModModule;

public class ProxyModBuildingBricksCompatMcMultipart extends ProxyModModule {

	@Override
	public void onPreInitEvent(FMLPreInitializationEvent event) {
		super.onPreInitEvent(event);
		MaterialBlockRegistry.itemCls = ItemMaterialBlockMultipart.class;
	}

	@Override
	public void onInitEvent(FMLInitializationEvent event) {
		super.onInitEvent(event);
		MultipartRegistry.registerPartFactory(new MultipartBlockMaterialFactory(), "minecraft:stone_slab",
				"buildingbricks:rock_slab", "buildingbricks:rock_vertical_slab", "buildingbricks:rock_step",
				"buildingbricks:rock_corner");
		MultipartRegistry.registerPartConverter(new MultipartBlockMaterialConverter());
		MultipartRegistryClient.registerEmptySpecialPartStateMapper(
				new ResourceLocation("minecraft:stone_slab"));

		BlockPlacementManager.getInstance().add(50, new SlabMultipartPlacementManager());
	}

	@Override
	protected void registerModelBakers() {
		addModelBaker(new ModelBakerBase() {
			@Override
			public void onModelBakeEvent(ModelBakeEvent event) {
				if (Minecraft.getMinecraft().getBlockRendererDispatcher() == null)
					return;
				Map<IBlockState, ModelResourceLocation> variants = Minecraft.getMinecraft()
						.getBlockRendererDispatcher()
						.getBlockModelShapes()
						.getBlockStateMapper()
						.getVariants(Blocks.STONE_SLAB);
				for (IBlockState state : Blocks.STONE_SLAB.getBlockState().getValidStates()) {
					IBakedModel model = event.getModelManager().getModel(variants.get(state));
					event.getModelRegistry()
							.putObject(new ModelResourceLocation(
									Block.REGISTRY.getNameForObject(state.getBlock()),
									getPropertyString(state)), model);
				}
			}
		});
	}
}
