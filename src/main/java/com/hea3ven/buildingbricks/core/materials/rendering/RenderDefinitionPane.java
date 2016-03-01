package com.hea3ven.buildingbricks.core.materials.rendering;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.model.ModelRotation;

import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.IModelState;

import com.hea3ven.buildingbricks.core.blocks.properties.BlockProperties;
import com.hea3ven.buildingbricks.core.materials.Material;

public class RenderDefinitionPane extends RenderDefinition {
	@Override
	public IModel getItemModel(Material mat) {
		return getModelOrDefault("minecraft:item/glass_pane", mat, "item");
	}

	@Override
	public IModelState getItemModelState(IModelState modelState) {
		return modelState;
	}

	@Override
	public IModel getModel(IBlockState state, Material mat) {
		int connections = 0;
		if (BlockProperties.getConnectionNorth(state))
			connections++;
		if (BlockProperties.getConnectionSouth(state))
			connections++;
		int ns = connections;
		if (BlockProperties.getConnectionEast(state))
			connections++;
		if (BlockProperties.getConnectionWest(state))
			connections++;

		if (connections == 0)
			return getModelOrDefault("buildingbricks:block/pane", mat);

		if (connections == 1)
			return getModelOrDefault("buildingbricks:block/pane_n", mat);

		if (connections == 2) {
			if (ns == 2 || ns == 0)
				return getModelOrDefault("buildingbricks:block/pane_ns", mat);
			else
				return getModelOrDefault("buildingbricks:block/pane_ne", mat);
		}

		if (connections == 3)
			return getModelOrDefault("minecraft:block/pane_nse", mat);
		if (connections == 4)
			return getModelOrDefault("minecraft:block/pane_nsew", mat);

		throw new IllegalStateException();
	}

	@Override
	public IModelState getModelState(IModelState modelState, IBlockState state) {
		int connections = 0;
		if (BlockProperties.getConnectionNorth(state))
			connections++;
		if (BlockProperties.getConnectionSouth(state))
			connections++;
		int ns = connections;
		if (BlockProperties.getConnectionEast(state))
			connections++;
		if (BlockProperties.getConnectionWest(state))
			connections++;

		if (connections == 0)
			return ModelRotation.X0_Y0;

		if (connections == 2) {
			if (ns == 2)
				return ModelRotation.X0_Y0;
			else if (ns == 0)
				return ModelRotation.X0_Y270;
		}

		if (BlockProperties.getConnectionNorth(state) && !BlockProperties.getConnectionWest(state)) {
			return ModelRotation.X0_Y0;
		}
		if (BlockProperties.getConnectionEast(state) && !BlockProperties.getConnectionNorth(state)) {
			return ModelRotation.X0_Y90;
		}
		if (BlockProperties.getConnectionSouth(state) && !BlockProperties.getConnectionEast(state)) {
			return ModelRotation.X0_Y180;
		}
		if (BlockProperties.getConnectionWest(state) && !BlockProperties.getConnectionSouth(state)) {
			return ModelRotation.X0_Y270;
		}
		return ModelRotation.X0_Y0;
	}
}
