package com.hea3ven.buildingbricks.core.client;

import java.util.Map;

import com.google.common.collect.Maps;
import com.google.common.collect.Table.Cell;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.util.IRegistry;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.IFlexibleBakedModel;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.IModelState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.hea3ven.buildingbricks.core.client.model.ModelItemMaterialBlock;
import com.hea3ven.buildingbricks.core.client.model.SmartModelCached;
import com.hea3ven.buildingbricks.core.materials.Material;
import com.hea3ven.buildingbricks.core.materials.MaterialBlockRegistry;
import com.hea3ven.buildingbricks.core.materials.MaterialBlockType;
import com.hea3ven.buildingbricks.core.materials.MaterialRegistry;
import com.hea3ven.buildingbricks.core.materials.StructureMaterial;
import com.hea3ven.buildingbricks.core.materials.rendering.*;
import com.hea3ven.buildingbricks.core.tileentity.TileMaterial;

@SideOnly(Side.CLIENT)
public class ModelBakerBlockMaterial extends ModelBakerBase {

	public static ModelBakerBlockMaterial instance;

	public Map<Material, TextureAtlasSprite> particleTextures = Maps.newHashMap();

	private Map<MaterialBlockType, RenderDefinition> renderDefinitions = Maps.newHashMap();

	public static void init() {
		instance = new ModelBakerBlockMaterial();

		initBlockTypeRendering();
		MinecraftForge.EVENT_BUS.register(instance);
	}

	private static void initBlockTypeRendering() {
		instance.renderDefinitions.put(MaterialBlockType.FULL,
				new RenderDefinitionSimple("minecraft:block/cube_bottom_top"));
		instance.renderDefinitions.put(MaterialBlockType.STAIRS, new RenderDefinitionStairs());
		instance.renderDefinitions.put(MaterialBlockType.SLAB, new RenderDefinitionSlab());
		instance.renderDefinitions.put(MaterialBlockType.VERTICAL_SLAB, new RenderDefinitionSlab(true));
		instance.renderDefinitions.put(MaterialBlockType.STEP, new RenderDefinitionStep());
		instance.renderDefinitions.put(MaterialBlockType.CORNER,
				new RenderDefinitionRotHalf("buildingbricks:block/corner_bottom"));
		instance.renderDefinitions.put(MaterialBlockType.WALL,
				new RenderDefinitionWall("minecraft:block/wall_post",
						"buildingbricks:block/wall_connection"));
		instance.renderDefinitions.put(MaterialBlockType.FENCE,
				new RenderDefinitionConnectable("minecraft:block/fence_post",
						"buildingbricks:block/fence_connection", "minecraft:block/fence_inventory"));
		instance.renderDefinitions.put(MaterialBlockType.FENCE_GATE, new RenderDefinitionFenceGate());
	}

	@SubscribeEvent
	public void onTextureStichPreEvent(TextureStitchEvent.Pre event) {
		for (Material mat : MaterialRegistry.getAll()) {
			particleTextures.put(mat,
					event.map.registerSprite(new ResourceLocation(mat.getTextures().get("particle"))));
		}
	}

	@SubscribeEvent
	public void onModelBakeEvent(ModelBakeEvent event) {
		for (Cell<MaterialBlockType, StructureMaterial, Block> cell : MaterialBlockRegistry.instance.getBlocks()
				.cellSet()) {
			SmartModelCached model = new SmartModelCached();
			ModelItemMaterialBlock itemModel = new ModelItemMaterialBlock();
			bakeBlockModels(event.modelRegistry, cell.getRowKey(), cell.getColumnKey(), cell.getValue(),
					itemModel, model);
		}
	}

	private void bakeBlockModels(IRegistry modelRegistry, MaterialBlockType blockType,
			StructureMaterial structMat, Block block, ModelItemMaterialBlock materialItemModel,
			SmartModelCached cacheModel) {
		RenderDefinition renderDefinition = renderDefinitions.get(blockType);
		if (renderDefinition == null)
			return;

		ResourceLocation blockName = (ResourceLocation) GameData.getBlockRegistry().getNameForObject(block);

		// Register models in the registry
		modelRegistry.putObject(new ModelResourceLocation(blockName + "#inventory"), materialItemModel);

		for (Object stateObj : block.getBlockState().getValidStates()) {
			ModelResourceLocation modelLoc =
					new ModelResourceLocation(blockName, getPropertyString((IBlockState) stateObj));
			modelRegistry.putObject(modelLoc, cacheModel);
		}

		// Generate the actual models
		for (Material mat : MaterialBlockRegistry.instance.getBlockMaterials(block)) {

			// Item model
			IModel itemModel = renderDefinition.getItemModel(mat);
			itemModel = retexture(mat.getTextures(), itemModel);
			IModelState modelState = renderDefinition.getItemModelState(itemModel.getDefaultState());
			IFlexibleBakedModel bakedItemModel = bake(itemModel, modelState);

			materialItemModel.put(mat.getMaterialId(), bakedItemModel);
			materialItemModel.setDelegate(bakedItemModel);

			for (Object stateObj : block.getBlockState().getValidStates()) {

				IBlockState state = (IBlockState) stateObj;

				IModel blockModel = renderDefinition.getModel(state, mat);
				blockModel = retexture(mat.getTextures(), blockModel);
				modelState = renderDefinition.getModelState(blockModel.getDefaultState(), state);
				IFlexibleBakedModel bakedModel = bake(blockModel, modelState);

				state = TileMaterial.setStateMaterial((IExtendedBlockState) state, mat);
				cacheModel.put(state, bakedModel);
			}
		}

		IModel defaultBlockModel = getModel(new ResourceLocation("block/stone"));
		cacheModel.setDelegate(bake(defaultBlockModel, defaultBlockModel.getDefaultState()));
	}
}
