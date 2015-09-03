package com.hea3ven.buildingbricks.core.materials.rendering;

import java.util.Map;

import com.google.common.collect.Maps;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.ModelRotation;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.IModelState;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.MultiModel;

import com.hea3ven.buildingbricks.core.blocks.properties.BlockProperties;
import com.hea3ven.buildingbricks.core.materials.Material;

public class RenderDefinitionWall implements IRenderDefinition {

	private ResourceLocation postModelLocation = new ResourceLocation("minecraft:block/wall_post");
	private ResourceLocation connModelLocation = new ResourceLocation(
			"buildingbricks:block/wall_connection");

	@Override
	public IModel getItemModel(Material mat) {
		IModel base = ModelLoaderRegistry.getModel(postModelLocation);
		Map<String, Pair<IModel, IModelState>> parts = Maps.newHashMap();
		parts.put("north", Pair.of(ModelLoaderRegistry.getModel(connModelLocation),
				(IModelState) new ModelLoader.UVLock(base.getDefaultState())));
		parts.put("south", Pair.of(ModelLoaderRegistry.getModel(connModelLocation),
				(IModelState) new ModelLoader.UVLock(ModelRotation.X0_Y180)));
		return new MultiModel(base, base.getDefaultState(), parts);
	}

	@Override
	public IModelState getItemModelState(IModelState modelState) {
		return modelState;
	}

	@Override
	public IModel getModel(IBlockState state, Material mat) {
		if ((BlockProperties.getConnectionNorth(state) && BlockProperties.getConnectionSouth(state)
				&& !BlockProperties.getConnectionEast(state)
				&& !BlockProperties.getConnectionWest(state))
				|| (!BlockProperties.getConnectionNorth(state)
						&& !BlockProperties.getConnectionSouth(state)
						&& BlockProperties.getConnectionEast(state)
						&& BlockProperties.getConnectionWest(state))) {
			return ModelLoaderRegistry
					.getModel(new ModelResourceLocation("minecraft:block/wall_ns"));
		}
		IModel base = null;
		base = ModelLoaderRegistry.getModel(postModelLocation);
		Map<String, Pair<IModel, IModelState>> parts = Maps.newHashMap();
		if (BlockProperties.getConnectionNorth(state)) {
			parts.put("north", Pair.of(ModelLoaderRegistry.getModel(connModelLocation),
					(IModelState) new ModelLoader.UVLock(ModelRotation.X0_Y0)));
		}
		if (BlockProperties.getConnectionEast(state)) {
			parts.put("east", Pair.of(ModelLoaderRegistry.getModel(connModelLocation),
					(IModelState) new ModelLoader.UVLock(ModelRotation.X0_Y90)));
		}
		if (BlockProperties.getConnectionSouth(state)) {
			parts.put("south", Pair.of(ModelLoaderRegistry.getModel(connModelLocation),
					(IModelState) new ModelLoader.UVLock(ModelRotation.X0_Y180)));
		}
		if (BlockProperties.getConnectionWest(state)) {
			parts.put("west", Pair.of(ModelLoaderRegistry.getModel(connModelLocation),
					(IModelState) new ModelLoader.UVLock(ModelRotation.X0_Y270)));
		}
		return new MultiModel(base, base != null ? base.getDefaultState() : null, parts);
	}

	@Override
	public IModelState getModelState(IModelState modelState, IBlockState state) {
		if (!BlockProperties.getConnectionNorth(state) && !BlockProperties.getConnectionSouth(state)
				&& BlockProperties.getConnectionEast(state)
				&& BlockProperties.getConnectionWest(state)) {
			return ModelRotation.X0_Y90;
		}
		return modelState;
	}

}
