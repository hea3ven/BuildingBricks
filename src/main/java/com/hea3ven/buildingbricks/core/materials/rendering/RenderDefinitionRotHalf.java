package com.hea3ven.buildingbricks.core.materials.rendering;

import javax.vecmath.Vector3f;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelRotation;

import net.minecraftforge.client.model.IModelState;
import net.minecraftforge.client.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.hea3ven.buildingbricks.core.blocks.properties.BlockProperties;
import com.hea3ven.buildingbricks.core.blockstate.EnumBlockHalf;
import com.hea3ven.buildingbricks.core.blockstate.EnumRotation;

@SideOnly(Side.CLIENT)
public class RenderDefinitionRotHalf extends RenderDefinitionSimple {

	public RenderDefinitionRotHalf(String modelLocation) {
		super(modelLocation);
	}
	
	@Override
	public IModelState getItemModelState(IModelState modelState) {
		return super.getItemModelState(ModelRotation.X0_Y90);
	}

	@Override
	public IModelState getModelState(IModelState modelState, IBlockState state) {
		EnumRotation rot = BlockProperties.getRotation(state);
		ModelRotation modelRot = ModelRotation.getModelRotation(0, rot.getAngleDeg());
		boolean bottom = BlockProperties.getHalf(state) == EnumBlockHalf.BOTTOM;
		Vector3f translation = bottom ? null : new Vector3f(0.0f, 0.5f, 0.0f);
		TRSRTransformation translate = new TRSRTransformation(translation, null, null, null);
		modelState = translate.compose(new TRSRTransformation(modelRot));
		return super.getModelState(modelState, state);
	}
}
