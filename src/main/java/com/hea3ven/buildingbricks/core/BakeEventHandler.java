package com.hea3ven.buildingbricks.core;

import java.util.HashMap;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import com.google.common.collect.ImmutableMap;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.ModelRotation;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.Attributes;
import net.minecraftforge.client.model.IFlexibleBakedModel;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.IModelState;
import net.minecraftforge.client.model.IRetexturableModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.TRSRTransformation;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameData;

import com.hea3ven.buildingbricks.ModBuildingBricks;
import com.hea3ven.buildingbricks.core.blockstate.EnumBlockHalf;
import com.hea3ven.buildingbricks.core.blockstate.EnumRotation;
import com.hea3ven.buildingbricks.core.client.model.ModelTrowel;
import com.hea3ven.buildingbricks.core.materials.Material;
import com.hea3ven.buildingbricks.core.materials.MaterialRegistry;

public class BakeEventHandler {
	public static final BakeEventHandler instance = new BakeEventHandler();

	private BakeEventHandler() {
	};

	@SubscribeEvent
	public void onModelBakeEvent(ModelBakeEvent event)
	{
		ModBuildingBricks.tml.loader = event.modelLoader;
		ModBuildingBricks.tml.bakery = event.modelBakery;
		// IModel model = ModelLoaderRegistry.getModel(new ResourceLocation(
		// "minecraft:block/half_slab"));
		for (Material material : MaterialRegistry.getAll()) {

			IModel model = ModelLoaderRegistry.getModel(new ResourceLocation(
					"buildingbricks:material/" + material.globalId + "_half_slab"));
			Object bakedModel = model.bake(new ModelLoader.UVLock(model.getDefaultState()),
					Attributes.DEFAULT_BAKED_FORMAT, null);
			event.modelRegistry.putObject(new ModelResourceLocation(
					GameData.getBlockRegistry().getNameForObject(material.getSlabBlock())
							+ "#inventory"), bakedModel);
			for (EnumFacing facing : EnumFacing.VALUES) {
				bakedModel = model.bake(new ModelLoader.UVLock(getModelRotationFromFacing(facing)),
						Attributes.DEFAULT_BAKED_FORMAT, null);
				event.modelRegistry.putObject(new ModelResourceLocation(
						GameData.getBlockRegistry().getNameForObject(material.getSlabBlock())
								+ "#facing=" + facing.getName()), bakedModel);
			}

			model = ModelLoaderRegistry.getModel(new ResourceLocation(
					"buildingbricks:material/" + material.globalId + "_step"));
			IModel modelVertical = ModelLoaderRegistry.getModel(new ResourceLocation(
					"buildingbricks:material/" + material.globalId + "_step_vertical"));
			bakedModel = model.bake(new ModelLoader.UVLock(model.getDefaultState()),
					Attributes.DEFAULT_BAKED_FORMAT, null);
			event.modelRegistry.putObject(new ModelResourceLocation(
					GameData.getBlockRegistry().getNameForObject(material.getStepBlock())
							+ "#inventory"), bakedModel);
			for (EnumBlockHalf half : EnumBlockHalf.values()) {
				for (EnumRotation rot : EnumRotation.values()) {
				bakedModel = model.bake(new ModelLoader.UVLock(getModelRotationFromFacing(rot, half)),
						Attributes.DEFAULT_BAKED_FORMAT, null);
				event.modelRegistry.putObject(new ModelResourceLocation(
						GameData.getBlockRegistry().getNameForObject(material.getStepBlock())
								+ "#half=" + half.getName() + ",rotation=" + rot.getName() + ",vertical=false"), bakedModel);
				bakedModel = modelVertical.bake(new ModelLoader.UVLock(getModelRotationVertical(rot)),
						Attributes.DEFAULT_BAKED_FORMAT, null);
				event.modelRegistry.putObject(new ModelResourceLocation(
						GameData.getBlockRegistry().getNameForObject(material.getStepBlock())
								+ "#half=" + half.getName() + ",rotation=" + rot.getName() + ",vertical=true"), bakedModel);
				}
			}

			model = ModelLoaderRegistry.getModel(new ResourceLocation(
					"buildingbricks:material/" + material.globalId + "_corner"));
			bakedModel = model.bake(new ModelLoader.UVLock(model.getDefaultState()),
					Attributes.DEFAULT_BAKED_FORMAT, null);
			event.modelRegistry.putObject(new ModelResourceLocation(
					GameData.getBlockRegistry().getNameForObject(material.getCornerBlock())
							+ "#inventory"), bakedModel);
			for (EnumBlockHalf half : EnumBlockHalf.values()) {
				for (EnumRotation rot : EnumRotation.values()) {
				bakedModel = model.bake(new ModelLoader.UVLock(getModelRotationFromFacing(rot, half)),
						Attributes.DEFAULT_BAKED_FORMAT, null);
				event.modelRegistry.putObject(new ModelResourceLocation(
						GameData.getBlockRegistry().getNameForObject(material.getCornerBlock())
								+ "#half=" + half.getName() + ",rotation=" + rot.getName() + ""), bakedModel);
				}
			}
		}

		for (Material material : MaterialRegistry.getAll()) {
			HashMap<String, String> textures = new HashMap<String, String>();
			textures.put("all", material.sideTextureLocation);
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

			ModelTrowel.models.put(material, new ModelTrowel(baseBakedItemModel,
					bakedItemModel));
		}
		IModel baseItemModel = event.modelLoader.getModel(new ResourceLocation(
				"buildingbricks:item/trowel"));
		IFlexibleBakedModel baseBakedItemModel = baseItemModel.bake(
				baseItemModel.getDefaultState(), Attributes.DEFAULT_BAKED_FORMAT, null);
		event.modelRegistry.putObject(new ModelResourceLocation(
				"buildingbricks:trowel#inventory"), new ModelTrowel(baseBakedItemModel));
	}

	private IModelState getModelRotationVertical(EnumRotation rot) {
		return ModelRotation.getModelRotation(0, rot.getAngleDeg());
	}

	private IModelState getModelRotationFromFacing(EnumFacing facing) {
		switch (facing) {
		default:
		case DOWN:
			return ModelRotation.X0_Y0;
		case UP:
			return ModelRotation.X180_Y0;
		case SOUTH:
			return ModelRotation.X90_Y0;
		case WEST:
			return ModelRotation.X90_Y90;
		case NORTH:
			return ModelRotation.X90_Y180;
		case EAST:
			return ModelRotation.X90_Y270;
		}
	}

	private IModelState getModelRotationFromFacing(EnumRotation rot, EnumBlockHalf half) {

		TRSRTransformation translate = new TRSRTransformation((half == EnumBlockHalf.BOTTOM) ? null
				: new Vector3f(0.0f, 0.5f, 0.0f), null, null, null);
		return translate.compose(new TRSRTransformation(ModelRotation.getModelRotation(0,
				rot.getAngleDeg())));
	}

}
