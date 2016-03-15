package com.hea3ven.buildingbricks.core.materials.rendering;

import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.IModelState;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.hea3ven.buildingbricks.core.blocks.properties.BlockProperties;
import com.hea3ven.buildingbricks.core.materials.Material;

@SideOnly(Side.CLIENT)
public class RenderDefinitionFenceGate extends RenderDefinition {

	public RenderDefinitionFenceGate() {
	}

	@Override
	public IModel getItemModel(Material mat) {
		return getModelOrDefault("minecraft:block/fence_gate_closed", mat);
	}

	@Override
	public IModelState getItemModelState(IModelState modelState) {
		return modelState;
	}

	@Override
	public IModel getModel(IBlockState state, Material mat) {
		if (BlockProperties.<Boolean>getProp(state, BlockFenceGate.OPEN))
			return getModelOrDefault("minecraft:block/fence_gate_open", mat);
		else
			return getModelOrDefault("minecraft:block/fence_gate_closed", mat);
	}

	@Override
	public IModelState getModelState(IModelState modelState, IBlockState state) {
		return ModelRotation.X0_Y90;
	}
}
