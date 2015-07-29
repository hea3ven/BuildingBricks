package com.hea3ven.buildingbricks.core.materials.rendering;

import javax.vecmath.Vector3f;

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
		boolean bottom = BlockProperties.getHalf(state) == EnumBlockHalf.BOTTOM;
		TRSRTransformation translate = new TRSRTransformation(
				bottom ? null : new Vector3f(0.0f, 0.5f, 0.0f), null, null, null);
		int angle = BlockProperties.getRotation(state).getAngleDeg() - 90;
		ModelRotation modelRot = ModelRotation.getModelRotation(0, angle);
		modelState = translate.compose(new TRSRTransformation(modelRot));
		return super.getModelState(modelState, state);
	}
}
