package com.hea3ven.buildingbricks.core.materials.rendering;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.IModelState;
import net.minecraftforge.client.model.ModelLoaderRegistry;

import com.hea3ven.buildingbricks.core.blocks.properties.BlockProperties;

public class RenderDefinitionStep extends RenderDefinitionRotHalf {

	private ResourceLocation verticalModelLocation;

	public RenderDefinitionStep() {
		super("buildingbricks:block/step_bottom");
		verticalModelLocation = new ResourceLocation("buildingbricks:block/step_vertical");
	}

	@Override
	public IModel getModel(IBlockState state) {
		if (BlockProperties.getVertical(state))
			return ModelLoaderRegistry.getModel(verticalModelLocation);
		return super.getModel(state);
	}

	@Override
	public IModelState getModelState(IModelState modelState, IBlockState state) {
		// if (BlockProperties.getVertical(state)) {
		// EnumRotation rot = BlockProperties.getRotation(state);
		// ModelRotation modelRot = ModelRotation.getModelRotation(0,
		// rot.getAngleDeg());
		// return modelRot;
		// }
		return super.getModelState(modelState, state);
	}
}
