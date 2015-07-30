package com.hea3ven.buildingbricks.core.materials.rendering;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.model.ModelRotation;

import net.minecraftforge.client.model.IModelState;

import com.hea3ven.buildingbricks.core.blocks.properties.BlockProperties;

public class RenderDefinitionSlab extends RenderDefinitionSimple {

	public RenderDefinitionSlab() {
		super("minecraft:block/half_slab");
	}

	@Override
	public IModelState getModelState(IModelState modelState, IBlockState state) {
		switch (BlockProperties.getFacing(state)) {
		default:
		case DOWN:
			modelState = ModelRotation.X0_Y0;
			break;
		case UP:
			modelState = ModelRotation.X180_Y0;
			break;
		case SOUTH:
			modelState = ModelRotation.X90_Y0;
			break;
		case WEST:
			modelState = ModelRotation.X90_Y90;
			break;
		case NORTH:
			modelState = ModelRotation.X90_Y180;
			break;
		case EAST:
			modelState = ModelRotation.X90_Y270;
			break;
		}
		return super.getModelState(modelState, state);
	}
}
