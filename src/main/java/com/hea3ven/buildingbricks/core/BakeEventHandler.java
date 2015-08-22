package com.hea3ven.buildingbricks.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.vecmath.Vector3f;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.DefaultStateMapper;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.util.IRegistry;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.client.event.ModelBakeEvent;
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

	private DefaultStateMapper stateMap = new DefaultStateMapper();

	private BakeEventHandler() {
	};

	@SubscribeEvent
	public void onModelBakeEvent(ModelBakeEvent event) {
		for (Entry<MaterialBlockType, HashMap<StructureMaterial, Block>> entry : MaterialBlockRegistry.instance
				.getBlocks()
				.entrySet()) {
			SmartModelCached model = new SmartModelCached();
			for (Entry<StructureMaterial, Block> subEntry : entry.getValue().entrySet()) {
				ModelItemMaterialBlock itemModel = new ModelItemMaterialBlock();
				bakeBlockModels(event.modelRegistry, model, itemModel, entry.getKey(),
						subEntry.getKey(), subEntry.getValue());
			}
		}

		bakeItemTrowelModels(event);
	}

	private void bakeBlockModels(IRegistry modelRegistry, SmartModelCached cacheModel,
			ModelItemMaterialBlock itemModel, MaterialBlockType blockType,
			StructureMaterial structMat, Block block) {
		IRenderDefinition renderDefinition = blockType.getRenderDefinition();
		if (renderDefinition == null)
			return;

		ResourceLocation blockName = (ResourceLocation) GameData
				.getBlockRegistry()
				.getNameForObject(block);
		modelRegistry.putObject(new ModelResourceLocation(blockName + "#inventory"), itemModel);

		for (Object stateObj : block.getBlockState().getValidStates()) {
			ModelResourceLocation modelLoc = new ModelResourceLocation(blockName,
					getPropertyString((IBlockState) stateObj));
			modelRegistry.putObject(modelLoc, cacheModel);
		}

		Iterable<Material> materials = Iterables.concat(
				MaterialBlockRegistry.instance.getBlocksMaterials().get(blockType).values());
		for (Material mat : materials) {
			HashMap<String, String> textures = new HashMap<String, String>();
			textures.put("all", mat.sideTextureLocation());
			textures.put("side", mat.sideTextureLocation());
			textures.put("top", mat.topTextureLocation());
			textures.put("bottom", mat.bottomTextureLocation());
			textures.put("wall", mat.sideTextureLocation());

			IModel baseModel = renderDefinition.getItemModel();
			baseModel = retexture(textures, baseModel);
			IModelState modelState = renderDefinition
					.getItemModelState(baseModel.getDefaultState());
			IFlexibleBakedModel bakedModel = bake(baseModel, modelState);

			itemModel.put(mat.materialId(), bakedModel);
			cacheModel.setDelegate(bakedModel);

			for (Object stateObj : block.getBlockState().getValidStates()) {
				IBlockState state = (IBlockState) stateObj;

				IModel blockModel = renderDefinition.getModel(state);
				blockModel = retexture(textures, blockModel);
				modelState = renderDefinition.getModelState(blockModel.getDefaultState(), state);
				bakedModel = bake(blockModel, modelState);

				state = TileMaterial.setStateMaterial((IExtendedBlockState) state, mat);
				cacheModel.put(state, bakedModel);
			}
		}

		IFlexibleBakedModel model = bake(ModelLoaderRegistry.getMissingModel());
		if (cacheModel.getDelegate() == null)
			cacheModel.setDelegate(model);
		if (itemModel.getDelegate() == null)
			itemModel.setDelegate(model);
	}

	private void bakeItemTrowelModels(ModelBakeEvent event) {
		ResourceLocation trowelModelLoc = new ResourceLocation("buildingbricks:item/trowel");
		for (Material material : MaterialRegistry.getAll()) {
			HashMap<String, String> textures = new HashMap<String, String>();
			textures.put("all", material.sideTextureLocation());

			IModel itemModel = event.modelLoader.getModel(new ResourceLocation("block/cube_all"));
			itemModel = retexture(textures, itemModel);
			Vector3f translation = new Vector3f(0.3f, 0.5f, 0.2f);
			Vector3f scale = new Vector3f(0.4f, 0.4f, 0.4f);
			IModelState modelState = new TRSRTransformation(translation, null, scale, null);
			IFlexibleBakedModel bakedItemModel = bake(itemModel, modelState);

			itemModel = event.modelLoader.getModel(trowelModelLoc);
			IFlexibleBakedModel baseBakedItemModel = bake(itemModel);

			ModelTrowel.models.put(material, new ModelTrowel(baseBakedItemModel, bakedItemModel));
		}
		IModel baseItemModel = event.modelLoader.getModel(trowelModelLoc);
		IFlexibleBakedModel baseBakedItemModel = bake(baseItemModel);
		event.modelRegistry.putObject(new ModelResourceLocation("buildingbricks:trowel#inventory"),
				new ModelTrowel(baseBakedItemModel));
	}

	private IFlexibleBakedModel bake(IModel model) {
		return bake(model, model.getDefaultState());
	}

	private IFlexibleBakedModel bake(IModel model, IModelState modelState) {
		return model.bake(modelState, Attributes.DEFAULT_BAKED_FORMAT, null);
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
