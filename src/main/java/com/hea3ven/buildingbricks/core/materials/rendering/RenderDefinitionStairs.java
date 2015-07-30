package com.hea3ven.buildingbricks.core.materials.rendering;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.model.ModelRotation;

import net.minecraftforge.client.model.IModelState;
import net.minecraftforge.client.model.TRSRTransformation;

import com.hea3ven.buildingbricks.core.blocks.properties.BlockProperties;
import com.hea3ven.buildingbricks.core.blockstate.EnumBlockHalf;

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
		if (BlockProperties.getVertical(state)) {
			int angle = BlockProperties.getRotation(state).getAngleDeg() - 90;
			modelState = ModelRotation.getModelRotation(90, angle);
		} else if (BlockProperties.getHalf(state) == EnumBlockHalf.BOTTOM) {
			int angle = BlockProperties.getRotation(state).getAngleDeg() - 90;
			modelState = ModelRotation.getModelRotation(0, angle);
		} else {
			TRSRTransformation translate = new TRSRTransformation(null, null, null, null);
			int angle = BlockProperties.getRotation(state).getAngleDeg();
			ModelRotation modelRot = ModelRotation.getModelRotation(180, angle);
			modelState = translate.compose(new TRSRTransformation(modelRot));
		}
		return super.getModelState(modelState, state);
	}
}
