package com.hea3ven.buildingbricks.core.materials.rendering;

import java.util.Map;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelRotation;

import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.IModelState;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.MultiModel;

import com.hea3ven.buildingbricks.core.blocks.properties.BlockProperties;
import com.hea3ven.buildingbricks.core.materials.Material;

public class RenderDefinitionConnectable extends RenderDefinition {

	protected String postModelLocation;
	protected String connModelLocation;
	protected String itemModelLocation;

	public RenderDefinitionConnectable(String postModel, String connectionModel, String itemModel) {
		postModelLocation = postModel;
		connModelLocation = connectionModel;
		itemModelLocation = itemModel;
	}

	@Override
	public IModel getItemModel(Material mat) {
		return getModelOrDefault(itemModelLocation, mat);
	}

	@Override
	public IModelState getItemModelState(IModelState modelState) {
		return ModelRotation.X0_Y90;
	}

	public IModel getModel(IBlockState state, Material mat) {
		IModel base = (postModelLocation != null) ? getModelOrDefault(postModelLocation, mat) : null;
		Map<String, Pair<IModel, IModelState>> parts = Maps.newHashMap();
		if (BlockProperties.getConnectionNorth(state)) {
			parts.put("north",
					Pair.of(getModelOrDefault(connModelLocation, mat), (IModelState) ModelRotation.X0_Y0));
		}
		if (BlockProperties.getConnectionEast(state)) {
			parts.put("east",
					Pair.of(getModelOrDefault(connModelLocation, mat), (IModelState) ModelRotation.X0_Y90));
		}
		if (BlockProperties.getConnectionSouth(state)) {
			parts.put("south",
					Pair.of(getModelOrDefault(connModelLocation, mat), (IModelState) ModelRotation.X0_Y180));
		}
		if (BlockProperties.getConnectionWest(state)) {
			parts.put("west",
					Pair.of(getModelOrDefault(connModelLocation, mat), (IModelState) ModelRotation.X0_Y270));
		}
		return new MultiModel(base, base != null ? base.getDefaultState() : null, parts);
	}

	public IModelState getModelState(IModelState modelState, IBlockState state) {
		if (!BlockProperties.getConnectionNorth(state) && !BlockProperties.getConnectionSouth(state) &&
				BlockProperties.getConnectionEast(state) && BlockProperties.getConnectionWest(state)) {
			return ModelRotation.X0_Y90;
		}
		return modelState;
	}
}
