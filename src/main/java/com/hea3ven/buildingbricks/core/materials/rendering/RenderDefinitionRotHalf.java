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
import com.hea3ven.buildingbricks.core.materials.MaterialBlockType;

@SideOnly(Side.CLIENT)
public class RenderDefinitionRotHalf extends RenderDefinitionSimple {

	public RenderDefinitionRotHalf(MaterialBlockType blockType) {
		super(blockType);
	}

	@Override
	public IModelState getItemModelState() {
		return ModelRotation.X0_Y90;
	}

	@Override
	public IModelState getModelState(IBlockState state) {
		EnumRotation rot = state.getValue(BlockProperties.ROTATION);
		ModelRotation modelRot = ModelRotation.getModelRotation(0, rot.getAngleDeg());
		boolean bottom = BlockProperties.getHalf(state) == EnumBlockHalf.BOTTOM;
		Vector3f translation = bottom ? null : new Vector3f(0.0f, 0.5f, 0.0f);
		TRSRTransformation translate = new TRSRTransformation(translation, null, null, null);
		return translate.compose(new TRSRTransformation(modelRot));
	}
}
