package com.hea3ven.buildingbricks.core.materials.rendering;

import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.model.ModelRotation;
import net.minecraft.util.EnumFacing;

import net.minecraftforge.client.model.IModelState;
import net.minecraftforge.client.model.TRSRTransformation;

import com.hea3ven.buildingbricks.core.blocks.properties.BlockProperties;

public class RenderDefinitionStairs extends RenderDefinitionSimple {

	public RenderDefinitionStairs(String modelLocation) {
		super(modelLocation);
	}

	@Override
	public IModelState getItemModelState(IModelState modelState) {
		return super.getItemModelState(ModelRotation.X0_Y180);
	}

	@Override
	public IModelState getModelState(IModelState modelState, IBlockState state) {
		if (BlockProperties.getProp(state, BlockStairs.HALF) == BlockStairs.EnumHalf.BOTTOM) {
			int angle = getAngle(BlockProperties.<EnumFacing> getProp(state, BlockStairs.FACING));
			modelState = ModelRotation.getModelRotation(0, angle);
		} else {
			TRSRTransformation translate = new TRSRTransformation(null, null, null, null);
			int angle = getAngle(BlockProperties.<EnumFacing> getProp(state, BlockStairs.FACING))
					- 90;
			ModelRotation modelRot = ModelRotation.getModelRotation(180, angle);
			modelState = translate.compose(new TRSRTransformation(modelRot));
		}
		return super.getModelState(modelState, state);
	}

	private int getAngle(EnumFacing prop) {
		switch (prop) {
		default:
		case NORTH:
			return 0;
		case EAST:
			return 90;
		case SOUTH:
			return 180;
		case WEST:
			return 270;
		}
	}
}
