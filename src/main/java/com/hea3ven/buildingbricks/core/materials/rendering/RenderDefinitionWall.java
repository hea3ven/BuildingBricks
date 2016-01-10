package com.hea3ven.buildingbricks.core.materials.rendering;

import java.util.Map;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.block.BlockWall;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.model.ModelRotation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.IModelState;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.MultiModel;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.hea3ven.buildingbricks.core.blocks.properties.BlockProperties;
import com.hea3ven.buildingbricks.core.materials.Material;

@SideOnly(Side.CLIENT)
public class RenderDefinitionWall extends RenderDefinitionConnectable {

	public RenderDefinitionWall(String postModel, String connectionModel) {
		super(postModel, connectionModel, null);
	}

	@Override
	public IModel getItemModel(Material mat) {
		IModel base = getModelOrDefault(postModelLocation, mat);
		Map<String, Pair<IModel, IModelState>> parts = Maps.newHashMap();
		parts.put("north", Pair.of(getModelOrDefault(connModelLocation, mat),
				(IModelState) new ModelLoader.UVLock(base.getDefaultState())));
		parts.put("south", Pair.of(getModelOrDefault(connModelLocation, mat),
				(IModelState) new ModelLoader.UVLock(ModelRotation.X0_Y180)));
		return new MultiModel(base, base.getDefaultState(), parts);
	}

	@Override
	public IModel getModel(IBlockState state, Material mat) {
		if (!state.getValue(BlockWall.UP) && (BlockProperties.getConnectionNorth(state)
				&& BlockProperties.getConnectionSouth(state) && !BlockProperties.getConnectionEast(state)
				&& !BlockProperties.getConnectionWest(state)) || (!BlockProperties.getConnectionNorth(state)
				&& !BlockProperties.getConnectionSouth(state) && BlockProperties.getConnectionEast(state)
				&& BlockProperties.getConnectionWest(state))) {
			return getModelOrDefault("minecraft:block/wall_ns", mat);
		}
		return super.getModel(state, mat);
	}
}
