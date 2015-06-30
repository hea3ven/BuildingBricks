package com.hea3ven.buildingbricks.core;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;

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
import net.minecraftforge.client.model.IRetexturableModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
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
import com.hea3ven.buildingbricks.core.tileentity.TileMaterial;

public class BakeEventHandler {
	public static final BakeEventHandler instance = new BakeEventHandler();

	private DefaultStateMapper stateMap = new DefaultStateMapper();

	private BakeEventHandler() {
	};

	@SubscribeEvent
	public void onModelBakeEvent(ModelBakeEvent event) {
		for (Entry<MaterialBlockType, HashMap<StructureMaterial, Set<Material>>> entry : MaterialBlockRegistry.instance
				.getBlocksMaterials().entrySet()) {
			SmartModelCached model = new SmartModelCached();
			ModelItemMaterialBlock itemModel = new ModelItemMaterialBlock();
			bakeBlockModels(event.modelRegistry, model, itemModel, entry.getKey(),
					Iterables.concat(entry.getValue().values()));
		}

		bakeItemTrowelModels(event);
	}

	private void bakeBlockModels(IRegistry modelRegistry, SmartModelCached cacheModel,
			ModelItemMaterialBlock itemModel, MaterialBlockType blockType,
			Iterable<Material> materials) {
		for (Material mat : materials) {
			HashMap<String, String> textures = new HashMap<String, String>();
			textures.put("side", mat.sideTextureLocation());
			textures.put("top", mat.topTextureLocation());
			textures.put("bottom", mat.bottomTextureLocation());

			IRetexturableModel baseModel = (IRetexturableModel) ModelLoaderRegistry
					.getModel(blockType.baseModel());
			baseModel = (IRetexturableModel) baseModel.retexture(ImmutableMap.copyOf(textures));

			IFlexibleBakedModel bakedModel = baseModel.bake(
					new ModelLoader.UVLock(baseModel.getDefaultState()),
					Attributes.DEFAULT_BAKED_FORMAT, null);
			itemModel.put(mat.materialId(), bakedModel);
			modelRegistry.putObject(new ModelResourceLocation(GameData.getBlockRegistry()
					.getNameForObject(mat.getBlock(blockType).getBlock()) + "#inventory"),
					itemModel);
			cacheModel.setDefault(bakedModel);
			for (IBlockState state : blockType.getValidBlockStates()) {
				baseModel = (IRetexturableModel) ModelLoaderRegistry.getModel(blockType
						.baseModel(state));
				baseModel = (IRetexturableModel) baseModel.retexture(ImmutableMap.copyOf(textures));

				bakedModel = baseModel.bake(
						new ModelLoader.UVLock(blockType.getModelStateFromBlockState(state)),
						Attributes.DEFAULT_BAKED_FORMAT, null);
				state = TileMaterial.setStateMaterial((IExtendedBlockState) state, mat);
				cacheModel.put(state, bakedModel);
				modelRegistry.putObject(
						new ModelResourceLocation((ResourceLocation) Block.blockRegistry
								.getNameForObject(mat.getBlock(blockType).getBlock()), stateMap
								.getPropertyString(state.getProperties())), cacheModel);
			}
		}
	}

	private void bakeItemTrowelModels(ModelBakeEvent event) {
		for (Material material : MaterialRegistry.getAll()) {
			HashMap<String, String> textures = new HashMap<String, String>();
			textures.put("all", material.sideTextureLocation());
			IRetexturableModel itemModel = (IRetexturableModel) event.modelLoader
					.getModel(new ResourceLocation("block/cube_all"));
			itemModel = (IRetexturableModel) itemModel.retexture(ImmutableMap.copyOf(textures));
			IFlexibleBakedModel bakedItemModel = itemModel.bake(new TRSRTransformation(
					new Vector3f(0.3f, 0.5f, 0.2f), new Quat4f(), new Vector3f(0.4f, 0.4f, 0.4f),
					new Quat4f()), Attributes.DEFAULT_BAKED_FORMAT, null);

			itemModel = (IRetexturableModel) event.modelLoader.getModel(new ResourceLocation(
					"buildingbricks:item/trowel"));
			IFlexibleBakedModel baseBakedItemModel = itemModel.bake(itemModel.getDefaultState(),
					Attributes.DEFAULT_BAKED_FORMAT, null);

			ModelTrowel.models.put(material, new ModelTrowel(baseBakedItemModel, bakedItemModel));
		}
		IModel baseItemModel = event.modelLoader.getModel(new ResourceLocation(
				"buildingbricks:item/trowel"));
		IFlexibleBakedModel baseBakedItemModel = baseItemModel.bake(
				baseItemModel.getDefaultState(), Attributes.DEFAULT_BAKED_FORMAT, null);
		event.modelRegistry.putObject(new ModelResourceLocation("buildingbricks:trowel#inventory"),
				new ModelTrowel(baseBakedItemModel));
	}

}
