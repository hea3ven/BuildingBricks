package com.hea3ven.buildingbricks.core.materials.rendering;

import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelManager;
import net.minecraft.client.renderer.block.model.ModelRotation;

import net.minecraftforge.client.model.IModel;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.hea3ven.buildingbricks.core.materials.Material;
import com.hea3ven.buildingbricks.core.materials.MaterialBlockType;

@SideOnly(Side.CLIENT)
public class RenderDefinitionFenceGate extends RenderDefinitionSimple {

	public RenderDefinitionFenceGate(MaterialBlockType blockType) {
		super(blockType);
	}

	@Override
	protected IModel getModel(ModelManager modelManager, Material mat, IBlockState state) {
		if (state.getValue(BlockFenceGate.OPEN))
			return loadModel(modelManager, "block", mat, "fence_gate_open");
		else
			return loadModel(modelManager, "block", mat, "fence_gate_closed");
	}

	@Override
	protected IModelState getModelState(IBlockState state) {
		switch (state.getValue(BlockFenceGate.FACING)) {
			default:
			case NORTH:
				return ModelRotation.X0_Y0;
			case EAST:
				return ModelRotation.X0_Y90;
			case SOUTH:
				return ModelRotation.X0_Y180;
			case WEST:
				return ModelRotation.X0_Y270;
		}
	}
}
