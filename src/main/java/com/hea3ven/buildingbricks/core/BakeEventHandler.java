package com.hea3ven.buildingbricks.core;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.vecmath.Vector3f;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Table.Cell;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.statemap.DefaultStateMapper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.util.IRegistry;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.Attributes;
import net.minecraftforge.client.model.IFlexibleBakedModel;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.IModelState;
import net.minecraftforge.client.model.IRetexturableModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.MultiModel;
import net.minecraftforge.client.model.TRSRTransformation;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameData;

import com.hea3ven.buildingbricks.core.client.model.ModelItemMaterialBlock;
import com.hea3ven.buildingbricks.core.client.model.ModelTrowel;
import com.hea3ven.buildingbricks.core.client.model.SmartModelCached;
import com.hea3ven.buildingbricks.core.materials.Material;
import com.hea3ven.buildingbricks.core.materials.MaterialBlockRegistry;
import com.hea3ven.buildingbricks.core.materials.MaterialBlockType;
import com.hea3ven.buildingbricks.core.materials.MaterialRegistry;
import com.hea3ven.buildingbricks.core.materials.StructureMaterial;
import com.hea3ven.buildingbricks.core.materials.rendering.IRenderDefinition;
import com.hea3ven.buildingbricks.core.tileentity.TileMaterial;

public class BakeEventHandler {
	public static final BakeEventHandler instance = new BakeEventHandler();

	protected static final Logger logger = LogManager.getLogger("BuildingBricks.BakeEventHandler");

	private DefaultStateMapper stateMap = new DefaultStateMapper();

	public Map<Material, TextureAtlasSprite> particleTextures = Maps.newHashMap();

	private BakeEventHandler() {
	};

	@SubscribeEvent
	public void onTextureStichPreEvent(TextureStitchEvent.Pre event) {
		for (Material mat : MaterialRegistry.getAll()) {
			particleTextures.put(mat, event.map
					.registerSprite(new ResourceLocation(mat.getTextures().get("particle"))));
		}
	}

	@SubscribeEvent
	public void onModelBakeEvent(ModelBakeEvent event) {
		for (Cell<MaterialBlockType, StructureMaterial, Block> cell : MaterialBlockRegistry.instance
				.getBlocks()
				.cellSet()) {
			SmartModelCached model = new SmartModelCached();
			ModelItemMaterialBlock itemModel = new ModelItemMaterialBlock();
			bakeBlockModels(event.modelRegistry, cell.getRowKey(), cell.getColumnKey(),
					cell.getValue(), itemModel, model);
		}

		bakeItemTrowelModels(event);
	}

	private void bakeBlockModels(IRegistry modelRegistry, MaterialBlockType blockType,
			StructureMaterial structMat, Block block, ModelItemMaterialBlock materialItemModel,
			SmartModelCached cacheModel) {
		IRenderDefinition renderDefinition = blockType.getRenderDefinition();
		if (renderDefinition == null)
			return;

		ResourceLocation blockName = (ResourceLocation) GameData
				.getBlockRegistry()
				.getNameForObject(block);

		// Register models in the registry
		modelRegistry.putObject(new ModelResourceLocation(blockName + "#inventory"),
				materialItemModel);

		for (Object stateObj : block.getBlockState().getValidStates()) {
			ModelResourceLocation modelLoc = new ModelResourceLocation(blockName,
					getPropertyString((IBlockState) stateObj));
			modelRegistry.putObject(modelLoc, cacheModel);
		}

		// Generate the actual models
		for (Material mat : MaterialBlockRegistry.instance.getBlockMaterials(block)) {

			// Item model
			IModel itemModel = renderDefinition.getItemModel(mat);
			itemModel = retexture(mat.getTextures(), itemModel);
			IModelState modelState = renderDefinition
					.getItemModelState(itemModel.getDefaultState());
			IFlexibleBakedModel bakedItemModel = bake(itemModel, modelState);

			materialItemModel.put(mat.materialId(), bakedItemModel);
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

		IModel defaultBlockModel = ModelLoaderRegistry.getMissingModel();
		cacheModel.setDelegate(bake(defaultBlockModel, defaultBlockModel.getDefaultState()));
	}

	private void bakeItemTrowelModels(ModelBakeEvent event) {
		ResourceLocation trowelModelLoc = new ResourceLocation("buildingbricks:item/trowel");
		for (Material material : MaterialRegistry.getAll()) {

			IModel itemModel = getModel(new ResourceLocation("block/cube_all"));
			itemModel = retexture(material.getTextures(), itemModel);
			Vector3f translation = new Vector3f(0.3f, 0.5f, 0.3f);
			Vector3f scale = new Vector3f(0.4f, 0.4f, 0.4f);
			IModelState modelState = new TRSRTransformation(translation, null, scale, null);
			IFlexibleBakedModel bakedItemModel = bake(itemModel, modelState);

			itemModel = getModel(trowelModelLoc);
			IFlexibleBakedModel baseBakedItemModel = bake(itemModel);

			ModelTrowel.models.put(material, new ModelTrowel(baseBakedItemModel, bakedItemModel));
		}
		IModel baseItemModel = getModel(trowelModelLoc);
		IFlexibleBakedModel baseBakedItemModel = bake(baseItemModel);
		event.modelRegistry.putObject(new ModelResourceLocation("buildingbricks:trowel#inventory"),
				new ModelTrowel(baseBakedItemModel));
	}

	private IModel getModel(ResourceLocation modelLoc) {
		try {
			return ModelLoaderRegistry.getModel(modelLoc);
		} catch (IOException e) {
			logger.warn("Could not find model {}", modelLoc);
			return ModelLoaderRegistry.getMissingModel();
		}
	}

	private IFlexibleBakedModel bake(IModel model) {
		return bake(model, model.getDefaultState());
	}

	private IFlexibleBakedModel bake(IModel model, IModelState modelState) {
		Function<ResourceLocation, TextureAtlasSprite> textureGetter = new Function<ResourceLocation, TextureAtlasSprite>() {
			public TextureAtlasSprite apply(ResourceLocation location) {
				return Minecraft
						.getMinecraft()
						.getTextureMapBlocks()
						.getAtlasSprite(location.toString());
			}
		};
		return model.bake(modelState, Attributes.DEFAULT_BAKED_FORMAT, textureGetter);
	}

	private IModel retexture(HashMap<String, String> textures, IModel blockModel) {
		if (blockModel instanceof IRetexturableModel)
			return ((IRetexturableModel) blockModel).retexture(ImmutableMap.copyOf(textures));
		else if (blockModel instanceof MultiModel) {
			Map<String, Pair<IModel, IModelState>> parts = Maps.newHashMap();
			for (Entry<String, Pair<IModel, IModelState>> entry : ((MultiModel) blockModel)
					.getParts()
					.entrySet()) {
				parts.put(entry.getKey(), Pair.of(retexture(textures, entry.getValue().getLeft()),
						entry.getValue().getRight()));
			}
			return new MultiModel(retexture(textures, ((MultiModel) blockModel).getBaseModel()),
					blockModel.getDefaultState(), parts);
		}
		return blockModel;
	}

	private String getPropertyString(IBlockState state) {
		return stateMap.getPropertyString(state.getProperties());
	}
}
